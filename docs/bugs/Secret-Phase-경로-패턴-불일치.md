# Secret Phase 경로 패턴 불일치 버그

**날짜**: 2025-11-24

---

## 1. 문제 인식

### 증상
- Secret Phase(Level 6) 진입 시 `FileNotFoundException` 발생
- 에러 메시지: `level6/level6.json (지정된 경로를 찾을 수 없습니다)`

```
java.io.FileNotFoundException: data/test-data/level6/level6.json
```

### 재현 조건
1. Secret Phase 해금 완료
2. 메인 메뉴에서 `s` 입력하여 Secret Phase 진입
3. 채점 시스템 초기화 시점에 예외 발생

---

## 2. 원인 발견

### 경로 생성 패턴
`LevelGraderObserver`가 레벨 번호 기반 패턴으로 경로를 생성:

```java
private static final String TEST_DATA_PATTERN = "data/test-data/level%d/level%d.json";
private static final String LEVEL_FILE_PATTERN = "Level%d.java";

// 생성자에서
String testDataPath = String.format(TEST_DATA_PATTERN, level, level);
// level=6 → "data/test-data/level6/level6.json"
```

### 실제 Secret Phase 구조
Secret Phase는 설계상 별도 디렉토리 사용:

```
data/test-data/
├── level1/level1.json
├── level2/level2.json
├── ...
├── level5/level5.json
└── secret/secret.json    ← 패턴과 불일치
```

솔루션 파일도 마찬가지:
```
src/main/java/solutions/
├── level1/Level1.java
├── ...
├── level5/Level5.java
└── secret/LevelSecret.java    ← Level6.java가 아님
```

---

## 3. 해결 과정

### 수정 내용

**LevelGraderObserver.java** - Secret Phase 전용 상수 추가:

```java
// Secret Phase (Level 6) 전용 경로
private static final int SECRET_LEVEL = 6;
private static final String SECRET_SOLUTION_CLASS = "solutions.secret.LevelSecret";
private static final String SECRET_TEST_DATA_PATH = "data/test-data/secret/secret.json";
private static final String SECRET_FILE_NAME = "LevelSecret.java";
```

**생성자에서 분기 처리**:

```java
this.expectedFileName = level == SECRET_LEVEL
    ? SECRET_FILE_NAME
    : String.format(LEVEL_FILE_PATTERN, level);

String testDataPath = level == SECRET_LEVEL
    ? SECRET_TEST_DATA_PATH
    : String.format(TEST_DATA_PATTERN, level, level);
```

**클래스 로딩에서도 분기**:

```java
private Class<?> loadSolutionClass() throws Exception {
    String className = level == SECRET_LEVEL
        ? SECRET_SOLUTION_CLASS
        : String.format(SOLUTION_CLASS_PATTERN, level, level);
    // ...
}
```

---

## 4. 교훈

### 패턴 기반 설계의 한계
- 규칙적인 패턴(`level%d`)은 대부분의 케이스를 깔끔하게 처리
- 하지만 "숨겨진" 또는 "특별한" 요소는 패턴을 벗어날 수 있음
- Secret Phase는 의도적으로 다른 네이밍(`secret/`, `LevelSecret`)을 사용

### 특수 케이스 명시적 처리
- 매직 넘버(6)보다는 상수(`SECRET_LEVEL`)로 의도 명확화
- 관련 경로들을 한 곳에 모아서 관리
- 향후 또 다른 특수 레벨 추가 시 동일 패턴 적용 가능

### 대안 검토
- **Option A**: Secret도 `level6/Level6.java`로 통일 → 기각 (숨김 의도 상실)
- **Option B**: 경로를 JSON 설정에서 읽기 → 오버엔지니어링
- **Option C**: 특수 케이스 분기 처리 → 채택 (간단하고 명확)
