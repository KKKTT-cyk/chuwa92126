package Coding.Question11;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        OrderService service = new OrderService();

        // 1. Create products and orders
        Product laptop = new Product("P1", "Laptop", new BigDecimal("999.99"), "Electronics", true);
        Product book   = new Product("P2", "Book",   new BigDecimal("20.00"),  "Books",       true);
        Product mug    = new Product("P3", "Mug",    new BigDecimal("12.99"),  "Home",        true);

        List<Order> orders = Arrays.asList(
                new Order("Order-1", LocalDateTime.now(), Arrays.asList(laptop, mug), "a@qq.com"),
                new Order("Order-2", LocalDateTime.now(), Arrays.asList(book), "b@qq.com"),
                new Order("Order-3", LocalDateTime.now(), Arrays.asList(mug, book), "c@qq.com")
        );

        // 2. Filter orders with total > $100 using a lambda expression
        List<Order> bigOrders = service.filterOrders(orders,
                order -> service.calculateTotal(order).compareTo(new BigDecimal("100")) > 0);
        System.out.println("Orders over $100: " + bigOrders.stream()
                .map(Order::getOrderId)
                .collect(Collectors.joining(", ")));

        // 3. Group orders by category
        Map<String, List<Order>> byCategory = service.groupOrdersByCategory(orders);
        System.out.println("Grouped by category:");
        byCategory.forEach((category, categoryOrders) ->
                System.out.println("  " + category + ": " + categoryOrders.stream()
                        .map(Order::getOrderId)
                        .collect(Collectors.joining(", "))));

        // 4. Find the most expensive order and handle the Optional result
        Optional<Order> mostExpensive = service.findMostExpensiveOrder(orders);
        String result = mostExpensive
                .map(Order::getOrderId)            // 5. method reference
                .orElse("No orders found");
        System.out.println("Most expensive order: " + result);
    }
}
