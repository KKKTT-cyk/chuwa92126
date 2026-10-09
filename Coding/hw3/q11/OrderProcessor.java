import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public interface OrderProcessor {

    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(price);
    }

    void processOrder(Order order);
}
