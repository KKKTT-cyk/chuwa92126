class Q12{
    static class Pizza {
        private final int size;
        private final boolean cheese;
        private final boolean pepperoni;
        private final boolean mushroom;
        private final String crust;

        private Pizza(Builder builder) {
            this.size = builder.size;
            this.cheese = builder.cheese;
            this.pepperoni = builder.pepperoni;
            this.mushroom = builder.mushroom;
            this.crust = builder.crust;
        }

        public static class Builder {
            private final int size;
            private boolean cheese = false;
            private boolean pepperoni = false;
            private boolean mushroom = false;
            private String crust = "regular";

            public Builder(int size) {
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
            public Builder mushroom(boolean mushroom) {
                this.mushroom = mushroom;
                return this;
            }
            public Builder crust(String crust) {
                this.crust = crust;
                return this;

            }
            public Pizza build() {
                return new Pizza(this);
            }
        }



    }
}