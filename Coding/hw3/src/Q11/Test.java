package Q11;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        // 1. create several products
        Product moonCake = new Product("p1","moonCake",new BigDecimal("4.5"),"Bread",true);
        Product donut = new Product("p2","donut",new BigDecimal("1.5"),"Bread",true);
        Product laptop = new Product("p3","laptop",new BigDecimal("999.99"),"Electronics",true);
        Product keyBoard = new Product("p4","keyBoard",new BigDecimal("60"),"Electronics",true);

        // 2. create several orders
        Order o1 = new Order("o1", LocalDateTime.now(), Arrays.asList(laptop,keyBoard),"amy@test.com");
        Order o2 = new Order("o2", LocalDateTime.now(), Arrays.asList(moonCake,donut),"tom@test.com");

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);

        OrderService orderService = new OrderService();

        // 3. Filter orders with total greater than $100
        List<Order> expensiveOrders = orderService.filterOrders(
                orders,order -> orderService.calculateTotal(order)
                        .compareTo(new BigDecimal("100")) > 0
        );
        System.out.println("Orders over $100: ");
        expensiveOrders.forEach(orderService::processOrder);

        System.out.println("-------------------------");
        // 4. Group orders by category
        Map<String,List<Order>> ordersByCategory = orderService.groupOrdersByCategory(orders);
        System.out.println("Orders grouped by category: ");
        ordersByCategory.forEach((category, categoryOrders) -> {
            System.out.println("Category: "+category);
            categoryOrders.forEach(orderService::processOrder);
        });

        System.out.println("-------------------------");

        // 5.Finding the most expensive order and handling the Optional result
        Optional<Order> mostExpensive = orderService.findMostExpensiveOrder(orders);
        String message = mostExpensive.
                map(order -> "Most expensive order is : "
                +order.getOrderId()+", total: "
                +OrderProcessor.formatPrice(orderService.calculateTotal(order)))
                .orElse("No orders found.");
        System.out.println(message);


    }
}
