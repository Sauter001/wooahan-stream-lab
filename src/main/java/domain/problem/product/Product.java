package domain.problem.product;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
public class Product {
    private final String name;
    private final ProductCategory category;  // Electronics, Food, Clothing
    private final int price;
    private final int stock;
}
