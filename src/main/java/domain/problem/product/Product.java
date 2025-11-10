package domain.problem.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class Product {
    private String name;
    private String category;  // Electronics, Food, Clothing
    private int price;
    private int stock;
}
