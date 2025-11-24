# Secret Phase 문제 설계 결정

## 배경

Secret Phase는 Level 1~5를 클리어하고 숨겨진 조건을 만족한 플레이어를 위한 도전 과제.
기존 Level 문제보다 높은 난이도와 새로운 도구 활용을 요구함.

---

## 문제 구성

| ID | 문제명 | 핵심 기법 | maxVariables |
|----|--------|-----------|--------------|
| S-1 | 순열 생성 | reduce + flatMap + Toolbox | 1 |
| S-2 | 피보나치 수열 | Stream.iterate + Pair | 1 |
| S-3 | 골드바흐 Top 100 | parallelStream + groupingBy | 5 |

---

## 주요 결정 사항

### 1. requiresToolbox 타입 변경 (boolean → String)

**이전**: `"requiresToolbox": true`
**이후**: `"requiresToolbox": "set" | "permutation" | "goldbach"`

**이유**:
- Level 4의 SetToolbox만 있던 상황에서 새로운 Toolbox 종류 추가
- PermutationToolbox: 순열 생성 도우미
- GoldbachToolbox: 소수 판별 도우미 (Sieve 기반)

**영향 범위**:
- `LevelTestData.java`: requiresToolbox 필드 타입 변경
- `LevelGrader.java`: invokeWithToolbox 메서드에 switch 분기 추가
- `level4.json`: `true` → `"set"` 변경

---

### 2. timeLimitMs 추가

**배경**: S-3 골드바흐 문제는 parallelStream 사용을 강제해야 함.

**구현**:
```java
// LevelTestData.ValidationConfig
@JsonProperty("timeLimitMs")
private Long timeLimitMs = null;
```

```java
// LevelGrader.runTestCases
if (timeLimitMs != null && elapsedMs > timeLimitMs) {
    System.out.println("  ⏱️ 시간 초과 (" + elapsedMs + "ms > " + timeLimitMs + "ms)");
    continue;
}
```

**대안 검토**:
- Validator에서 `parallelStream` 호출 강제 → **기각** (구문적 제약이 아닌 의미적 제약)
- 타임아웃으로 간접 강제 → **채택** (parallelStream 없으면 시간 초과)

---

### 3. 골드바흐 범위/시간 제한 조정

**검토 과정**:

| 범위 | 시간(parallel) | 시간(sequential) | 결정 |
|------|----------------|------------------|------|
| 1000만 | ~97초 | - | 너무 느림, 기각 |
| 100만 | ~3초 | - | 여전히 느림 |
| 10만 | ~900ms | ~3초+ | 적정 |

**최종 결정**: `maxN=100000`, `timeLimitMs=1500`

**근거**:
- parallel 사용 시 약 900ms → 여유 있게 통과
- sequential 사용 시 시간 초과 → parallelStream 강제
- 1.5초는 충분한 여유 마진 (환경 차이 고려)

---

### 4. 순열 정렬 요구사항 추가

**문제**: 순열 생성 순서가 구현에 따라 달라짐

```
입력: [1, 2]
구현 A: [[1, 2], [2, 1]]
구현 B: [[2, 1], [1, 2]]
```

**후보**:
1. 순서 무관하게 Set으로 비교 → 기각 (순열의 특성상 List가 자연스러움)
2. **문제에서 사전순 정렬 요구** → 채택

**추가 이슈**: 정렬 비교자에서 for문 사용 시 validator 위반

```java
// ❌ for문 사용 - validator 실패
.sorted((a, b) -> {
    for (int i = 0; i < a.size(); i++) {
        int cmp = a.get(i).compareTo(b.get(i));
        if (cmp != 0) return cmp;
    }
    return 0;
})

// ✅ IntStream 사용 - validator 통과
.sorted((a, b) -> IntStream.range(0, a.size())
    .map(i -> a.get(i).compareTo(b.get(i)))
    .filter(c -> c != 0)
    .findFirst()
    .orElse(0))
```

---

### 5. maxVariables 설정

| 문제 | maxVariables | 근거 |
|------|--------------|------|
| S-1 | 1 | 단일 스트림 체인으로 해결 가능 |
| S-2 | 1 | Stream.iterate 단일 체인 |
| S-3 | 5 | Collectors.groupingBy 결과를 다시 스트림 처리 |

S-3의 5개는 다소 너그러운 설정. 최적화된 코드는 2-3개로도 가능하나, 가독성을 위한 중간 변수 허용.

---

## Toolbox 설계 원칙

### 왜 Toolbox를 사용하는가?

1. **순수 스트림 제약 유지**: 직접 구현하면 for문/재귀 필요
2. **학습 목표 집중**: 복잡한 로직은 도구로 추상화
3. **import 방지**: 파라미터로만 전달하여 의도적 사용 유도

### 각 Toolbox 역할

| Toolbox | 제공 기능 | 숨기는 복잡성 |
|---------|----------|---------------|
| SetToolbox | 집합 확장 연산 | 불변 집합 생성 |
| PermutationToolbox | insertAll 연산 | 모든 위치 삽입 로직 |
| GoldbachToolbox | isPrime, primes | Sieve of Eratosthenes |

---

## 결론

Secret Phase는 기존 Level과 차별화된 도전을 제공하면서도:
- 일관된 검증 시스템 유지 (Validator + Grader)
- Toolbox 패턴으로 복잡성 추상화
- 시간 제한으로 성능 고려 유도

향후 문제 추가 시에도 이 패턴을 따르면 됨.
