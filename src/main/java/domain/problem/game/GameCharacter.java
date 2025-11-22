package domain.problem.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class GameCharacter {
    private final Long id;
    private final String name;
    private final int level;
    private final CharacterClass characterClass;
    private final Guild guild;
    private final List<Item> inventory;
    private final int gold;
    private final boolean active;
}