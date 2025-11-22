package domain.problem.game;

import lombok.Getter;

@Getter
public enum CharacterClass {
    ARCHER("Archer"),
    ASSASSIN("Assassin"),
    HEALER("Healer"),
    MAGE("Mage"),
    WARRIOR("Warrior");

    private final String name;

    CharacterClass(String name) {
        this.name = name;
    }
}
