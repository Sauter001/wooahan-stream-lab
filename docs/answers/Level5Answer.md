# Level 5 정답

## 5-1-1. getRareItemCollectors
```java
public static List<String> getRareItemCollectors(List<GameCharacter> characters) {
    return characters.stream()
            .filter(c -> c.getGuild() != null)
            .filter(c -> c.getInventory().stream()
                    .filter(i -> i.getRarity() != Rarity.COMMON)
                    .count() >= 3)
            .sorted(Comparator.comparingInt((GameCharacter c) -> c.getGuild().getLevel()).reversed()
                    .thenComparingLong(GameCharacter::getId))
            .map(GameCharacter::getName)
            .toList();
}
```

## 5-1-2. groupByThree
```java
public static List<List<Integer>> groupByThree(List<Integer> numbers) {
    return numbers.stream()
            .collect(Collector.of(
                    ArrayList::new,
                    (acc, n) -> {
                        if (acc.isEmpty() || acc.getLast().size() >= 3) {
                            acc.add(new ArrayList<>());
                        }
                        acc.getLast().add(n);
                    },
                    (a, b) -> {
                        a.addAll(b);
                        return a;
                    }
            ));
}
```

## 5-2-1. getTop5PowerItemsByType
```java
public static Map<String, String> getTop5PowerItemsByType(List<GameCharacter> characters) {
    return characters.stream()
            .flatMap(c -> c.getInventory().stream())
            .distinct()
            .collect(Collectors.groupingBy(
                    i -> i.getType().name(),
                    Collectors.maxBy(Comparator.comparingInt(Item::getPower)
                            .thenComparing(Item::getRarity, Comparator.reverseOrder()))
            ))
            .entrySet().stream()
            .flatMap(e -> e.getValue().stream()
                    .map(i -> Map.entry(e.getKey(), i)))
            .sorted(Map.Entry.comparingByValue(
                    Comparator.comparingInt(Item::getPower).reversed()
            ))
            .limit(5)
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().getName(),
                    (a, b) -> a,
                    LinkedHashMap::new
            ));
}
```

## 5-2-2. getMaximumSubarraySum
```java
public static int getMaximumSubarraySum(List<Integer> numbers) {
    return numbers.stream()
            .reduce(
                    new Pair<>(0, Integer.MIN_VALUE),
                    (acc, n) -> {
                        int curMax = Math.max(n, acc.first() + n);
                        int globalMax = Math.max(acc.second(), curMax);
                        return new Pair<>(curMax, globalMax);
                    },
                    (a, b) -> b
            ).second();
}
```

## 5-3-1. compress
```java
public static String compress(String str) {
    return (str + "\0").chars()
            .mapToObj(c -> (char) c)
            .reduce(
                    new Pair<>("", new Pair<>('\0', 0)),
                    (acc, c) -> {
                        var cur = acc.second();
                        if (cur.first().equals(c)) {
                            return new Pair<>(acc.first(), new Pair<>(c, cur.second() + 1));
                        } else {
                            String result = cur.second() > 0
                                    ? acc.first() + cur.first() + cur.second()
                                    : acc.first();
                            return new Pair<>(result, new Pair<>(c, 1));
                        }
                    },
                    (a, b) -> b
            )
            .first();
}
```
