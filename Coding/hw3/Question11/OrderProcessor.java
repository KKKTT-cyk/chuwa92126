package Question11;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public interface OrderProcessor {
  default BigDecimal calculateTotal(Order order) {
    if (order == null || order.getItems() == null) {
      return BigDecimal.ZERO;
    }
    return order.getItems()
        .stream()
        .map(Product::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  static String formatPrice(BigDecimal price) {
    if (price == null) {
      return "$0.00";
    }
    return String.format("$%.2f", price);
  }

  abstract void processOrder(Order order);
}
