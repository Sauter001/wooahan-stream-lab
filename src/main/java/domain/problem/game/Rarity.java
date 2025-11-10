package domain.problem.game;

import lombok.Getter;

@Getter
public enum Rarity {
    LEGENDARY("Legendary"), EPIC("Epic"), RARE("Rare"), COMMON("Common");

    private final String name;

    Rarity(String name) {
        this.name = name;
    }
}
