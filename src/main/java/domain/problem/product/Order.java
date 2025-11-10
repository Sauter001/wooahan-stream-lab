package domain.problem.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class Order {
    private String orderId;
    private String productName;
    private int quantity;
    private int price;
    private String customerName;
    private LocalDate orderDate;
}
