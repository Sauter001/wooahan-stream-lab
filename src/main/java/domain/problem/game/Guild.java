package domain.problem.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class Guild {
    private Long id;
    private String name;
    private int level;
    private String region;
}