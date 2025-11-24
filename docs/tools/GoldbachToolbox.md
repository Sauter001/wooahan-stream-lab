# GoldbachToolbox - 골드바흐 추측 검증 도구 모음

Secret Phase 문제에서 골드바흐 추측 검증을 위해 제공되는 도구입니다.
내부적으로 에라토스테네스의 체(Sieve of Eratosthenes)가 미리 계산되어 O(1) 소수 판별이 가능합니다.

## 사용 방법

**중요**: `GoldbachToolbox`는 직접 import하여 사용할 수 없습니다. 메서드 파라미터로 전달받아 사용해야 합니다.

```java
// ❌ 잘못된 사용 - import 불가
import domain.tools.GoldbachToolbox;
import domain.tools.GoldbachToolboxImpl;

// ✅ 올바른 사용 - 파라미터로 전달받음
public static long sumTop100GoldbachPrimes(int maxN, GoldbachToolbox tools) {
    return IntStream.iterate(4, n -> n <= maxN, n -> n + 2)
        .parallel()
        .flatMap(n -> IntStream.rangeClosed(2, n / 2)
            .filter(p -> tools.isPrime(p) && tools.isPrime(n - p)))
        // ...
}
```

---

## 메서드 목록

| 메서드 | 설명 | 시간복잡도 |
|--------|------|-----------|
| `isPrime(n)` | n이 소수인지 판별 | O(1) |
| `primes(maxN)` | 2~maxN의 소수 IntStream | O(maxN) |
| `getMaxN()` | 지원 최대 범위 | O(1) |

---

## 핵심 메서드 상세

### isPrime(int n)

주어진 수가 소수인지 O(1)에 판별합니다.

```java
tools.isPrime(2)   // → true
tools.isPrime(4)   // → false
tools.isPrime(17)  // → true
tools.isPrime(1)   // → false
```

**내부 구현**: 생성 시 에라토스테네스의 체로 소수 여부를 미리 계산해둡니다.

---

### primes(int maxN)

2부터 maxN까지의 모든 소수를 IntStream으로 반환합니다.

```java
tools.primes(20)
// → IntStream.of(2, 3, 5, 7, 11, 13, 17, 19)
```

**용도**: 소수 목록이 필요할 때 사용 (골드바흐 문제에서는 주로 isPrime 사용)

---

### getMaxN()

이 Toolbox가 지원하는 최대 범위를 반환합니다.

```java
tools.getMaxN()  // → 100000 (문제에 따라 다름)
```

**주의**: maxN을 초과하는 수에 대해 isPrime을 호출하면 ArrayIndexOutOfBoundsException 발생

---

## 골드바흐 추측이란?

> 2보다 큰 모든 짝수는 두 소수의 합으로 나타낼 수 있다.

예시:
- 4 = 2 + 2
- 6 = 3 + 3
- 8 = 3 + 5
- 10 = 3 + 7 = 5 + 5
- 100 = 3 + 97 = 11 + 89 = 17 + 83 = ...

---

## 활용 예시

### 골드바흐 쌍 찾기

짝수 n에 대해 골드바흐 쌍(p, n-p)을 찾습니다:

```java
// n=10의 골드바흐 쌍에서 작은 소수들
IntStream.rangeClosed(2, 10 / 2)
    .filter(p -> tools.isPrime(p) && tools.isPrime(10 - p))
// → IntStream.of(3, 5)  // (3,7), (5,5)
```

### 상위 100개 소수 합 (S-3 문제)

```java
public static long sumTop100GoldbachPrimes(int maxN, GoldbachToolbox tools) {
    return IntStream.iterate(4, n -> n <= maxN, n -> n + 2)
        .parallel()                           // 병렬 처리 필수
        .flatMap(n -> IntStream.rangeClosed(2, n / 2)
            .filter(p -> tools.isPrime(p) && tools.isPrime(n - p)))
        .boxed()
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
        .entrySet().stream()
        .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
        .limit(100)
        .mapToLong(Map.Entry::getKey)
        .sum();
}
```

**핵심 포인트**:
1. `parallel()`: 시간 제한(1.5초) 내 처리를 위해 필수
2. `flatMap`: 각 짝수에서 골드바흐 쌍의 소수들을 평탄화
3. `groupingBy + counting`: 각 소수의 등장 횟수 집계
4. `sorted + limit(100)`: 상위 100개 추출

---

## 성능 고려사항

### 왜 parallelStream이 필요한가?

| 범위 | Sequential | Parallel |
|------|------------|----------|
| 10,000 | ~200ms | ~100ms |
| 50,000 | ~1.5s | ~250ms |
| 100,000 | ~3s+ | ~900ms |

문제의 시간 제한(1.5초)은 `parallel()` 사용을 강제합니다.

### O(1) 소수 판별의 중요성

만약 isPrime이 O(√n)이었다면:
- 각 짝수당 n/2번의 소수 판별
- 총 연산: O(n² × √n) → 수십 초 소요

에라토스테네스의 체로 O(1) 판별:
- 전처리: O(n log log n)
- 판별: O(1)
- 총 연산: O(n²) → parallel로 1초 이내

---

## 주의사항

1. **import 금지**: `domain.tools` 패키지 import 시 검증 실패합니다.
2. **파라미터 전용**: 문제의 메서드 시그니처에 포함된 `GoldbachToolbox` 파라미터만 사용하세요.
3. **범위 제한**: `getMaxN()`을 초과하는 수에 대한 판별은 오류 발생합니다.
4. **병렬 처리**: S-3 문제는 `parallel()` 없이는 시간 초과됩니다.
