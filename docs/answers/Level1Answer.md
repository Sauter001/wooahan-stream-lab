# Level 1 정답지

## 1-1-1: filterHighScoreStudents
80점 이상인 학생들의 이름만 리스트로 반환

```java
public static List<String> filterHighScoreStudents(List<Student> students) {
    return students.stream()
            .filter(student -> student.getScore() >= 80)
            .map(Student::getName)
            .toList();
}
```

## 1-1-2: getGrade2StudentNames
2학년 학생들의 이름만 Set으로 반환

```java
public static Set<String> getGrade2StudentNames(List<Student> students) {
    return students.stream()
            .filter(s -> s.getGrade() == 2)
            .map(Student::getName)
            .collect(Collectors.toSet());
}
```

## 1-1-3: filterHighScoreFemaleStudents
여학생(F)들 중 90점 이상인 학생의 이름 리스트

```java
public static List<String> filterHighScoreFemaleStudents(List<Student> students) {
    return students.stream()
            .filter(s -> s.getGender() == Gender.FEMALE)
            .filter(s -> s.getScore() >= 90)
            .map(Student::getName)
            .toList();
}
```

## 1-2-1: getUpperCaseProductNames
모든 상품 이름을 대문자로 변환한 리스트

```java
public static List<String> getUpperCaseProductNames(List<Product> products) {
    return products.stream()
            .map(p -> p.getName().toUpperCase())
            .toList();
}
```

## 1-2-2: getCheapProductCategories
가격이 50000원 이하인 상품들의 카테고리만 Set으로 반환

```java
public static Set<String> getCheapProductCategories(List<Product> products) {
    return products.stream()
            .filter(p -> p.getPrice() <= 50000)
            .map(p -> p.getCategory().getType())
            .collect(Collectors.toSet());
}
```

## 1-2-3: getElectronicsStocks
Electronics 카테고리 상품들의 재고 수량 리스트

```java
public static List<Integer> getElectronicsStocks(List<Product> products) {
    return products.stream()
            .filter(p -> p.getCategory() == ProductCategory.ELECTRONICS)
            .map(Product::getStock)
            .toList();
}
```

## 1-3-1: getActiveCharacterNames
isActive가 true인 캐릭터의 이름을 최대 5개만 반환

```java
public static List<String> getActiveCharacterNames(List<GameCharacter> characters) {
    return characters.stream()
            .filter(GameCharacter::isActive)
            .limit(5)
            .map(GameCharacter::getName)
            .toList();
}
```

## 1-3-2: sortByLevelDesc
모든 캐릭터를 레벨 내림차순으로 정렬하여 이름 반환

```java
public static List<String> sortByLevelDesc(List<GameCharacter> characters) {
    return characters.stream()
            .sorted(Comparator.comparing(GameCharacter::getLevel).reversed())
            .map(GameCharacter::getName)
            .toList();
}
```

## 1-3-3: getDistinctGuildNames
모든 캐릭터가 속한 길드 이름을 중복 없이 반환

```java
public static List<String> getDistinctGuildNames(List<GameCharacter> characters) {
    return characters.stream()
            .map(GameCharacter::getGuild)
            .filter(Objects::nonNull)
            .distinct()
            .map(Guild::getName)
            .toList();
}
```
