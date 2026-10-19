package chuwa92126.Coding.Question_11;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        OrderService service = new OrderService();

        Product book = new Product(
                "P3",
                "Python Java Book",
                new BigDecimal("40.00"),
                "Books",
                true
        );


        Product mouse = new Product(
                "P2",
                "Mouse",
                new BigDecimal("50.00"),
                "Electronics",
                true
        );

        // Create products
        Product phone = new Product(
                "P1",
                "Iphone18",
                new BigDecimal("600.00"),
                "Electronics",
                true
        );


        // Create orders
        Order order1 = new Order(
                "O3",
                LocalDateTime.now(),
                Arrays.asList(phone, mouse),
                "user1@email.com"
        );

        Order order2 = new Order(
                "O4",
                LocalDateTime.now(),
                Arrays.asList(book),
                "user2@email.com"
        );

        List<Order> orders = Arrays.asList(order1, order2);

        // Filter orders over $100
        List<Order> over100 = service.filterOrders(
                orders,
                order -> service.calculateTotal(order)
                        .compareTo(new BigDecimal("100")) > 0
        );

        System.out.println("Orders over $100:");

        over100.forEach(order ->
                System.out.println(order.getOrderId())
        );

        // Group orders by category
        Map<String, List<Order>> groupedOrders =
                service.groupOrdersByCategory(orders);

        System.out.println("\nGrouped orders:");

        groupedOrders.forEach((category, list) ->
                System.out.println(category + ": " + list)
        );

        // Find most expensive order
        service.findMostExpensiveOrder(orders)
                .ifPresent(order ->
                        System.out.println(
                                "\nMost expensive order: "
                                + order.getOrderId()
                                + " "
                                + OrderProcessor.formatPrice(
                                    service.calculateTotal(order))
                        )
                );

        // Method reference
        System.out.println("\nProcessing orders:");

        orders.forEach(service::processOrder);
    }
}
