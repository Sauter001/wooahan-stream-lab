# SetToolbox - 집합 연산 도구 모음

- Level4 이상 문제에서 복잡한 집합 연산(멱집합 등)을 위해 제공되는 도구

## 사용 방법

**중요**: `SetToolbox`는 직접 import하여 사용할 수 없다. 메서드 파라미터로 전달받아 사용해야 한다.

```java
// ❌ 잘못된 사용 - import 불가
import domain.tools.SetToolbox;
import domain.tools.SetToolboxImpl;

// ✅ 올바른 사용 - 파라미터로 전달받음
public static <T> Set<Set<T>> powerSet(Set<T> set, SetToolbox<T> tools) {
    return set.stream()
        .reduce(tools.emptySeed(), tools::extendAll, tools::merge);
}
```

---

## 메서드 목록

### 기본 집합 연산

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `add(set, element)` | 원소 추가한 새 Set 반환 | `add({a}, b)` → `{a, b}` |
| `remove(set, element)` | 원소 제거한 새 Set 반환 | `remove({a, b}, a)` → `{b}` |
| `union(a, b)` | 합집합 | `union({a}, {b})` → `{a, b}` |
| `intersection(a, b)` | 교집합 | `intersection({a, b}, {b, c})` → `{b}` |
| `difference(a, b)` | 차집합 (a - b) | `difference({a, b}, {b})` → `{a}` |

### 멱집합 특화 연산

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `emptySeed()` | 빈 집합만 포함한 Set (초기값) | `{{}}` |
| `extendAll(subsets, element)` | 모든 부분집합에 원소 추가 후 합침 | `extendAll({{}, {a}}, b)` → `{{}, {a}, {b}, {a,b}}` |
| `branch(set, element)` | 원본 + 원소추가 버전을 Stream으로 | `branch({a}, b)` → `Stream.of({a}, {a,b})` |

### 병합/누적 연산

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `merge(a, b)` | 두 Set<Set<T>> 병합 | reduce의 combiner로 활용 |
| `fold(set, identity, accumulator)` | 집합 원소 순회하며 누적 | 커스텀 누적 연산 |

### 유틸리티

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `of(elements...)` | 가변 인자로 Set 생성 | `of(1, 2, 3)` → `{1, 2, 3}` |
| `empty()` | 빈 Set 생성 | `{}` |

---

## 활용 예시

### 멱집합 구현

```java
public static <T> Set<Set<T>> powerSet(Set<T> set, SetToolbox<T> tools) {
    return set.stream()
        .reduce(
            tools.emptySeed(),           // 초기값: {{}}
            tools::extendAll,            // 누적: 원소마다 확장
            tools::merge                 // 병합: 병렬 처리 시
        );
}
```

**동작 과정** (set = {a, b}):
1. 초기: `{{}}`
2. 'a' 처리: `{{}, {a}}`
3. 'b' 처리: `{{}, {a}, {b}, {a,b}}`

### 조합(Combination) 구현

```java
public static <T> Set<Set<T>> combinations(Set<T> set, int k, SetToolbox<T> tools) {
    return powerSet(set, tools).stream()
        .filter(subset -> subset.size() == k)
        .collect(Collectors.toSet());
}
```

---

## 주의사항

1. **불변성**: 모든 연산은 원본을 변경하지 않고 새 Set을 반환한다.
2. **import 금지**: `domain.tools` 패키지 import 시 검증 실패.
3. **파라미터 전용**: 문제의 메서드 시그니처에 포함된 `SetToolbox` 파라미터만 사용할 것.
