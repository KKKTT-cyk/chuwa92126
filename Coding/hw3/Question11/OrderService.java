package Question11;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderService implements OrderProcessor {
  public OrderService(){

  }
  @Override
  public void processOrder(Order order) {
    BigDecimal total = calculateTotal(order);
    System.out.println("Processing " + order.getOrderId() + " for " + order.getCustomerEmail()
        + " with total of " + OrderProcessor.formatPrice(total));

  }

  public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
    return orders.stream()
        .filter(condition)
        .collect(Collectors.toList());
  }

  public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
    return orders.stream()
        .filter(order->order.getItems() != null && !order.getItems().isEmpty())
        .collect(Collectors.groupingBy(
            order->order.getItems().get(0).getCategory()
        ));
  }

  public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
    if (orders == null){
      return Optional.empty();
    }
    return orders.stream()
        .max(Comparator.comparing(this::calculateTotal));
  }


}
