package chuwa92126.Coding;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Product class
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return name;
    }
}


// Order class
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

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<Product> getItems() {
        return items;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    @Override
    public String toString() {
        return orderId;
    }
}


// Interface
interface OrderProcessor {

    // Default method
    default BigDecimal calculateTotal(Order order) {
        return order.getItems()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Static method
    static String formatPrice(BigDecimal price) {
        return NumberFormat
                .getCurrencyInstance(Locale.US)
                .format(price);
    }

    // Abstract method
    void processOrder(Order order);
}


// Service class
class OrderService implements OrderProcessor {

    @Override
    public void processOrder(Order order) {
        System.out.println("Processing order: " + order.getOrderId());
    }

    // Filter orders using Predicate
    public List<Order> filterOrders(
            List<Order> orders,
            Predicate<Order> condition) {

        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    // Group by first product's category
    public Map<String, List<Order>> groupOrdersByCategory(
            List<Order> orders) {

        return orders.stream()
                .filter(order -> !order.getItems().isEmpty())
                .collect(Collectors.groupingBy(
                        order -> order.getItems()
                                .get(0)
                                .getCategory()
                ));
    }

    // Find most expensive order
    public Optional<Order> findMostExpensiveOrder(
            List<Order> orders) {

        return orders.stream()
                .max(Comparator.comparing(this::calculateTotal));
    }
}


// Main class
public class hw3_question11 {

    public static void main(String[] args) {

        // Create products
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
                new BigDecimal("29.99"),
                "Electronics",
                true
        );

        Product book = new Product(
                "P3",
                "Java Book",
                new BigDecimal("49.99"),
                "Books",
                true
        );

        Product pen = new Product(
                "P4",
                "Pen",
                new BigDecimal("5.99"),
                "Stationery",
                true
        );

        // Create orders
        Order order1 = new Order(
                "O1",
                LocalDateTime.now(),
                Arrays.asList(laptop, mouse),
                "alice@example.com"
        );

        Order order2 = new Order(
                "O2",
                LocalDateTime.now(),
                Arrays.asList(book),
                "bob@example.com"
        );

        Order order3 = new Order(
                "O3",
                LocalDateTime.now(),
                Arrays.asList(pen),
                "charlie@example.com"
        );

        List<Order> orders =
                Arrays.asList(order1, order2, order3);

        OrderService service = new OrderService();


        // 1. Filter orders with total > $100
        List<Order> expensiveOrders =
                service.filterOrders(
                        orders,
                        order ->
                                service.calculateTotal(order)
                                        .compareTo(
                                                new BigDecimal("100")
                                        ) > 0
                );

        System.out.println("Orders over $100:");

        expensiveOrders.forEach(
                order ->
                        System.out.println(order.getOrderId())
        );


        // 2. Group orders by category
        System.out.println("\nOrders grouped by category:");

        Map<String, List<Order>> grouped =
                service.groupOrdersByCategory(orders);

        grouped.forEach(
                (category, orderList) ->
                        System.out.println(
                                category + ": " + orderList
                        )
        );


        // 3. Find most expensive order
        System.out.println("\nMost expensive order:");

        Optional<Order> mostExpensive =
                service.findMostExpensiveOrder(orders);

        mostExpensive.ifPresent(order -> {
            System.out.println("Order: " + order.getOrderId());

            System.out.println(
                    "Total: " +
                    OrderProcessor.formatPrice(
                            service.calculateTotal(order)
                    )
            );
        });


        // 4. Method reference
        System.out.println("\nProcessing all orders:");

        orders.forEach(service::processOrder);
    }
}
