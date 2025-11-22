package domain.problem.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Item {
    private final Long id;
    private final String name;
    private final Rarity rarity;
    private final int price;
    private final ItemType type;
    private final int power;
}