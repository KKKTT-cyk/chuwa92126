import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private String category;
    private boolean available;

    public Product(String id, String name, BigDecimal price,
                   String category, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }
}

class Order {
    private String orderId;
    private LocalDateTime orderDate;
    private List<Product> items;
    private String customerEmail;

    public Order(String orderId, LocalDateTime orderDate,
                 List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<Product> getItems() { return items; }
    public String getCustomerEmail() { return customerEmail; }
}

interface OrderProcessor {
    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP);
    }

    void processOrder(Order order);
}

class OrderService implements OrderProcessor {
    @Override
    public void processOrder(Order order) {
        System.out.println("Processing order " + order.getOrderId()
                + ": " + OrderProcessor.formatPrice(calculateTotal(order)));
    }

    public List<Order> filterOrders(List<Order> orders,
                                    Predicate<Order> condition) {
        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        return orders.stream().collect(Collectors.groupingBy(order ->
                order.getItems().isEmpty()
                        ? "Uncategorized"
                        : order.getItems().get(0).getCategory()
        ));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream()
                .max(Comparator.comparing(this::calculateTotal));
    }
}

public class Main {
    public static void main(String[] args) {
        Product headphones = new Product(
                "P1", "Headphones", new BigDecimal("80.00"),
                "Electronics", true);
        Product mouse = new Product(
                "P2", "Mouse", new BigDecimal("30.00"),
                "Electronics", true);
        Product book = new Product(
                "P3", "Book", new BigDecimal("40.00"),
                "Books", true);
        Product keyboard = new Product(
                "P4", "Keyboard", new BigDecimal("150.00"),
                "Electronics", true);

        List<Order> orders = Arrays.asList(
                new Order("O1", LocalDateTime.now(),
                        Arrays.asList(headphones, mouse), "a@example.com"),
                new Order("O2", LocalDateTime.now(),
                        Arrays.asList(book), "b@example.com"),
                new Order("O3", LocalDateTime.now(),
                        Arrays.asList(keyboard), "c@example.com")
        );

        OrderService service = new OrderService();

        // Lambda: keep orders whose total is greater than $100.
        List<Order> expensiveOrders = service.filterOrders(
                orders,
                order -> service.calculateTotal(order)
                        .compareTo(new BigDecimal("100.00")) > 0
        );
        System.out.println("Orders over $100:");
        expensiveOrders.forEach(service::processOrder);

        // Group by the category of each order's first product.
        Map<String, List<Order>> groups =
                service.groupOrdersByCategory(orders);
        groups.forEach((category, categoryOrders) ->
                System.out.println(category + ": "
                        + categoryOrders.stream()
                        .map(Order::getOrderId)
                        .collect(Collectors.toList()))
        );

        // Handle the Optional result.
        service.findMostExpensiveOrder(orders)
                .ifPresent(order -> System.out.println(
                        "Most expensive: " + order.getOrderId() + " ("
                                + OrderProcessor.formatPrice(
                                service.calculateTotal(order)) + ")"
                ));
    }
}