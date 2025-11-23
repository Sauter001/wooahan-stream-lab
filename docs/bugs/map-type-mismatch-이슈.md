# Map 비교 시 타입 불일치 버그

**날짜**: 2025-11-23

---

## 1. 문제 인식

### 증상 1: 중첩 Map의 숫자 타입 불일치
- Level3 문제 3-1-3 (`countStudentsByGradeAndSubject`)이 올바른 구현에도 테스트 실패
- 반환 타입: `Map<Integer, Map<String, Long>>`
- 코드 로직은 정확하나 채점 시스템에서 불일치 판정

```java
public static Map<Integer, Map<String, Long>> countStudentsByGradeAndSubject(List<Student> students) {
    return students.stream()
            .collect(Collectors.groupingBy(
                    Student::getGrade,
                    Collectors.groupingBy(s -> s.getSubject().getName(), Collectors.counting())
            ));
}
```

### 증상 2: Boolean 키 타입 불일치
- Level3 문제 3-1-4 (`partitionStudentNamesByScore80`)도 동일하게 실패
- 반환 타입: `Map<Boolean, List<String>>`

```java
public static Map<Boolean, List<String>> partitionStudentNamesByScore80(List<Student> students) {
    return students.stream()
            .collect(Collectors.partitioningBy(
                    s -> s.getScore() >= 80,
                    Collectors.mapping(Student::getName, Collectors.toList())));
}
```

---

## 2. 원인 발견

### 원인 1: 숫자 타입 불일치 (Integer vs Long)

**JSON 테스트 데이터 (expected):**
```json
{
  "1": {"Math": 1, "English": 1, "Science": 2},
  "2": {"Math": 2, "Science": 1},
  "3": {"Math": 2, "English": 1}
}
```

**파싱 후 타입:**
- 외부 Map 키: `String` → `Integer`로 변환 (convertExpected에서 처리)
- 내부 Map 값: `Integer` (JSON 기본 숫자 타입)

**실제 반환 타입:**
- 외부 Map 키: `Integer`
- 내부 Map 값: `Long` (`Collectors.counting()` 반환 타입)

`Integer(1).equals(Long(1L))` → `false`

### 원인 2: Boolean 키 타입 불일치 (String vs Boolean)

**JSON 테스트 데이터 (expected):**
```json
{
  "true": ["김철수", "이영희", ...],
  "false": ["박민수", "강호진", ...]
}
```

**파싱 후 타입:**
- Map 키: `String` ("true", "false")

**실제 반환 타입:**
- Map 키: `Boolean` (`partitioningBy()` 반환 타입)

`"true".equals(Boolean.TRUE)` → `false`

### 공통 원인
```java
// LevelGrader.compareResults()
if (expected instanceof Map && actual instanceof Map) {
    return expected.equals(actual);  // 단순 equals 비교
}
```

Java의 `equals()`는 타입이 다르면 false를 반환하므로, 값이 같아도 실패.

---

## 3. 해결 과정

### 해결 전략
1. Map 비교 시 재귀적 깊은 비교 구현
2. 숫자 타입 간 비교 시 값 기반 비교

### 수정 내용

**LevelGrader.java** - `compareMaps()` 메서드 추가:
```java
private boolean compareMaps(Map<?, ?> expected, Map<?, ?> actual) {
    if (expected.size() != actual.size()) return false;

    for (Map.Entry<?, ?> entry : expected.entrySet()) {
        Object expectedKey = entry.getKey();
        Object expectedValue = entry.getValue();

        // actual에서 매칭되는 키 찾기 (숫자 타입 차이 허용)
        Object actualValue = null;
        boolean keyFound = false;

        for (Map.Entry<?, ?> actualEntry : actual.entrySet()) {
            if (keysMatch(expectedKey, actualEntry.getKey())) {
                actualValue = actualEntry.getValue();
                keyFound = true;
                break;
            }
        }

        if (!keyFound) return false;
        if (!compareResults(expectedValue, actualValue)) return false;  // 재귀 비교
    }

    return true;
}

private boolean keysMatch(Object key1, Object key2) {
    if (key1 == null && key2 == null) return true;
    if (key1 == null || key2 == null) return false;

    // 숫자 키 비교 (Integer, Long 등)
    if (key1 instanceof Number && key2 instanceof Number) {
        return ((Number) key1).longValue() == ((Number) key2).longValue();
    }

    // Boolean 키 비교 (String "true"/"false" vs Boolean)
    if (key1 instanceof String && key2 instanceof Boolean) {
        return Boolean.parseBoolean((String) key1) == (Boolean) key2;
    }
    if (key1 instanceof Boolean && key2 instanceof String) {
        return (Boolean) key1 == Boolean.parseBoolean((String) key2);
    }

    return key1.equals(key2);
}
```

**compareResults()의 Map 분기 수정:**
```java
// 변경 전
if (expected instanceof Map && actual instanceof Map) {
    return expected.equals(actual);
}

// 변경 후
if (expected instanceof Map && actual instanceof Map) {
    return compareMaps((Map<?, ?>) expected, (Map<?, ?>) actual);
}
```

### 비교 흐름

**케이스 1: 중첩 Map (Integer/Long)**
```
compareMaps({1: {Math: 1}}, {1: {Math: 1L}})
    ↓
keysMatch(1, 1) → true (둘 다 Number, longValue 비교)
    ↓
compareResults({Math: 1}, {Math: 1L}) → compareMaps 재귀 호출
    ↓
keysMatch("Math", "Math") → true (String.equals)
    ↓
compareResults(1, 1L) → true (Number 비교, doubleValue 사용)
```

**케이스 2: Boolean 키 Map**
```
compareMaps({"true": [...]}, {true: [...]})
    ↓
keysMatch("true", true) → true (String→Boolean 파싱 후 비교)
    ↓
compareResults([...], [...]) → List 비교
```

---

## 4. 도전 측면에서 깨달은 점

### Java 타입 시스템의 엄격함
- `Integer`와 `Long`은 둘 다 숫자지만 `equals()`는 타입까지 비교
- `String`과 `Boolean`도 마찬가지로 `"true".equals(true)` → `false`
- JSON 파싱 라이브러리는 보통 정수를 `Integer`로, boolean 키를 `String`으로 변환
- `Collectors.counting()`은 `Long`, `partitioningBy()`는 `Boolean` 키 반환

### 테스트 데이터와 실제 코드의 간극
- 테스트 데이터(JSON)와 실제 Java 코드 사이에는 타입 변환 레이어가 존재
- 특히 제네릭 타입(`Map<K, V>`)은 런타임에 타입 정보가 소거되어 주의 필요

### 재귀적 비교의 필요성
- 중첩 자료구조(Map of Map, List of Map 등)는 단순 `equals()`로 비교 불가
- 각 레벨에서 타입 정규화가 필요할 수 있음

### 방어적 비교 설계
- 숫자 비교 시 `Number` 인터페이스 활용하여 타입 무관하게 비교
- Boolean 비교 시 `Boolean.parseBoolean()` 활용하여 String↔Boolean 변환
- 키 비교와 값 비교를 분리하여 각각 적절한 전략 적용
- 새로운 타입 불일치 발견 시 `keysMatch()`에 케이스 추가로 확장 가능
