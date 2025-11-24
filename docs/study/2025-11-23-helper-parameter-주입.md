# Helper 함수를 파라미터로 주입하는 설계 결정

## 배경

### 문제 상황
Level4부터 멱집합(Powerset) 같은 복잡한 집합 연산 문제가 등장한다. 이러한 문제는:
- 순수 Stream API만으로 풀기에는 지나치게 복잡함
- 재귀적 구조를 표현하기 어려움
- 학습 목표는 Stream API 활용이지, 집합 연산 알고리즘 구현이 아님

따라서 **집합 연산 로직이 구현된 Helper**를 제공해야 했다.

### 핵심 딜레마
Helper를 어떻게 제공할 것인가?

---

## 연구 및 의사결정 과정

### Option 1: 유틸리티 클래스로 제공

```java
// 플레이어 코드
import utils.SetUtils;

public static <T> Set<Set<T>> powerSet(Set<T> set) {
    return SetUtils.generatePowerSet(set);  // 정적 메서드 호출
}
```

**장점**:
- 사용이 간단함
- 기존 Java 생태계 패턴과 일치 (Collections, Arrays 등)

**단점**:
- 어디서든 import 가능 → 치팅 가능성
- 다른 문제에서도 무분별하게 사용될 수 있음
- "Stream API 학습"이라는 목표와 맞지 않음

### Option 2: 상속/추상 클래스 활용

```java
// 플레이어가 상속받아 구현
public class Level4Solution extends SetOperationSupport {
    public <T> Set<Set<T>> powerSet(Set<T> set) {
        return set.stream()
            .reduce(emptySeed(), this::extendAll, this::merge);
    }
}
```

**장점**:
- 메서드를 자연스럽게 사용 가능

**단점**:
- 상속 구조가 복잡해짐
- 기존 Level 클래스 구조와 맞지 않음 (static 메서드 기반)
- protected 메서드로 제공해도 결국 접근 가능

### Option 3: 파라미터로 주입 (의존성 주입 패턴)

```java
// 플레이어 코드
public static <T> Set<Set<T>> powerSet(Set<T> set, SetToolbox<T> tools) {
    return set.stream()
        .reduce(tools.emptySeed(), tools::extendAll, tools::merge);
}
```

**장점**:
- **스코프 제한**: 해당 메서드 내에서만 사용 가능
- **치팅 방지**: import 자체를 금지할 수 있음
- **명시적 의존성**: 어떤 도구가 필요한지 시그니처에서 드러남
- **테스트 용이**: Grader가 구현체를 주입
- **유연성**: 문제마다 다른 도구 세트 제공 가능

**단점**:
- 메서드 시그니처가 길어짐
- 플레이어가 처음 보면 낯설 수 있음

---

## 최종 결정: Option 3 (파라미터 주입)

### 선정 이유

1. **학습 목표 부합**
   - Stream API 활용에 집중하게 함
   - Helper 로직 구현이 아닌, 활용 방법 학습

2. **치팅 완벽 차단**
   - `domain.tools` 패키지 import 시 → 검증 실패
   - 파라미터로 받은 인스턴스만 사용 가능

3. **공구통 컨셉과 일치**
   - 플레이어에게 "이 도구들이 주어집니다. 골라서 쓰세요" 라는 메시지 전달
   - 문서화된 API를 보고 필요한 것만 선택

4. **의존성 주입 패턴 학습**
   - 실무에서 자주 쓰이는 DI 패턴 자연스럽게 노출
   - 테스트 가능한 코드 작성법 체득

### 구현 구조

```
domain/tools/
├── SetToolbox.java       # 인터페이스 (플레이어가 볼 수 있는 API)
└── SetToolboxImpl.java   # 구현체 (Grader가 주입, 플레이어 접근 불가)
```

### 검증 규칙

```java
// ImportValidator에서 체크
List<String> bannedImports = List.of(
    "domain.tools.*"
);
// 위 패턴 import 발견 시 → ValidationResult.Fail
```

---

