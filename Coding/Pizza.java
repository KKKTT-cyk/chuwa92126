
public final class Pizza {

    // Immutable fields
    private final String size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final boolean mushrooms;
    private final String crust;

    // Private constructor
    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.crust = builder.crust;
    }

    // Getter methods
    public String getSize() {
        return size;
    }

    public boolean hasCheese() {
        return cheese;
    }

    public boolean hasPepperoni() {
        return pepperoni;
    }

    public boolean hasMushrooms() {
        return mushrooms;
    }

    public String getCrust() {
        return crust;
    }

    // Static inner Builder class
    public static class Builder {

        // Required field
        private final String size;

        // Optional fields with default values
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        private String crust = "regular";

        // Constructor for required field
        public Builder(String size) {
            if (size == null || size.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Pizza size is required"
                );
            }
            this.size = size;
        }

        // Chainable setter methods
        public Builder cheese(boolean cheese) {
            this.cheese = cheese;
            return this;
        }

        public Builder pepperoni(boolean pepperoni) {
            this.pepperoni = pepperoni;
            return this;
        }

        public Builder mushrooms(boolean mushrooms) {
            this.mushrooms = mushrooms;
            return this;
        }

        public Builder crust(String crust) {
            if (crust == null || crust.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Crust cannot be empty"
                );
            }
            this.crust = crust;
            return this;
        }

        // Build an immutable Pizza
        public Pizza build() {
            return new Pizza(this);
        }
    }

    @Override
    public String toString() {
        return "Pizza{size='" + size
                + "', cheese=" + cheese
                + ", pepperoni=" + pepperoni
                + ", mushrooms=" + mushrooms
                + ", crust='" + crust + "'}";
    }

    // Test the Builder pattern
    public static void main(String[] args) {

        Pizza pizza = new Pizza.Builder("large")
                .cheese(true)
                .pepperoni(true)
                .mushrooms(false)
                .crust("thin")
                .build();

        Pizza plainPizza =
                new Pizza.Builder("medium").build();

        System.out.println(pizza);
        System.out.println(plainPizza);
    }
}
