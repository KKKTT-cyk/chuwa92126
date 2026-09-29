import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderDemo {
    public static void main(String[] args) {
        Product laptop = new Product("P001", "Laptop", new BigDecimal("999.99"), "Electronics", true);
        Product mouse = new Product("P002", "Mouse", new BigDecimal("29.99"), "Electronics", true);
        Product book = new Product("P003", "Java Book", new BigDecimal("59.99"), "Books", true);
        Product notebook = new Product("P004", "Notebook", new BigDecimal("12.50"), "Stationery", true);

        Order order1 = new Order("O001", LocalDateTime.now(), Arrays.asList(laptop, mouse), "alice@example.com");
        Order order2 = new Order("O002", LocalDateTime.now(), Arrays.asList(book), "bob@example.com");
        Order order3 = new Order("O003", LocalDateTime.now(), Arrays.asList(notebook, book), "carol@example.com");

        List<Order> orders = Arrays.asList(order1, order2, order3);
        OrderService service = new OrderService();

        orders.forEach(service::processOrder);

        System.out.println("\nOrders with total > $100:");
        List<Order> expensiveOrders = service.filterOrders(
                orders,
                order -> service.calculateTotal(order).compareTo(new BigDecimal("100")) > 0
        );

        expensiveOrders.forEach(order -> System.out.println(
                order.getOrderId() + " -> " +
                        OrderProcessor.formatPrice(service.calculateTotal(order))
        ));

        System.out.println("\nOrders grouped by category:");
        Map<String, List<Order>> grouped = service.groupOrdersByCategory(orders);
        grouped.forEach((category, categoryOrders) ->
                System.out.println(category + " -> " + categoryOrders));

        System.out.println("\nMost expensive order:");
        Optional<Order> mostExpensive = service.findMostExpensiveOrder(orders);
        mostExpensive.ifPresent(order -> System.out.println(
                order.getOrderId() + " -> " +
                        OrderProcessor.formatPrice(service.calculateTotal(order))
        ));
    }
}
