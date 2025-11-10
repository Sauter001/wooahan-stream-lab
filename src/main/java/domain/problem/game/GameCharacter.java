package domain.problem.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class GameCharacter {
    private Long id;
    private String name;
    private int level;
    private CharacterClass characterClass; // "Warrior", "Mage", "Archer" 등
    private Guild guild;
    private List<Item> inventory;
    private int gold;
    private boolean isActive;
}