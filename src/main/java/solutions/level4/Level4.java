package solutions.level4;

import domain.problem.game.GameCharacter;
import domain.problem.product.Order;
import domain.tools.SetToolbox;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Level4 {
    /**
     * 4-1-1
     * <p>
     * 주어진 문자열이 앞뒤가 같은 팰린드롬인지 판별하세요.
     * <p>
     *     예를 들어 <code>comoc</code>은 팰린드롬이지만, <code>abc</code>는 팰린드롬이 아닙니다.
     * </p>
     * 힌트: IntStream.range() + allMatch() 또는 StringBuilder.reverse()
     *
     * @param str 판별할 문자열
     * @return 팰린드롬이면 true, 아니면 false
     */
    public static boolean isPalindrome(String str) {
        return IntStream.range(0, str.length() / 2)
                .allMatch(i -> str.charAt(i) == str.charAt(str.length() - i - 1));
    }

    /**
     * 4-1-2
     * <p>
     * 레벨 오름차순 정렬 후, 동일 레벨에서는 첫 번째 캐릭터만 남긴 이름 목록 반환
     * 힌트: sorted() + collect(Collectors.toMap()) with merge function + values()
     * 또는 TreeMap 활용
     *
     * @param characters 캐릭터 리스트
     * @return 레벨별 첫 번째 캐릭터의 이름 목록 (레벨 오름차순)
     */
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

    /**
     * 4-1-3
     * <p>
     * 길드별로 레벨이 가장 높은 상위 3명의 레벨 합계를 구하라 (길드 없는 캐릭터 제외)
     * 힌트: filter() + groupingBy() + collectingAndThen() + sorted().limit(3).sum()
     *
     * @param characters 캐릭터 리스트
     * @return 길드명을 key로, 상위 3명 레벨 합계를 value로 하는 Map
     */
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

    /**
     * 4-2-1
     * <p>
     * 각 월에서 가장 많이 팔린(quantity 합계 기준) 상품명을 반환
     * 힌트: groupingBy(월) + groupingBy(상품) + summingInt() + maxBy()
     *
     * @param orders 주문 리스트
     * @return 월(1, 2, 3...)을 key로, 베스트셀러 상품명을 value로 하는 Map
     */
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

    /**
     * 4-2-2
     * <p>
     * 총 구매액 2,000,000원 이상이면서 구매 횟수 2회 이상인 고객 이름 Set 반환
     * 총 구매액 = sum(quantity * price)
     * 힌트: groupingBy(고객) 후 각 조건별 필터링, 교집합 구하기
     *
     * @param orders 주문 리스트
     * @return VIP 고객 이름 Set
     */
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

    /**
     * 4-3-1
     * <p>
     * 주어진 정수 집합의 멱집합(모든 부분집합)을 반환
     * 힌트: Stream.reduce() + SetToolbox의 emptySeed(), extendAll(), merge() 활용
     *
     * @param set   원본 집합
     * @param tools 집합 연산 도구 (파라미터로 제공됨)
     * @return 모든 부분집합을 담은 Set
     * @see <a href="../../docs/tools/SetToolbox.md">SetToolbox 문서</a>
     */
    public static Set<Set<Integer>> powerSet(Set<Integer> set, SetToolbox<Integer> tools) {
        return set.stream()
                .reduce(
                        tools.emptySeed(),
                        tools::extendAll,
                        tools::merge
                );
    }
}
