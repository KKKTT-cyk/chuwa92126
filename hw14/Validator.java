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
            throw new IllegalArgumentException("target must not be null");
        }

        List<String> violations = new ArrayList<>();

        // Walk up the class hierarchy: own class, then superclasses, stopping before Object.
        for (Class<?> clazz = target.getClass();
             clazz != null && clazz != Object.class;
             clazz = clazz.getSuperclass()) {

            // getDeclaredFields() returns all fields declared in this class, including private ones.
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(target);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot read field " + field.getName(), e);
                }

                String name = field.getName();

                if (field.isAnnotationPresent(NotNull.class) && value == null) {
                    violations.add(name + ": must not be null");
                }

                if (field.isAnnotationPresent(NotBlank.class) && field.getType() == String.class) {
                    if (value == null || ((String) value).isBlank()) {
                        violations.add(name + ": must not be blank");
                    }
                }

                // Primitive int/long values are boxed by field.get(), so one check covers both.
                // A null wrapper is not an Integer/Long instance, so it is skipped automatically.
                Range range = field.getAnnotation(Range.class);
                if (range != null && (value instanceof Integer || value instanceof Long)) {
                    long number = ((Number) value).longValue();
                    if (number < range.min() || number > range.max()) {
                        violations.add(name + ": must be between "
                                + range.min() + " and " + range.max());
                    }
                }
            }
        }

        Collections.sort(violations);
        return violations;
    }
}