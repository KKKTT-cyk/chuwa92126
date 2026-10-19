package org.tiff.homework2.question11;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderService implements OrderProcessor {
    @Override
    public void processOrder(Order order) {
        System.out.println(order.getOrderId() + ": "
                + OrderProcessor.formatPrice(calculateTotal(order)));
    }

    public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(order -> order.getItems().isEmpty()
                        ? "No items" : order.getItems().get(0).getCategory()));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream()
                .max(Comparator.comparing(this::calculateTotal));
    }
}
