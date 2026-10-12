package Q11;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public interface OrderProcessor {

    default BigDecimal calculateTotal(Order order){
            return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    static String formatPrice(BigDecimal price){
        NumberFormat numberformat = NumberFormat.getCurrencyInstance(Locale.US);
        return numberformat.format(price);
    }

    void processOrder(Order order);
}
