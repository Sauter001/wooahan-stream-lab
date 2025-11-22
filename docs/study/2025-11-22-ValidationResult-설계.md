# ValidationResult 설계: sealed interface + record

## 목적

코드 검증 결과를 **타입 안전**하게 표현하기 위한 설계.

---

## 기존 방식의 문제점

### boolean 반환
```java
boolean validate(AnalysisContext ctx);
// 실패 이유, 위치, 심각도 등 아무 정보 없음
```

### 예외 던지기
```java
void validate(AnalysisContext ctx) throws ValidationException;
// 검증 실패는 예외적 상황이 아닌 정상적인 비즈니스 로직
```

### null 반환
```java
String validate(AnalysisContext ctx); // null이면 성공?
// null 체크 누락 시 NPE, 여러 에러 표현 불가
```

---

## 해결책: Result Pattern

```java
public sealed interface ValidationResult {
    record Ok() implements ValidationResult {}
    record Violation(
        Severity severity,
        String message,
        int lineNumber,
        String codeSnippet
    ) implements ValidationResult {}
}
```

---

## sealed의 장점

### 1. 완전성 검사 (Exhaustiveness)

```java
switch (result) {
    case Ok ok -> handleSuccess();
    case Violation v -> handleError(v);
    // default 불필요 - 컴파일러가 모든 케이스 확인
}
```

새로운 케이스 추가 시 **컴파일 에러**로 누락 방지.

### 2. 확장 방지

```java
// sealed가 없으면 누군가 마음대로 구현 가능
class WeirdResult implements ValidationResult {} // sealed로 차단
```

---

## record의 장점

```java
record Violation(Severity severity, String message, int lineNumber, String codeSnippet)
```

자동 생성:
- 불변 필드 (final)
- 생성자, getter
- equals/hashCode (값 기반)
- toString

---

## Sum Type 표현

```
ValidationResult = Ok | Violation
```

함수형 언어의 Sum Type을 자바로 구현:

| 언어 | 문법 |
|------|------|
| Haskell | `data Either a b = Left a \| Right b` |
| Rust | `enum Result<T, E> { Ok(T), Err(E) }` |
| Kotlin | `sealed class Result` |
| Java | `sealed interface` + `record` |

---

## Haskell Either Monad와의 관계

ValidationResult는 Haskell의 `Either` monad와 동일한 패턴:

```haskell
data Either a b = Left a | Right b
-- Left = 실패 (에러 정보)
-- Right = 성공 (결과 값)
```

**매핑:**
| Either | ValidationResult |
|--------|------------------|
| `Left error` | `Violation(...)` |
| `Right value` | `Ok()` |

Either의 핵심 연산인 `>>=` (bind/flatMap)을 자바로 표현:

```java
default ValidationResult flatMap(Function<Ok, ValidationResult> f) {
    return switch(this) {
        case Ok ok -> f.apply(ok);
        case Violation v -> v;  // 실패 시 단락(short-circuit)
    };
}
```

이는 **Railway Oriented Programming**의 기반:
- 성공 트랙(Right/Ok): 다음 연산으로 진행
- 실패 트랙(Left/Violation): 즉시 반환, 이후 연산 스킵

> Either monad는 예외 없이 실패를 **값으로 표현**하는 함수형 에러 처리의 표준 패턴.

---

## 패턴 매칭 활용

```java
String msg = switch(result) {
    case Ok() -> "통과";
    case Violation(var sev, var msg, var line, _) ->
        "[%s] %s (Line %d)".formatted(sev, msg, line);
};
```

중첩 패턴 매칭:
```java
boolean critical = switch(result) {
    case Violation(Severity.ERROR, _, _, _) -> true;
    case Ok(), Violation(_, _, _, _) -> false;
};
```

---

## 확장 예시

```java
sealed interface ValidationResult {
    record Ok() implements ValidationResult {}
    record Violation(...) implements ValidationResult {}
    record MultipleViolations(List<Violation> violations)
        implements ValidationResult {}
}
```

---

## 함수 체이닝 (Railway Oriented)

```java
ValidationResult finalResult = validator1.validate(ctx)
    .flatMap(r -> validator2.validate(ctx))
    .flatMap(r -> validator3.validate(ctx));
// 하나라도 실패하면 즉시 중단
```

---

## 비교 정리

| 방식 | 타입 안전 | 정보 전달 | 확장성 | 함수형 |
|------|:--------:|:--------:|:------:|:------:|
| boolean | X | X | X | X |
| 예외 | △ | O | △ | X |
| null | X | △ | X | X |
| **sealed+record** | **O** | **O** | **O** | **O** |

---

## 결론

- **타입 안전**: 컴파일러가 모든 케이스 처리 강제
- **명확성**: Ok/Violation 구분이 명시적
- **정보 전달**: 위치, 이유, 심각도 포함
- **확장성**: Warning, Info 등 추가 용이
- **패턴 매칭**: switch로 깔끔한 분기 처리

> 타입 시스템을 활용한 **컴파일 타임 검증**이 핵심.
