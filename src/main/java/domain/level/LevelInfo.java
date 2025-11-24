package domain.level;

import java.util.List;

public record LevelInfo(
        int level,
        String title,
        String description,
        List<String> learningObjectives,
        String dialogue
) {
    public static LevelInfo ofLevel(int level) {
        return switch (level) {
            case 1 -> new LevelInfo(
                    1,
                    "Level 1 - 기본 연산",
                    "filter, map, collect의 기초를 배웁니다",
                    List.of(
                            "filter()로 조건에 맞는 요소 걸러내기",
                            "map()으로 요소 변환하기",
                            "collect()로 결과 수집하기",
                            "sorted(), limit(), distinct() 활용"
                    ),
                    """
                    좋아요! 이제 본격적으로 Stream을 배워봅시다.

                    Level 1에서는 Stream의 가장 기본적인 연산들을 다룹니다.

                    filter()는 조건에 맞는 요소만 걸러내고,
                    map()은 각 요소를 다른 형태로 변환하며,
                    collect()는 결과를 원하는 자료구조로 모아줍니다.

                    준비되셨나요? 시작해봅시다! 아, 풀다보면 무슨 코드가 나오는데...
                    
                    ...아니에요 신경 쓰지마세요
                    
                    """
            );
            case 2 -> new LevelInfo(
                    2,
                    "Level 2 - 중급 메서드",
                    "flatMap, skip, 복잡한 filter를 배웁니다",
                    List.of(
                            "flatMap()으로 중첩 구조 평탄화",
                            "skip()과 limit() 조합으로 페이징",
                            "여러 조건을 조합한 복잡한 필터링"
                    ),
                    """
                    Level 1을 훌륭하게 통과하셨네요!

                    이제 조금 더 복잡한 연산을 배워볼까요?

                    flatMap()은 중첩된 구조를 평탄화할 때 사용하고,
                    skip()과 limit()를 조합하면 페이징 처리가 가능합니다.
                    
                    """
            );
            case 3 -> new LevelInfo(
                    3,
                    "Level 3 - Collectors 심화",
                    "고급 Collector 활용법을 배웁니다",
                    List.of(
                            "중첩 groupingBy()로 다중 그룹화",
                            "partitioningBy()로 이분할",
                            "toMap()의 mergeFunction 활용",
                            "mapping(), reducing() 컬렉터 조합"
                    ),
                    """
                    벌써 Level 3까지 오셨군요! 대단해요!

                    이번 레벨에서는 Collectors를 깊이 있게 다룹니다.

                    groupingBy()를 중첩하면 다차원 그룹화가 가능하고,
                    toMap()에 mergeFunction을 제공하면 키 충돌도 처리할 수 있어요.

                    이 기능들을 익히면 복잡한 데이터 처리도 한 줄로!
                    """
            );
            case 4 -> new LevelInfo(
                    4,
                    "Level 4 - 고급 문제",
                    "복합적인 Stream 활용과 실전 문제를 풉니다",
                    List.of(
                            "여러 중간 연산의 효율적 조합",
                            "커스텀 Comparator 작성",
                            "복잡한 비즈니스 로직의 Stream 변환",
                            "실전형 데이터 처리 문제"
                    ),
                    """
                    Level 4에 도전하시는군요! 진정한 Stream 마스터의 길!

                    여기서는 지금까지 배운 모든 것을 종합적으로 활용합니다.

                    이제부터는 복잡한 데이터 처리 문제들이에요.
                    반복문으로 작성하면 수십 줄이 될 코드를
                    Stream으로 우아하게 해결해보세요!
                    """
            );
            case 5 -> new LevelInfo(
                    5,
                    "Level 5 - Expert Challenges",
                    "전문가 수준의 도전 과제입니다",
                    List.of(
                            "커스텀 Collector 구현",
                            "함수형 프로그래밍 패턴 적용",
                            "알고리즘 문제의 Stream 풀이"
                    ),
                    """
                    마지막 Level 5입니다! 여기까지 온 것만으로도 대단해요!

                    이제 진정한 전문가 수준의 문제들이 기다리고 있습니다.

                    커스텀 Collector를 직접 구현하고,
                    데카르트 곱, 연속 부분합 등 알고리즘 문제도 Stream으로 풀어봅니다.

                    이 레벨을 통과하면 당신은 진정한 Stream Master!
                    """
            );
            default -> new LevelInfo(
                    level,
                    "Level " + level,
                    "Unknown level",
                    List.of(),
                    "알 수 없는 레벨입니다."
            );
        };
    }
}
