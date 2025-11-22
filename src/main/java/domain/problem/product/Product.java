package domain.problem.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Product {
    private final String name;
    private final ProductCategory category;  // Electronics, Food, Clothing
    private final int price;
    private final int stock;
}
