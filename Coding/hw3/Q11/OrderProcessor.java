import java.math.BigDecimal;

public interface OrderProcessor {

    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2);
    }

    void processOrder(Order order);
}