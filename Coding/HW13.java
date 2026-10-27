import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
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

        // Check the target class and each superclass, stopping before Object.
        for (Class<?> currentClass = target.getClass();
             currentClass != null && currentClass != Object.class;
             currentClass = currentClass.getSuperclass()) {

            for (Field field : currentClass.getDeclaredFields()) {
                // Ignore static fields.
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                boolean hasValidationAnnotation =
                        field.isAnnotationPresent(NotNull.class)
                                || field.isAnnotationPresent(NotBlank.class)
                                || field.isAnnotationPresent(Range.class);

                if (!hasValidationAnnotation) {
                    continue;
                }

                try {
                    // Allow reflection to access private fields.
                    field.setAccessible(true);
                    Object value = field.get(target);

                    if (field.isAnnotationPresent(NotNull.class)
                            && value == null) {
                        violations.add(field.getName() + ": must not be null");
                    }

                    if (field.isAnnotationPresent(NotBlank.class)
                            && field.getType() == String.class
                            && (value == null || ((String) value).isBlank())) {
                        violations.add(field.getName() + ": must not be blank");
                    }

                    if (field.isAnnotationPresent(Range.class)) {
                        Class<?> type = field.getType();

                        boolean isIntOrLong =
                                type == int.class
                                        || type == Integer.class
                                        || type == long.class
                                        || type == Long.class;

                        // A null Integer or Long value is not checked by @Range.
                        if (isIntOrLong && value != null) {
                            Range range = field.getAnnotation(Range.class);
                            long number = ((Number) value).longValue();

                            if (number < range.min() || number > range.max()) {
                                violations.add(
                                        field.getName()
                                                + ": must be between "
                                                + range.min()
                                                + " and "
                                                + range.max());
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(
                            "Cannot access field: " + field.getName(), e);
                }
            }
        }

        // Sort violations in natural String order.
        violations.sort(Comparator.naturalOrder());
        return violations;
    }
}