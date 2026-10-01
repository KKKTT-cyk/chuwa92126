import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderSystemDemo {

    public static void main(String[] args) {
        OrderService service = new OrderService();

        Product laptop   = new Product("P1", "Laptop",   new BigDecimal("1299.99"), "Electronics", true);
        Product mouse    = new Product("P2", "Mouse",    new BigDecimal("25.50"),   "Electronics", true);
        Product novel    = new Product("P3", "Novel",    new BigDecimal("15.99"),   "Books",       true);
        Product textbook = new Product("P4", "Textbook", new BigDecimal("120.00"),  "Books",       false);
        Product tshirt   = new Product("P5", "T-Shirt",  new BigDecimal("19.99"),   "Clothing",    true);
        Product jacket   = new Product("P6", "Jacket",   new BigDecimal("89.90"),   "Clothing",    true);

        List<Order> orders = Arrays.asList(
                new Order("O1", LocalDateTime.of(2026, 3, 1, 10, 30), Arrays.asList(laptop, mouse), "alice@example.com"),
                new Order("O2", LocalDateTime.of(2026, 3, 2, 14, 0), Arrays.asList(novel), "bob@example.com"),
                new Order("O3", LocalDateTime.of(2026, 3, 3, 9, 15), Arrays.asList(textbook, novel), "carol@example.com"),
                new Order("O4", LocalDateTime.of(2026, 3, 4, 18, 45), Arrays.asList(tshirt, jacket), "dave@example.com"),
                new Order("O5", LocalDateTime.of(2026, 3, 5, 11, 0), Arrays.asList(tshirt), "erin@example.com")
        );

        System.out.println("All orders:");
        orders.forEach(service::processOrder);

        // filter orders with total > 100
        BigDecimal limit = new BigDecimal("100");
        List<Order> bigOrders = service.filterOrders(orders,
                o -> service.calculateTotal(o).compareTo(limit) > 0);

        System.out.println();
        System.out.println("Orders over $100:");
        bigOrders.forEach(o ->
                System.out.println(o.getOrderId() + " " + OrderProcessor.formatPrice(service.calculateTotal(o))));

        // group by category of first product
        System.out.println();
        System.out.println("Grouped by category:");
        Map<String, List<Order>> groups = service.groupOrdersByCategory(orders);
        groups.forEach((category, list) -> {
            List<String> ids = list.stream()
                    .map(Order::getOrderId)
                    .collect(Collectors.toList());
            System.out.println(category + " " + ids);
        });

        // most expensive order
        System.out.println();
        Optional<Order> maxOrder = service.findMostExpensiveOrder(orders);
        maxOrder.ifPresent(o -> System.out.println("Most expensive: " + o.getOrderId()
                + " " + OrderProcessor.formatPrice(service.calculateTotal(o))));

        String maxId = maxOrder.map(Order::getOrderId).orElse("none");
        System.out.println("max order id = " + maxId);

        // empty list should not throw
        String emptyResult = service.findMostExpensiveOrder(Collections.<Order>emptyList())
                .map(Order::getOrderId)
                .orElse("none");
        System.out.println("empty list -> " + emptyResult);

        System.out.println();
        System.out.println("Totals:");
        orders.stream()
                .map(service::calculateTotal)
                .map(OrderProcessor::formatPrice)
                .forEach(System.out::println);
    }
}

class Product {
    private final String id;
    private final String name;
    private final BigDecimal price;
    private final String category;
    private final boolean available;

    public Product(String id, String name, BigDecimal price, String category, boolean available) {
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

    @Override
    public String toString() {
        return name + "(" + OrderProcessor.formatPrice(price) + ")";
    }
}

class Order {
    private final String orderId;
    private final LocalDateTime orderDate;
    private final List<Product> items;
    private final String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        // copy the list so outside changes don't affect the order
        this.items = items == null ? new ArrayList<Product>() : new ArrayList<>(items);
        this.customerEmail = customerEmail;
    }

    public String getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<Product> getItems() { return Collections.unmodifiableList(items); }
    public String getCustomerEmail() { return customerEmail; }

    @Override
    public String toString() {
        return orderId + " " + items;
    }
}

interface OrderProcessor {

    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(Product::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        if (price == null) {
            return "$0.00";
        }
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }

    void processOrder(Order order);
}

class OrderService implements OrderProcessor {

    @Override
    public void processOrder(Order order) {
        System.out.println(order.getOrderId() + " " + order.getCustomerEmail()
                + " items=" + order.getItems().size()
                + " total=" + OrderProcessor.formatPrice(calculateTotal(order)));
    }

    public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        // orders with no items go to "Unknown"
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getItems().isEmpty()
                        ? "Unknown"
                        : o.getItems().get(0).getCategory()));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream()
                .max(Comparator.comparing(this::calculateTotal));
    }
}



