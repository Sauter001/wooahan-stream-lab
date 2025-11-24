# PermutationToolbox - 순열 생성 도구 모음

- Secret Phase 문제에서 순열(Permutation) 생성을 위해 제공되는 도구.

## 사용 방법

**중요**: `PermutationToolbox`는 직접 import하여 사용할 수 없다. 메서드 파라미터로 전달받아 사용해야 한다.

```java
// ❌ 잘못된 사용 - import 불가
import domain.tools.PermutationToolbox;
import domain.tools.PermutationToolboxImpl;

// ✅ 올바른 사용 - 파라미터로 전달받음
public static List<List<Integer>> permutations(List<Integer> elements, PermutationToolbox<Integer> tools) {
    return elements.stream()
        .reduce(tools.emptySeed(),
                (perms, e) -> perms.stream()
                    .flatMap(p -> tools.insertAll(p, e))
                    .toList(),
                tools::merge);
}
```

---

## 메서드 목록

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `emptySeed()` | 빈 리스트만 포함한 List (초기값) | `[[]]` |
| `insertAll(list, element)` | 모든 위치에 원소 삽입한 Stream 반환 | `insertAll([2,3], 1)` → `Stream.of([1,2,3], [2,1,3], [2,3,1])` |
| `merge(a, b)` | 두 `List<List<T>>` 병합 | reduce의 combiner로 활용 |

---

## 핵심 메서드 상세

### emptySeed()

순열 생성의 시작점이다. 빈 리스트 하나를 담은 List를 반환한다.

```java
tools.emptySeed()  // → [[]]
```

**용도**: `reduce`의 identity로 사용

---

### insertAll(list, element)

핵심 연산이다. 주어진 리스트의 **모든 위치**에 원소를 삽입한 결과를 Stream으로 반환한다.

```java
tools.insertAll(List.of(2, 3), 1)
// → Stream.of([1, 2, 3], [2, 1, 3], [2, 3, 1])
```

**동작 원리**:
- 리스트 길이가 n이면 n+1개의 위치가 존재
- 각 위치에 원소를 삽입한 새 리스트 생성
- 위치 0: 맨 앞에 삽입
- 위치 n: 맨 뒤에 삽입

---

### merge(a, b)

두 `List<List<T>>`를 하나로 합친다.

```java
tools.merge(
    List.of(List.of(1, 2), List.of(2, 1)),
    List.of(List.of(3, 4), List.of(4, 3))
)
// → [[1,2], [2,1], [3,4], [4,3]]
```

**용도**: `reduce`의 combiner로 사용 (병렬 처리 시 부분 결과 병합)

---

## 활용 예시

### 순열 생성 (기본)

```java
public static List<List<Integer>> permutations(List<Integer> elements, PermutationToolbox<Integer> tools) {
    return elements.stream()
        .reduce(
            tools.emptySeed(),          // 초기값: [[]]
            (perms, e) -> perms.stream()
                .flatMap(p -> tools.insertAll(p, e))
                .toList(),               // 각 원소를 모든 위치에 삽입
            tools::merge                 // 병합
        );
}
```

**동작 과정** (elements = [1, 2, 3]):

1. 초기: `[[]]`
2. 원소 1 처리: `[[1]]`
3. 원소 2 처리:
   - `insertAll([1], 2)` → `[[2,1], [1,2]]`
4. 원소 3 처리:
   - `insertAll([2,1], 3)` → `[[3,2,1], [2,3,1], [2,1,3]]`
   - `insertAll([1,2], 3)` → `[[3,1,2], [1,3,2], [1,2,3]]`
5. 결과: `[[3,2,1], [2,3,1], [2,1,3], [3,1,2], [1,3,2], [1,2,3]]`

### 사전순 정렬된 순열

문제에서 사전순 정렬을 요구하는 경우:

```java
public static List<List<Integer>> permutations(List<Integer> elements, PermutationToolbox<Integer> tools) {
    return elements.stream()
        .reduce(
            tools.emptySeed(),
            (perms, e) -> perms.stream()
                .flatMap(p -> tools.insertAll(p, e))
                .toList(),
            tools::merge
        ).stream()
        .sorted((a, b) -> IntStream.range(0, a.size())
            .map(i -> a.get(i).compareTo(b.get(i)))
            .filter(c -> c != 0)
            .findFirst()
            .orElse(0))
        .toList();
}
```

---

## 주의사항

1. **불변성**: 모든 연산은 원본을 변경하지 않고 새 List를 반환한다.
2. **import 금지**: `domain.tools` 패키지 import 시 검증 실패함.
3. **파라미터 전용**: 문제의 메서드 시그니처에 포함된 `PermutationToolbox` 파라미터만 사용할 것.
4. **정렬 순서**: 기본 순열 생성은 순서를 보장하지 않는다. 사전순이 필요하면 별도 정렬이 필요하다.
