package domain.problem.game;

import lombok.Getter;

@Getter
public enum CharacterClass {
    WARRIOR("Warrior"), MAGE("Mage"), ARCHER("Archer");

    private final String name;

    CharacterClass(String name) {
        this.name = name;
    }
}
