# Level 4 정답

## 4-1-1. isPalindrome
```java
public static boolean isPalindrome(String str) {
    return IntStream.range(0, str.length() / 2)
            .allMatch(i -> str.charAt(i) == str.charAt(str.length() - i - 1));
}
```

## 4-1-2. getFirstCharacterNameByLevel
```java
public static List<String> getFirstCharacterNameByLevel(List<GameCharacter> characters) {
    return characters.stream()
            .collect(Collectors.toMap(
                    GameCharacter::getLevel,
                    GameCharacter::getName,
                    (exist, repl) -> exist,
                    TreeMap::new
            ))
            .values()
            .stream()
            .toList();
}
```

## 4-1-3. getTop3LevelSumByGuild
```java
public static Map<String, Integer> getTop3LevelSumByGuild(List<GameCharacter> characters) {
    return characters.stream()
            .filter(c -> c.getGuild() != null)
            .collect(Collectors.groupingBy(
                    c -> c.getGuild().getName(),
                    Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> list.stream()
                                    .sorted((a, b) -> Integer.compare(b.getLevel(), a.getLevel()))
                                    .limit(3)
                                    .mapToInt(GameCharacter::getLevel)
                                    .sum()
                    )
            ));
}
```

## 4-2-1. getBestSellerByMonth
```java
public static Map<Integer, String> getBestSellerByMonth(List<Order> orders) {
    return orders.stream()
            .collect(Collectors.groupingBy(
                    o -> o.getOrderDate().getMonthValue(),
                    Collectors.groupingBy(
                            Order::getProductName,
                            Collectors.summingInt(Order::getQuantity)
                    )
            ))
            .entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("")
            ));
}
```

## 4-2-2. getVipCustomers
```java
public static Set<String> getVipCustomers(List<Order> orders) {
    return orders.stream()
            .collect(Collectors.groupingBy(Order::getCustomerName))
            .entrySet().stream()
            .filter(e -> e.getValue().size() >= 2)
            .filter(e -> e.getValue().stream()
                    .mapToLong(o -> (long) o.getQuantity() * o.getPrice())
                    .sum() >= 2_000_000)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
}
```

## 4-3-1. powerSet
```java
public static Set<Set<Integer>> powerSet(Set<Integer> set, SetToolbox<Integer> tools) {
    return set.stream()
            .reduce(
                    tools.emptySeed(),
                    tools::extendAll,
                    tools::merge
            );
}
```
