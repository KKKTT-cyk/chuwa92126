import java.lang.reflect.*;
import java.util.*;

public final class Validator {

    private Validator() {
    }

    public static List<String> validate(Object target) {

        if (target == null) {
            throw new IllegalArgumentException("Target is null");
        }

        List<String> violations = new ArrayList<>();

        Class<?> clazz = target.getClass();

        while (clazz != null && clazz != Object.class) {

            for (Field field : clazz.getDeclaredFields()) {

                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);

                Object value;
                try {
                    value = field.get(target);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }

                String name = field.getName();

                // Check @NotNull
                if (field.isAnnotationPresent(NotNull.class)
                        && value == null) {
                    violations.add(name + ": must not be null");
                }

                // Check @NotBlank
                if (field.isAnnotationPresent(NotBlank.class)
                        && field.getType() == String.class
                        && (value == null
                            || ((String) value).isBlank())) {
                    violations.add(name + ": must not be blank");
                }

                // Check @Range
                Range range = field.getAnnotation(Range.class);

                if (range != null && value != null) {
                    Class<?> type = field.getType();

                    if (type == int.class || type == long.class
                            || type == Integer.class
                            || type == Long.class) {

                        long number = ((Number) value).longValue();

                        if (number < range.min()
                                || number > range.max()) {
                            violations.add(
                                name + ": must between "
                                + range.min() + " and "
                                + range.max()
                            );
                        }
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }

        Collections.sort(violations);

        return violations;
    }

    public static void main(String[] args) {
        Person p1 = new Person("Lulu", 29);
        Person p2 = new Person("", -5);

        System.out.println(validate(p1));
        System.out.println(validate(p2));
    }
}