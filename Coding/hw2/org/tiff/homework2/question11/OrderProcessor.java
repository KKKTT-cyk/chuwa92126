package org.tiff.homework2.question11;

import java.math.BigDecimal;
import java.math.RoundingMode;

@FunctionalInterface
public interface OrderProcessor {
    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    void processOrder(Order order);
}
