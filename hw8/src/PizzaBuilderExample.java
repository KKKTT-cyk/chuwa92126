
class Pizza {
    // all fields are final -> cannot be changed after creation
    private final String size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final boolean mushrooms;
    private final String crust;


    // private constructor: Pizza can only be created through Builder
    public Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.crust = builder.crust;
    }

    // getters only, no setters
    public String getSize()       { return size; }
    public boolean hasCheese()    { return cheese; }
    public boolean hasPepperoni() { return pepperoni; }
    public boolean hasMushrooms() { return mushrooms; }
    public String getCrust()      { return crust; }


    @Override
    public String toString() {
        return "Pizza{size=" + size
                + ", cheese=" + cheese
                + ", pepperoni=" + pepperoni
                + ", mushrooms=" + mushrooms
                + ", crust=" + crust + "}";
    }



    // static nested Builder class
    public static class Builder {
        private final String size; // required
        private boolean cheese = false; // optional
        private boolean pepperoni = false; // optional
        private boolean mushrooms = false; // optional
        private String crust = "regular"; // optional   

        public Builder(String size) {
            if (size == null || size.isEmpty()) {
                throw new IllegalArgumentException("Size is required");
            }
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
            return new Pizza(this);
        }
    }
}


public class PizzaBuilderExample {
    public static void main(String[] args) {
        Pizza custom = new Pizza.Builder("large")
                .cheese(true)
                .mushrooms(true)
                .crust("thin")
                .build();
        System.out.println(custom);

        Pizza plain = new Pizza.Builder("small").build();
        System.out.println(plain);
    }
}