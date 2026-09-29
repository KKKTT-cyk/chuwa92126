import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final String orderId;
    private final LocalDateTime orderDate;
    private final List<Product> items;
    private final String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<Product> getItems() { return items; }
    public String getCustomerEmail() { return customerEmail; }

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
