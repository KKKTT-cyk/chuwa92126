import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        Product laptop = new Product(
                "P1",
                "Laptop",
                new BigDecimal("999.99"),
                "Electronics",
                true
        );

        Product mouse = new Product(
                "P2",
                "Mouse",
                new BigDecimal("25.00"),
                "Electronics",
                true
        );

        Product book = new Product(
                "P3",
                "Study Note",
                new BigDecimal("35.00"),
                "Books",
                true
        );

        Product notebook = new Product(
                "P4",
                "Notebook",
                new BigDecimal("10.00"),
                "Stationery",
                true
        );

        Order order1 = new Order(
                "O1",
                LocalDateTime.now(),
                Arrays.asList(laptop, mouse),
                "customer1@abc.com"
        );

        Order order2 = new Order(
                "O2",
                LocalDateTime.now(),
                Arrays.asList(book),
                "customer2@abc.com"
        );

        Order order3 = new Order(
                "O3",
                LocalDateTime.now(),
                Arrays.asList(notebook, book),
                "customer3@abc.com"
        );

        List<Order> orders = Arrays.asList(order1, order2, order3);

        OrderService service = new OrderService();

        List<Order> expensiveOrders = service.filterOrders(
                orders,
                order -> service.calculateTotal(order)
                        .compareTo(new BigDecimal("100")) > 0
        );

        System.out.println("Orders over $100:");
        expensiveOrders.forEach(
                order -> System.out.println(order.getOrderId())
        );

        Map<String, List<Order>> groupedOrders =
                service.groupOrdersByCategory(orders);

        System.out.println("\nOrders grouped by category:");

        groupedOrders.forEach((category, categoryOrders) -> {
            System.out.println(category);

            categoryOrders.forEach(
                    order -> System.out.println("  " + order.getOrderId())
            );
        });

        Optional<Order> mostExpensive =
                service.findMostExpensiveOrder(orders);

        mostExpensive.ifPresent(order ->
                System.out.println(
                        "\nMost expensive order: "
                                + order.getOrderId()
                                + " - "
                                + OrderProcessor.formatPrice(
                                        service.calculateTotal(order)
                                )
                )
        );

        orders.forEach(service::processOrder);
    }
}