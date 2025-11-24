# Secret Phase 정답

## S-1. permutations (순열 생성)
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

## S-2. fibonacciUpTo (피보나치 수열)
```java
public static List<Integer> fibonacciUpTo(int max) {
    return Stream.iterate(
                    new Pair<>(1, 1), p -> p.first() <= max,
                    p -> new Pair<>(p.second(), p.first() + p.second())
            )
            .map(Pair::first)
            .toList();
}
```

## S-3. sumTop100GoldbachPrimes (골드바흐 추측 검증)
```java
public static long sumTop100GoldbachPrimes(int maxN, GoldbachToolbox tools) {
    return IntStream.iterate(4, n -> n <= maxN, n -> n + 2)
            .parallel()
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
