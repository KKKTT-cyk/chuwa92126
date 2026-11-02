import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotNull {
}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Range {
    long min();
    long max();
}
class Person {
    @NotBlank
    private String name;
    @NotNull
    @Range(min = 0, max = 150)
    private Integer age;
    Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}


