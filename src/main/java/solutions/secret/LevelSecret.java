package solutions.secret;

import domain.problem.secret.Pair;
import domain.tools.GoldbachToolbox;
import domain.tools.PermutationToolbox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LevelSecret {

    /**
     * S-1: 순열 생성
     * <p>
     * 주어진 정수 리스트의 모든 순열을 사전순으로 정렬하여 반환합니다.
     * <p>
     * 예: [1, 2, 3] → [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
     * <p>
     * 힌트: reduce() + flatMap() + tools.insertAll() + sorted()
     *
     * @param elements 원본 리스트
     * @param tools    순열 생성 도구
     * @return 사전순 정렬된 모든 순열의 리스트
     */
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

    /**
     * S-2: 피보나치 수열
     * <p>
     * 무한 스트림을 활용하여 max 이하의 피보나치 수열을 반환합니다.
     * <p>
     * 예: max=1000 → [1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987]
     * <p>
     * 힌트: Stream.iterate() + Pair + takeWhile()
     *
     * @param max 최대값
     * @return max 이하의 피보나치 수열
     */
    public static List<Integer> fibonacciUpTo(int max) {
        return Stream.iterate(
                        new Pair<>(1, 1), p -> p.first() <= max,
                        p -> new Pair<>(p.second(), p.first() + p.second())
                )
                .map(Pair::first)
                .toList();
    }

    /**
     * S-3: 골드바흐 추측 검증
     * <p>
     * 4부터 maxN까지의 모든 짝수에 대해 골드바흐 쌍(두 소수의 합)을 찾고,
     * 각 소수가 골드바흐 쌍에 등장한 횟수를 계산하여,
     * 가장 많이 사용된 상위 100개 소수의 합을 반환합니다.
     * <p>
     * 제약: 1.5초 이내, parallelStream 필수
     * <p>
     * 힌트: parallelStream + flatMap + groupingBy(counting) + sorted + limit
     *
     * @param maxN  검증할 최대 짝수 (최대 100,000개)
     * @param tools 골드바흐 도구 (소수 판별)
     * @return 상위 100개 소수의 합
     */
    public static long sumTop100GoldbachPrimes(int maxN, GoldbachToolbox tools) {
        return java.util.stream.IntStream.iterate(4, n -> n <= maxN, n -> n + 2)
                .parallel()
                .flatMap(n -> java.util.stream.IntStream.rangeClosed(2, n / 2)
                        .filter(p -> tools.isPrime(p) && tools.isPrime(n - p)))
                .boxed()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(100)
                .mapToLong(Map.Entry::getKey)
                .sum();
    }
}
