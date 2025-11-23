package solutions.level5;

import domain.problem.game.GameCharacter;
import domain.problem.game.Item;
import domain.problem.game.Rarity;
import domain.problem.secret.Pair;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Level5 {

    /**
     * 5-1-1
     * <p>
     * Rare, Epic, Legendary 등급 아이템을 3개 이상 보유한 캐릭터를 찾아,
     * 소속 길드의 레벨 내림차순으로 정렬하여 이름 목록을 반환하세요.
     * 길드가 없는 캐릭터는 제외합니다.
     * 같은 길드 레벨 내에서는 캐릭터 ID 오름차순으로 정렬합니다.
     * <p>
     * 힌트: filter() + 중첩 filter() + sorted(Comparator.comparing().thenComparing()) + map()
     *
     * @param characters 캐릭터 리스트
     * @return 조건을 만족하는 캐릭터 이름 목록 (길드 레벨 내림차순, 같은 레벨 내 ID 오름차순)
     */
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

    /**
     * 5-1-2
     * <p>
     * 정수 리스트를 3개씩 묶어 List&lt;List&lt;Integer&gt;&gt;로 반환하세요.
     * 마지막 그룹은 3개 미만일 수 있습니다.
     * <p>
     * 예: [1, 2, 3, 4, 5, 6, 7] → [[1, 2, 3], [4, 5, 6], [7]]
     * <p>
     * 힌트: Collector.of()를 사용하여 Custom Collector를 구현하세요.
     *
     * @param numbers 정수 리스트
     * @return 3개씩 묶인 리스트
     */
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

    /**
     * 5-2-1
     * <p>
     * 모든 캐릭터의 인벤토리에서 아이템을 추출하여 타입별로 그룹화한 뒤,
     * 각 타입에서 power가 가장 높은 아이템을 찾고,
     * 그 power 기준 상위 5개 타입의 아이템을 Map으로 반환하세요.
     * Power가 같으면 Rarity가 높은 아이템을 선택합니다. (LEGENDARY > EPIC > RARE > COMMON)
     * <p>
     * 힌트: flatMap() → distinct() → groupingBy() + maxBy(thenComparing) → Map 스트림 → sorted() → limit(5)
     *
     * @param characters 캐릭터 리스트
     * @return 타입(key)과 아이템 이름(value)의 Map (상위 5개 타입)
     */
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

    /**
     * 5-2-2
     * <p>
     * 정수 리스트에서 연속된 부분합의 최댓값을 구하세요. (Kadane's Algorithm)
     * <p>
     * 변수 정의 <b>2개 이하</b> 사용 가능
     * <p>
     * 예: [-2, 1, -3, 4, -1, 2, 1, -5, 4] → 6 (부분합 [4, -1, 2, 1])
     * <p>
     * 힌트: reduce()와 Pair를 활용하여 (현재 최대합, 전체 최대합) 상태를 관리
     *
     * @param numbers 정수 리스트
     * @return 연속 부분합의 최댓값
     */
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

    /**
     * 5-3-1
     * <p>
     * 문자열에서 연속된 문자를 '문자+개수' 형태로 압축하세요.
     * <p>
     * 예: "aaabbbcc" → "a3b3c2"
     * <p>
     * 힌트:
     * <ul>
     *   <li>[방법1] Collector.of()로 상태 관리</li>
     *   <li>[방법2] reduce()와 Pair&lt;결과, Pair&lt;현재문자, 카운트&gt;&gt; 활용 (종료 문자 트릭 권장)</li>
     * </ul>
     *
     * @param str 압축할 문자열
     * @return 압축된 문자열
     */
    public static String compress(String str) {
        return (str + "\0").chars()
                .mapToObj(c -> (char) c)
                .reduce(
                        new Pair<>("", new Pair<>('\0', 0)),
                        (acc, c) -> {
                            var cur = acc.second();
                            if (cur.first().equals(c)) {
                                return new Pair<>(acc.first(), new Pair<>(c, cur.second() +
                                        1));
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

    // ===================================================
    // 시스템 유지보수용 - 건드리지 마세요
    // ===================================================
    public static List<Integer> doNotTouch() {
        return List.of(1,1,2,3,5,8,13);
    }
}
