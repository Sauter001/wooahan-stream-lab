# Level 3 정답

## 3-1-1. groupStudentNamesByGrade
```java
public static Map<Integer, List<String>> groupStudentNamesByGrade(List<Student> students) {
    return students.stream()
            .collect(
                    Collectors.groupingBy(
                            Student::getGrade,
                            Collectors.mapping(Student::getName, Collectors.toList())
                    )
            );
}
```

## 3-1-2. getAverageScoreBySubject
```java
public static Map<String, Double> getAverageScoreBySubject(List<Student> students) {
    return students.stream()
            .collect(
                    Collectors.groupingBy(s -> s.getSubject().getName(), Collectors.averagingInt(Student::getScore))
            );
}
```

## 3-1-3. countStudentsByGradeAndSubject
```java
public static Map<Integer, Map<String, Long>> countStudentsByGradeAndSubject(List<Student> students) {
    return students.stream()
            .collect(Collectors
                    .groupingBy(
                            Student::getGrade,
                            Collectors.groupingBy(s -> s.getSubject().getName(), Collectors.counting())
                    )
            );
}
```

## 3-1-4. partitionStudentNamesByScore80
```java
public static Map<Boolean, List<String>> partitionStudentNamesByScore80(List<Student> students) {
    return students.stream()
            .collect(Collectors.partitioningBy(
                    s -> s.getScore() >= 80,
                    Collectors.mapping(Student::getName, Collectors.toList())));
}
```

## 3-2-1. getMaxPriceByProductName
```java
public static Map<String, Integer> getMaxPriceByProductName(List<Product> products) {
    return products.stream()
            .collect(Collectors.toMap(Product::getName, Product::getPrice, Math::max));
}
```

## 3-2-2. getTotalStockByCategory
```java
public static Map<String, Integer> getTotalStockByCategory(List<Product> products) {
    return products.stream()
            .collect(Collectors.groupingBy(p -> p.getCategory().getType(), Collectors.summingInt(Product::getStock)));
}
```
