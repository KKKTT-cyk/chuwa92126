package chuwa92126.Coding.Question_11;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<Product> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return orderId;
    }
}
