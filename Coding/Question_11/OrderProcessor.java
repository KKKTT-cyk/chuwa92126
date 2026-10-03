package chuwa92126.Coding.Question_11;

import java.math.BigDecimal;

public interface OrderProcessor {

    default BigDecimal calculateTotal(Order order) {
        return order.getItems()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        return String.format("$%.2f", price);
    }

    void processOrder(Order order);
}