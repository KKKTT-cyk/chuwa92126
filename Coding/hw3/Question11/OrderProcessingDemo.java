import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Product {

    private String id;
    private String name;
    private BigDecimal price;
    private String category;
    private boolean available;

    public Product(
            String id,
            String name,
            BigDecimal price,
            String category,
            boolean available) {

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
        return name + " (" + category + ", $" + price + ")";
    }
}


class Order {

    private String orderId;
    private LocalDateTime orderDate;
    private List<Product> items;
    private String customerEmail;

    public Order(
            String orderId,
            LocalDateTime orderDate,
            List<Product> items,
            String customerEmail) {

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
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}


interface OrderProcessor {

    default BigDecimal calculateTotal(Order order) {

        return order.getItems()
                .stream()
                .map(Product::getPrice)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    static String formatPrice(BigDecimal price) {

        NumberFormat formatter =
                NumberFormat.getCurrencyInstance(Locale.US);

        return formatter.format(price);
    }

    void processOrder(Order order);
}


class OrderService implements OrderProcessor {

    @Override
    public void processOrder(Order order) {

        System.out.println(
                "Processing order: " +
                        order.getOrderId()
        );
    }

    public List<Order> filterOrders(
            List<Order> orders,
            Predicate<Order> condition) {

        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>>
    groupOrdersByCategory(List<Order> orders) {

        return orders.stream()
                .collect(
                        Collectors.groupingBy(order -> {

                            if (order.getItems().isEmpty()) {
                                return "Uncategorized";
                            }

                            return order.getItems()
                                    .get(0)
                                    .getCategory();
                        })
                );
    }

    public Optional<Order> findMostExpensiveOrder(
            List<Order> orders) {

        return orders.stream()
                .max(
                        Comparator.comparing(
                                this::calculateTotal
                        )
                );
    }
}


public class OrderProcessingDemo {

    public static void main(String[] args) {

        Product laptop =
                new Product(
                        "P001",
                        "Laptop",
                        new BigDecimal("999.99"),
                        "Electronics",
                        true
                );

        Product mouse =
                new Product(
                        "P002",
                        "Mouse",
                        new BigDecimal("29.99"),
                        "Electronics",
                        true
                );

        Product book =
                new Product(
                        "P003",
                        "Java Book",
                        new BigDecimal("59.99"),
                        "Books",
                        true
                );

        Product notebook =
                new Product(
                        "P004",
                        "Notebook",
                        new BigDecimal("9.99"),
                        "Stationery",
                        true
                );

        Order order1 =
                new Order(
                        "O001",
                        LocalDateTime.now(),
                        Arrays.asList(laptop, mouse),
                        "customer1@example.com"
                );

        Order order2 =
                new Order(
                        "O002",
                        LocalDateTime.now(),
                        Arrays.asList(book, notebook),
                        "customer2@example.com"
                );

        Order order3 =
                new Order(
                        "O003",
                        LocalDateTime.now(),
                        Arrays.asList(mouse, book),
                        "customer3@example.com"
                );

        List<Order> orders =
                Arrays.asList(order1, order2, order3);

        OrderService service = new OrderService();

        // Process orders using a method reference
        orders.forEach(service::processOrder);


        // Filter orders with total > $100 using lambda
        List<Order> expensiveOrders =
                service.filterOrders(
                        orders,
                        order ->
                                service.calculateTotal(order)
                                        .compareTo(
                                                new BigDecimal("100")
                                        ) > 0
                );

        System.out.println(
                "\nOrders over $100:"
        );

        expensiveOrders.forEach(order ->
                System.out.println(
                        order.getOrderId()
                                + " - "
                                + OrderProcessor.formatPrice(
                                service.calculateTotal(order)
                        )
                )
        );


        // Group orders by category
        Map<String, List<Order>> grouped =
                service.groupOrdersByCategory(orders);

        System.out.println(
                "\nOrders grouped by category:"
        );

        grouped.forEach((category, orderList) -> {

            System.out.println(category + ":");

            orderList.forEach(order ->
                    System.out.println(
                            "  " + order.getOrderId()
                    )
            );
        });


        // Find most expensive order
        Optional<Order> mostExpensive =
                service.findMostExpensiveOrder(orders);

        System.out.println(
                "\nMost expensive order:"
        );

        mostExpensive.ifPresent(order -> {

            System.out.println(
                    order.getOrderId()
                            + " - "
                            + OrderProcessor.formatPrice(
                            service.calculateTotal(order)
                    )
            );
        });
    }
}