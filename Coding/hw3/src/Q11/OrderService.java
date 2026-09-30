package Q11;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class OrderService implements OrderProcessor{

    public List<Order> filterOrders(List<Order> orders, Predicate<Order>
            condition){
        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    Map<String, List<Order>> groupOrdersByCategory(List<Order> orders){
        return orders.stream()
                .filter(order -> !order.getItems().isEmpty())
                .collect(Collectors.groupingBy(
                        order -> order.getItems()
                                .get(0)
                                .getCategory()
                ));
    }

    Optional<Order> findMostExpensiveOrder(List<Order> orders){
        return orders.stream()
                .max(Comparator.comparing(this::calculateTotal));
    }

    @Override
    public BigDecimal calculateTotal(Order order) {
        return OrderProcessor.super.calculateTotal(order);
    }

    @Override
    public void processOrder(Order order) {
        BigDecimal total = calculateTotal(order);
        System.out.println("Processing order: "+order.getOrderId());
        System.out.println("Customer: "+order.getCustomerEmail());
        System.out.println("Total: "+OrderProcessor.formatPrice(total));
    }
}
