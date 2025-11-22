package domain.problem.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Guild {
    private final Long id;
    private final String name;
    private final int level;
    private final String region;
}