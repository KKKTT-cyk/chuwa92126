
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public final class Validator {

  private Validator() {
  }

  public static List<String> validate(Object target) {

    if (target == null) {
      throw new IllegalArgumentException(
          "target must not be null");
    }

    List<String> violations = new ArrayList<>();

    Class<?> clazz = target.getClass();

    while (clazz != null && clazz != Object.class) {

      for (Field field : clazz.getDeclaredFields()) {

        // Ignore static fields
        if (Modifier.isStatic(field.getModifiers())) {
          continue;
        }

        // Ignore fields without validation annotations
        if (!field.isAnnotationPresent(NotNull.class)
            && !field.isAnnotationPresent(NotBlank.class)
            && !field.isAnnotationPresent(Range.class)) {
          continue;
        }

        try {
          // Allow reflective access to private fields
          field.setAccessible(true);

          Object value = field.get(target);
          String name = field.getName();

          // @NotNull
          if (field.isAnnotationPresent(NotNull.class)) {
            if (value == null) {
              violations.add(
                  name + ": must not be null");
            }
          }

          // @NotBlank
          if (field.isAnnotationPresent(NotBlank.class)
              && field.getType() == String.class) {

            if (value == null
                || ((String) value).isBlank()) {

              violations.add(
                  name + ": must not be blank");
            }
          }

          // @Range
          Range range = field.getAnnotation(Range.class);

          if (range != null && value != null) {

            Class<?> type = field.getType();

            if (type == int.class
                || type == long.class
                || type == Integer.class
                || type == Long.class) {

              long number = ((Number) value).longValue();

              if (number < range.min()
                  || number > range.max()) {

                violations.add(
                    name + ": must be between "
                        + range.min()
                        + " and "
                        + range.max());
              }
            }
          }

        } catch (IllegalAccessException e) {
          throw new IllegalStateException(
              "Cannot access field: "
                  + field.getName(), e);
        }
      }

      // Check fields declared in the superclass
      clazz = clazz.getSuperclass();
    }

    // Sort violations in natural String order
    Collections.sort(violations);

    return violations;
  }

  public static void main(String[] args) {

    Person p1 = new Person("   ", 200);
    System.out.println(validate(p1));

    Person p2 = new Person(null, null);
    System.out.println(validate(p2));

    Person p3 = new Person("Alice", 25);
    System.out.println(validate(p3));
  }
}
