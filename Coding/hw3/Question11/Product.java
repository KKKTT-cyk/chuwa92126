package Question11;

import java.math.BigDecimal;

public class Product {
  String id;
  String name;
  BigDecimal price;
  String category;
  boolean available;

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
    return String.format("Product{id='%s', name = '%s', price=%s, category ='%s', available=%b}",
        id, name, OrderProcessor.formatPrice(price), category, available);
  }

  public static class Main {
    public static void main(String[] args) {
      //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
      // to see how IntelliJ IDEA suggests fixing it.
      System.out.printf("Hello and welcome!");

      for (int i = 1; i <= 5; i++) {
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        System.out.println("i = " + i);
      }
    }
  }
}
