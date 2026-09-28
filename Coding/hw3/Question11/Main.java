package Question11;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
  public static void main(String[] args) {
    // Create Products
    Product p1 = new Product("P101", "Laptop", new BigDecimal("1200.00"), "Electronics", true);
    Product p2 =
        new Product("P102", "Wireless Mouse", new BigDecimal("25.50"), "Electronics", true);
    Product p3 =
        new Product("P103", "Java Programming Guide", new BigDecimal("45.00"), "Books", true);
    Product p4 =
        new Product("P104", "Data Structures Handbook", new BigDecimal("60.00"), "Books", true);
    Product p5 = new Product("P105", "Running Shoes", new BigDecimal("85.00"), "Apparel", true);

    // Create Orders
    Order o1 = new Order("ORD-001", LocalDateTime.now(), Arrays.asList(p1, p2),
        "alice@example.com"); // Total: $1225.50
    Order o2 = new Order("ORD-002", LocalDateTime.now().minusDays(1), Arrays.asList(p3, p4),
        "bob@example.com"); // Total: $105.00
    Order o3 = new Order("ORD-003", LocalDateTime.now().minusDays(2), Arrays.asList(p5),
        "charlie@example.com"); // Total: $85.00

    List<Order> orders = Arrays.asList(o1, o2, o3);
    OrderService orderService = new OrderService();

    // 1. Process all orders
    System.out.println("=== 1. Processing Orders ===");
    orders.forEach(orderService::processOrder);

    // 2. Filter orders with total > $ 100 using a Lambda
    System.out.println("\n=== 2. Filtering Orders (Total > $ 100) ===");
    List<Order> bigOrders = orderService.filterOrders(orders,
        order -> orderService.calculateTotal(order)
            .compareTo(new BigDecimal("100.00")) > 0);
    bigOrders.forEach(order -> System.out.println(
        order.getOrderId() + " -> Total: " +
            OrderProcessor.formatPrice(orderService.calculateTotal(order)))
    );

    // 3. Group orders by category of the first product
    System.out.println("\n=== 3. Grouping Orders by First Product Category ===");
    Map<String, List<Order>> groupByCategory = orderService.groupOrdersByCategory(orders);
    groupByCategory.forEach((category, orderList) -> {
      System.out.println("Category: " + category);
      orderList.forEach(order -> System.out.println(
          " -" + order.getOrderId() + " ordered by " + order.getCustomerEmail()));
    });

    // 4. Find the most expensive order and handle the Optional result safe
    System.out.println("\n=== 4. Finding Most Expensive Order (Optional) ===");
    Optional<Order> mostExpensiveOpt = orderService.findMostExpensiveOrder(orders);
    if (mostExpensiveOpt == null || !mostExpensiveOpt.isPresent()) {
      System.out.println("No order found.");
    }
    mostExpensiveOpt.ifPresent(order -> {
      BigDecimal maxTotal = orderService.calculateTotal(order);
      System.out.println("Most Expensive Order: " + order.getOrderId()
          + " placed by " + order.getCustomerEmail()
          + " with Total = " + OrderProcessor.formatPrice(maxTotal));
    });
  }
}