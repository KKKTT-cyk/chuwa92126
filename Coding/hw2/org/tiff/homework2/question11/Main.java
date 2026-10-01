package org.tiff.homework2.question11;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Product keyboard = new Product("P1", "Keyboard", new BigDecimal("80.00"), "Electronics", true);
        Product mouse = new Product("P2", "Mouse", new BigDecimal("30.00"), "Electronics", true);
        Product book = new Product("P3", "Java Book", new BigDecimal("45.50"), "Books", true);
        Product headphones = new Product("P4", "Headphones", new BigDecimal("120.00"), "Electronics", true);

        LocalDateTime date = LocalDateTime.of(2026, 9, 28, 10, 0);
        List<Order> orders = Arrays.asList(
                new Order("O1", date, Arrays.asList(keyboard, mouse), "alex@example.com"),
                new Order("O2", date.plusHours(1), Arrays.asList(book), "sam@example.com"),
                new Order("O3", date.plusHours(2), Arrays.asList(headphones, book), "mia@example.com")
        );
        OrderService service = new OrderService();

        System.out.println("All orders:");
        orders.forEach(service::processOrder);

        System.out.println("Orders over $100:");
        service.filterOrders(orders,
                order -> service.calculateTotal(order).compareTo(new BigDecimal("100")) > 0)
                .forEach(service::processOrder);

        System.out.println("Orders grouped by first product category:");
        service.groupOrdersByCategory(orders)
                .forEach((category, group) -> System.out.println(category + ": " + group));

        Optional<Order> mostExpensive = service.findMostExpensiveOrder(orders);
        System.out.println("Most expensive order: "
                + mostExpensive.map(Order::getOrderId).orElse("No orders"));
        mostExpensive.ifPresent(service::processOrder);
    }
}
