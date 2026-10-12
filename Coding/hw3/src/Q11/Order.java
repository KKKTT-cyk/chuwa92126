package Q11;

import java.time.LocalDateTime;
import java.util.*;

public class Order {
    private String OrderId;
    private LocalDateTime orderDate;
    private List<Product> items;
    private String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        OrderId = orderId;
        this.orderDate = orderDate;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() {
        return OrderId;
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
}
