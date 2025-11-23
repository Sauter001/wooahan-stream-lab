package domain.problem.game;

import lombok.Getter;

@Getter
public enum ItemType {
    WEAPON("WEAPON"), ARMOR("ARMOR"), POTION("POTION");

    private final String name;

    ItemType(String name) {
        this.name = name;
    }
}
