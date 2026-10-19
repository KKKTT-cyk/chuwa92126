public class Pizza {
    private final int size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final boolean mushrooms;
    private final String crust;

    // Private constructor - only the Builder can create a Pizza
    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.crust = builder.crust;
    }

    @Override
    public String toString() {
        return "Pizza{size=" + size + ", cheese=" + cheese + ", pepperoni=" + pepperoni
                + ", mushrooms=" + mushrooms + ", crust='" + crust + "'}";
    }

    public static class Builder {
        // Required parameter
        private final int size;

        // Optional parameters - default values
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        private String crust = "regular";

        public Builder(int size) {
            this.size = size;   // Required field enforced via constructor
        }

        public Builder cheese(boolean value) {
            this.cheese = value;
            return this;   // Returns "this" - enables method chaining
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
            return new Pizza(this);
        }
    }
}
