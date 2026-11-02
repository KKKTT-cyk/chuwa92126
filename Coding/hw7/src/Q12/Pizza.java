package Q12;

import java.lang.module.ModuleDescriptor;

public class Pizza {
    private final int size;
    private final boolean pepperoni;
    private final boolean cheese;
    private final boolean mushroom;
    private final String crust;

    public Pizza(Builder builder) {
        this.size = builder.size;
        this.pepperoni = builder.pepperoni;
        this.cheese = builder.cheese;
        this.mushroom = builder.mushroom;
        this.crust= builder.crust;
    }

    public static class Builder{
        private final int size;
        private boolean pepperoni = false;
        private boolean cheese = false;
        private boolean mushroom=false;
        private String crust = "regular";

        public Builder(int size){
            this.size=size;
        }
        public Builder cheese (boolean value){
            this.cheese=value;
            return this;
        }
        public Builder pepperoni (boolean value){
            this.pepperoni=value;
            return this;
        }
        public Builder mushroom(boolean value){
            this.mushroom=value;
            return this;
        }
        public Builder crust(String value){
            this.crust=value;
            return this;
        }
        public Pizza build (){
            return new Pizza(this);
        }


    }

    @Override
    public String toString() {
        return "Pizza{" +
                "size=" + size +
                ", pepperoni=" + pepperoni +
                ", cheese=" + cheese +
                ", mushroom=" + mushroom +
                ", crust='" + crust + '\'' +
                '}';
    }
}
