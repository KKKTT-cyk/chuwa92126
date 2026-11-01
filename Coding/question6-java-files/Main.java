public class Main {
    public static void main(String[] args) {
        System.out.println(Validator.validate(new Person("Alice", 25))); // []
        System.out.println(Validator.validate(new Person("  ", 151)));
        // [age: must be between 0 and 150, name: must not be blank]
        System.out.println(Validator.validate(new Person(null, null)));
        // [age: must not be null, name: must not be blank]
    }
}
