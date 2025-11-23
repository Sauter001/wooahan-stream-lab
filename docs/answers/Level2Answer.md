# Level 2 정답

## 2-1-1. getGuildMembersAboveLevel30
```java
public static List<String> getGuildMembersAboveLevel30(List<GameCharacter> characters) {
    return characters.stream()
            .filter(c -> c.getLevel() >= 30)
            .filter(c -> c.getGuild() != null)
            .map(GameCharacter::getName)
            .sorted()
            .collect(Collectors.toList());
}
```

## 2-1-2. getTop10AverageGold
```java
public static double getTop10AverageGold(List<GameCharacter> characters) {
    return characters.stream()
            .sorted(Comparator.comparingInt(GameCharacter::getLevel).reversed())
            .limit(10)
            .map(GameCharacter::getGold)
            .collect(Collectors.averagingInt(Integer::intValue));
}
```

## 2-1-3. getAllItemNamesJoined
```java
public static String getAllItemNamesJoined(List<GameCharacter> characters) {
    return characters.stream()
            .flatMap(c -> c.getInventory().stream())
            .map(Item::getName)
            .sorted()
            .distinct()
            .collect(Collectors.joining(","));
}
```

## 2-1-4. getSilverTrophyWinners
```java
public static List<String> getSilverTrophyWinners(List<GameCharacter> characters) {
    return characters.stream()
            .sorted(Comparator.comparingInt(GameCharacter::getLevel).reversed())
            .skip(3)
            .limit(7)
            .map(GameCharacter::getName)
            .toList();
}
```

## 2-1-5. hasRichHighLevelCharacter
```java
public static boolean hasRichHighLevelCharacter(List<GameCharacter> characters) {
    return characters.stream()
            .anyMatch(c -> c.getLevel() >= 50 && c.getGold() >= 5000);
}
```
