class Pizza {
  private final int size;
  private final boolean cheese;
  private final boolean pepperoni;
  private final boolean mushrooms;
  private final String crust;

  private Pizza(Builder builder) {
    this.size = builder.size;
    this.cheese = builder.cheese;
    this.pepperoni = builder.pepperoni;
    this.mushrooms = builder.mushrooms;
    this.crust = builder.crust;
  }

  public static class Builder {
    private final int size;
    private boolean cheese = false;
    private boolean pepperoni = false;
    private boolean mushrooms = false;
    private String crust = "regular";

    public Builder(int size) {
      this.size = size;
    }

    public Builder cheese(boolean value) {
      this.cheese = value;
      return this;
    }

    public Builder pepperoni(boolean value) {
      this.pepperoni = value;
      return this;
    }

    public Builder mushrooms(boolean value) {
      this.mushrooms = value;
      return this;
    }

    public Builder crust(String value) {
      this.crust = value;
      return this;
    }

    public Pizza build() {
      if (size <= 0) {
        throw new IllegalStateException("Size must be positive");
      }
      if (crust == null || crust.isEmpty()) {
        throw new IllegalStateException("Crust must not be empty");
      }
      return new Pizza(this);
    }
  }

  @Override
  public String toString() {
    return "Pizza {size = " + size
        + ", cheese = " + cheese
        + ", pepperoni = " + pepperoni
        + ", mushrooms = " + mushrooms
        + ", crust = '" + crust + "'}";
  }
}


public class Question12 {
  public static void main(String args[]) {
    Pizza plain = new Pizza.Builder(12).build();
    System.out.println(plain);

    Pizza custom = new Pizza.Builder(14)
        .pepperoni(true)
        .cheese(true)
        .mushrooms(false)
        .crust("thin")
        .build();
    System.out.println(custom);
  }
}
