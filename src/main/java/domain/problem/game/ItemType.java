package domain.problem.game;

import lombok.Getter;

@Getter
public enum ItemType {
    WEAPON("Weapon"), ARMOR("Armor"), POTION("Potion");

    private final String name;

    ItemType(String name) {
        this.name = name;
    }
}
