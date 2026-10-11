public final class Pizza {
    private final String size;
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

    public String getSize() { return size; }
    public boolean hasCheese() { return cheese; }
    public boolean hasPepperoni() { return pepperoni; }
    public boolean hasMushrooms() { return mushrooms; }
    public String getCrust() { return crust; }

    @Override
    public String toString() {
        return "Pizza{size='" + size + "', cheese=" + cheese
                + ", pepperoni=" + pepperoni + ", mushrooms=" + mushrooms
                + ", crust='" + crust + "'}";
    }

    public static class Builder {
        private final String size;
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        private String crust = "regular";

        public Builder(String size) {
            if (size == null || size.trim().isEmpty()) {
                throw new IllegalArgumentException("Pizza size is required");
            }
            this.size = size;
        }

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
                throw new IllegalArgumentException("Crust cannot be empty");
            }
            this.crust = crust;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}
