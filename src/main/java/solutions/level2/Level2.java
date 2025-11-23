package solutions.level2;

import domain.problem.game.GameCharacter;

import java.util.List;

public class Level2 {
    /**
     * 2-1-1
     * <p>
     * 레벨 30 이상이면서 길드에 속한 캐릭터의 이름을 알파벳 순으로 반환
     *
     * @param characters 캐릭터 리스트
     */
    public static List<String> getGuildMembersAboveLevel30(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 2-1-2
     * <p>
     * 레벨이 가장 높은 상위 10명의 평균 gold를 Double로 반환
     *
     * @param characters 캐릭터 리스트
     */
    public static double getTop10AverageGold(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 2-1-3
     * <p>
     * 모든 캐릭터의 인벤토리에 있는 아이템 이름을 오름차순으로 정렬 후
     * 중복 없이 쉼표로 구분한 String 반환
     * 예시: 천갑옷,얼음창,강철갑옷
     *
     * @param characters 캐릭터 리스트
     * @return 쉼표(,)로 결합 된 문자열
     */
    public static String getAllItemNamesJoined(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 2-1-4
     * <p>
     * 레벨 기준 내림차순으로 4~10등에 해당하는 캐릭터 이름 목록을 반환하라.
     * (1~3등: 골드 트로피, 4~10등: 실버 트로피)
     *
     * @param characters 캐릭터 리스트
     * @return 실버 트로피를 받을 캐릭터의 이름
     */
    public static List<String> getSilverTrophyWinners(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현해주세요");
    }

    /**
     * 2-1-5
     * <p>
     * 레벨 50 이상이면서 gold가 5000 이상인 캐릭터가 존재하는지 판단하라.
     *
     * @param characters 캐릭터 리스트
     * @return <p>
     * `true` : 50레벨 이상에 5000 골드 이상 가진 캐릭터가 존재 <br>
     * `false` : 그러한 캐릭터가 존재하지 않는 경우
     * </p>
     */
    public static boolean hasRichHighLevelCharacter(List<GameCharacter> characters) {
        throw new UnsupportedOperationException("구현해주세요");
    }
}
