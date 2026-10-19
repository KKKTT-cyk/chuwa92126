package com.lant.hw3;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

class Order {
    private String orderId;
    private LocalDateTime orderDate;
    private List<Product> items;
    private String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", items=" + items +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}

class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private String category;
    private boolean available;

    public Product(String id, String name, BigDecimal price, String category, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", available=" + available +
                '}';
    }
}

interface OrderProcessor {
    default BigDecimal calculateTotal(Order order) {
        return order.getItems().stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        return String.format("$%.2f", price);
    }

    abstract void processOrder(Order order);
}

class OrderService implements OrderProcessor {
    public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
        return orders.stream().filter(condition).toList();
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        return orders.stream().filter(o -> !o.getItems().isEmpty())
                .collect(Collectors.groupingBy(order -> order.getItems().get(0).getCategory()));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream().max(Comparator.comparing(this::calculateTotal));
    }

    @Override
    public void processOrder(Order order) {

    }
}

public class Lambda_order {
    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("P001", "Laptop", new BigDecimal("1299.99"), "Electronics", true),
                new Product("P002", "Headphones", new BigDecimal("199.50"), "Electronics", true),
                new Product("P003", "Coffee Mug", new BigDecimal("12.99"), "Kitchen", true),
                new Product("P004", "Blender", new BigDecimal("89.00"), "Kitchen", false),
                new Product("P005", "Java Book", new BigDecimal("45.99"), "Books", true),
                new Product("P006", "Notebook", new BigDecimal("5.49"), "Stationery", true)
        );
        List<Order> orders = List.of(
                new Order("O1001", LocalDateTime.of(2026, 9, 1, 10, 30),
                        List.of(products.get(0), products.get(1)), "alice@example.com"),
                new Order("O1002", LocalDateTime.of(2026, 9, 5, 14, 15),
                        List.of(products.get(2), products.get(3)), "bob@example.com"),
                new Order("O1003", LocalDateTime.of(2026, 9, 10, 9, 0),
                        List.of(products.get(4)), "alice@example.com"),
                new Order("O1004", LocalDateTime.of(2026, 9, 12, 18, 45),
                        List.of(products.get(5), products.get(2), products.get(4)), "carol@example.com"),
                new Order("O1005", LocalDateTime.of(2026, 9, 20, 11, 20),
                        List.of(products.get(5), products.get(2), products.get(4)), "dave@example.com"));

        System.out.println("==== greater than 100");
        OrderService service = new OrderService();
        List<Order> greaterThan100 = service.filterOrders(orders,
                (order) -> service.calculateTotal(order).compareTo(BigDecimal.valueOf(100)) > 0);
        greaterThan100.forEach(System.out::println);

        System.out.println("==== group by");
        Map<String, List<Order>> stringListMap = service.groupOrdersByCategory(orders);
        stringListMap.forEach((k, v) -> {
            System.out.println("key: " + k + " value: " + v);
        });

        System.out.println("==== most expensive if exist");
        Optional<Order> mostExpensiveOrder = service.findMostExpensiveOrder(orders);
        mostExpensiveOrder.ifPresent(System.out::println);




    }
}
