package Coding.Question11;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final String orderId;
    private final LocalDateTime orderDate;
    private final List<Product> items;
    private final String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        // Never store null: an order with no items holds an empty list
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        this.customerEmail = customerEmail;
    }
    public String getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<Product> getItems() { return Collections.unmodifiableList(items); }
    public String getCustomerEmail() { return customerEmail; }

    @Override
    public String toString() {
        return orderId;
    }
}
