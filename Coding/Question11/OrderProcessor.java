package Coding.Question11;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public interface OrderProcessor {


    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
        // Static method: formats a price as a US currency string, e.g. "$99.99"
    static String formatPrice(BigDecimal price) {
        if (price == null) {
            return "N/A";
        }
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }

    // Abstract method: each implementation decides how to process an order
    void processOrder(Order order);


}
