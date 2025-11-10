package domain.problem.product;

import lombok.Getter;

@Getter
public enum ProductCategory {
    ELECTRONICS("Electronics"), FOOD("Food"), CLOTHING("Clothing"),;

    private final String type;

    ProductCategory(String type) {
        this.type = type;
    }
}
