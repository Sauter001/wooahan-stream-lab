package domain.problem.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Order {
    private final String orderId;
    private final String productName;
    private final int quantity;
    private final int price;
    private final String customerName;
    private final LocalDate orderDate;
}
