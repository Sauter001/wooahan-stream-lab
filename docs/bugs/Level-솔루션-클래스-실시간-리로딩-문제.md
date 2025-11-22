# Level 솔루션 클래스 실시간 리로딩 문제

## 1. 문제 발견

### 증상
- Level1.java 파일을 수정하고 저장해도 채점 결과가 변경되지 않음
- 프로그램을 완전히 재시작해야만 수정된 코드가 반영됨
- FileWatcher는 파일 변경을 정상적으로 감지함

### 기대 동작
사용자가 솔루션 파일을 수정하고 Ctrl+S로 저장하면 실시간으로 채점이 수행되어야 함

---

## 2. 시행착오

### 시도 1: URLClassLoader 사용
```java
// 매번 새로운 ClassLoader 생성
URLClassLoader classLoader = new URLClassLoader(urls, getClass().getClassLoader());
return classLoader.loadClass(className);
```
**결과**: 실패. 여전히 이전 클래스가 로드됨

### 시도 2: try-with-resources로 ClassLoader 닫기
```java
try (URLClassLoader classLoader = new URLClassLoader(urls, getClass().getClassLoader())) {
    return classLoader.loadClass(className);
}
```
**결과**: 실패. ClassLoader를 닫아도 캐싱 문제 해결 안 됨

### 시도 3: Parent ClassLoader를 PlatformClassLoader로 변경
```java
new URLClassLoader(urls, ClassLoader.getPlatformClassLoader());
```
**결과**: 실패. `Student`, `Product` 등 애플리케이션 클래스를 찾지 못함
- **도전 요소**: Parent ClassLoader 계층 구조 이해 필요
- PlatformClassLoader는 JDK 클래스만 로드하므로 도메인 클래스 접근 불가

### 시도 4: Custom ClassLoader로 Parent Delegation 우회
```java
private static class LevelReloadingClassLoader extends URLClassLoader {
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.equals(targetClassName)) {
            return findClass(name);  // Parent 위임 없이 직접 로드
        }
        return super.loadClass(name);
    }
}
```
**결과**: 실패. URLClassLoader 내부 캐싱으로 여전히 이전 클래스 로드

### 시도 5: 바이트 배열 직접 읽기 + defineClass
```java
byte[] classBytes = Files.readAllBytes(classFile);
return defineClass(name, classBytes, 0, classBytes.length);
```
**결과**: 부분 성공. 클래스는 새로 로드되나, .class 파일 자체가 업데이트 안 됨

---

## 3. 원인 파악

### 핵심 문제
```
[의도한 흐름]
.java 수정 → 저장 → 컴파일 → .class 업데이트 → 새 클래스 로드 → 채점

[실제 흐름]
.java 수정 → 저장 → FileWatcher 감지 → 채점 (이전 .class 사용)
                    ↓
              .class 파일은 변경되지 않음!
```

**근본 원인**: 소스 파일(.java) 변경과 컴파일된 파일(.class)이 독립적으로 존재
- FileWatcher는 .java 파일 변경만 감지
- .class 파일은 별도의 컴파일 과정이 필요
- IDE의 auto-build가 없으면 .class는 업데이트되지 않음

### 도전 요소
1. **Java ClassLoader의 Parent Delegation Model**: 기본적으로 부모 ClassLoader에 먼저 위임
2. **URLClassLoader 내부 캐싱**: 동일 URL에서 로드한 클래스 재사용
3. **소스-바이너리 분리**: Java는 컴파일 언어이므로 소스 변경이 바로 반영되지 않음

---

## 4. 해결안 후보

### A. 파일 변경 시 자동 컴파일 트리거
- 장점: 구현 간단, 기존 빌드 시스템 활용
- 단점: 컴파일 시간만큼 지연 발생 (약 2-3초)

### B. Java Compiler API로 인메모리 컴파일
- 장점: 빠른 컴파일, 외부 프로세스 불필요
- 단점: classpath/의존성 처리 복잡, 구현 난이도 높음

### C. 인터프리터 방식 (소스 코드 직접 해석)
- 장점: 컴파일 불필요
- 단점: 구현 매우 복잡, Stream API 지원 어려움

### 선정: A안 (자동 컴파일 트리거)
- 학습 도구 목적상 2-3초 지연은 허용 가능
- 기존 debounce 2초와 합쳐도 사용자 경험에 큰 영향 없음
- 안정적이고 유지보수 용이

---

## 5. 적용

### 최종 구현

**1) ByteArrayClassLoader (공용 클래스)**
```java
public class ByteArrayClassLoader extends ClassLoader {
    private final String targetClassName;
    private final byte[] classBytes;

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.equals(targetClassName)) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return super.loadClass(name);
    }
}
```

**2) 자동 컴파일 메서드**
```java
private boolean compileSource() {
    String gradleCommand = os.contains("win") ? "gradlew.bat" : "./gradlew";
    ProcessBuilder pb = new ProcessBuilder(gradleCommand, "compileJava", "-q");
    // ...
    return exitCode == 0;
}
```

**3) 채점 흐름**
```java
private void executeGrading(Path filePath) {
    // 1. 소스 파일 컴파일
    if (!compileSource()) {
        System.err.println("❌ 컴파일 실패");
        return;
    }

    // 2. .class 파일 바이트 직접 읽기
    byte[] classBytes = Files.readAllBytes(classFile);

    // 3. 새 ClassLoader로 클래스 로드
    ByteArrayClassLoader classLoader = new ByteArrayClassLoader(...);
    Class<?> solutionClass = classLoader.loadClass(className);

    // 4. 채점 수행
    // ...
}
```

### 수정된 파일
- `tools/watcher/ByteArrayClassLoader.java` (신규)
- `tools/watcher/LevelGraderObserver.java`
- `tools/watcher/TutorialGraderObserver.java`

---

## 6. 깨달은 점

### 기술적 학습
1. **Java ClassLoader 동작 원리**
   - Parent Delegation Model의 이해
   - 클래스 캐싱 메커니즘
   - defineClass()를 통한 동적 클래스 정의

2. **컴파일 vs 런타임의 분리**
   - Java는 컴파일 언어이므로 소스 변경 → 바이너리 변경이 자동이 아님
   - Hot Reload를 위해서는 별도의 컴파일 트리거 필요

3. **URLClassLoader의 한계**
   - 동일 경로의 클래스를 다시 로드해도 캐싱된 버전 반환
   - 완전한 리로딩을 위해서는 바이트 직접 읽기 + defineClass 필요

### 설계적 교훈
- "파일 변경 감지 → 즉시 반영"이라는 단순한 요구사항도 컴파일 언어에서는 복잡한 문제가 될 수 있음
- 사용자 경험(실시간 피드백)과 기술적 제약(컴파일 필요) 사이의 트레이드오프 고려 필요

---

## 날짜
2024-11-23
