import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Creating several products and orders
        Product laptop = new Product("P001", "Laptop", new BigDecimal("999.99"), "Electronics", true);
        Product mouse = new Product("P002", "Mouse", new BigDecimal("25.50"), "Electronics", true);
        Product book = new Product("P003", "Java Book", new BigDecimal("45.00"), "Books", true);
        Product notebook = new Product("P004", "Notebook", new BigDecimal("5.99"), "Books", false);
        Product shirt = new Product("P005", "T-Shirt", new BigDecimal("19.99"), "Clothing", true);

        List<Order> orders = Arrays.asList(
                new Order("O001", LocalDateTime.now(), Arrays.asList(laptop, mouse), "alice@email.com"),
                new Order("O002", LocalDateTime.now(), Arrays.asList(book, notebook), "bob@email.com"),
                new Order("O003", LocalDateTime.now(), Arrays.asList(shirt), "carol@email.com"),
                new Order("O004", LocalDateTime.now(), Arrays.asList(book, book, shirt), "dave@email.com")
        );

        OrderService service = new OrderService();

        System.out.println("--- All orders ---");
        orders.forEach(service::processOrder);

        // Filtering orders with total > $100 using lambda expression
        System.out.println("--- Orders with total > $100 ---");
        List<Order> bigOrders = service.filterOrders(orders,
                order -> service.calculateTotal(order).compareTo(new BigDecimal("100")) > 0);
        bigOrders.stream()
                .map(Order::getOrderId)
                .forEach(System.out::println);

        // Grouping orders by category
        System.out.println("--- Orders grouped by category ---");
        Map<String, List<Order>> byCategory = service.groupOrdersByCategory(orders);
        byCategory.forEach((category, categoryOrders) -> System.out.println(category + ": "
                + categoryOrders.stream().map(Order::getOrderId).collect(Collectors.toList())));

        // Finding the most expensive order and handling the Optional result
        System.out.println("--- Most expensive order ---");
        Optional<Order> mostExpensive = service.findMostExpensiveOrder(orders);
        String result = mostExpensive
                .map(order -> order.getOrderId() + " ("
                        + OrderProcessor.formatPrice(service.calculateTotal(order)) + ")")
                .orElse("No orders");
        System.out.println(result);
    }
}
