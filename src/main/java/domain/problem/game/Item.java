package domain.problem.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class Item {
    private Long id;
    private String name;
    private Rarity rarity; // "Common", "Rare", "Epic", "Legendary"
    private int price;
    private String type; // "Weapon", "Armor", "Potion" 등
    private int power;
}