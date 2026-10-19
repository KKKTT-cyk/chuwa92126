import java.math.BigDecimal;
import java.math.RoundingMode;

public interface OrderProcessor {

    // Default method: total price of all products in the order
    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Static method: format a price as a currency string, e.g. "$99.99"
    static String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP);
    }

    // Abstract method
    void processOrder(Order order);
}
