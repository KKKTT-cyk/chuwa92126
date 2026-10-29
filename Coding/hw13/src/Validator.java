import java.util.List;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
public final class Validator {
    private Validator() {
    }
    public static List<String> validate(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("target could not be null");
        }

        List<String> violations = new ArrayList<>();


        for (Class<?> clazz = target.getClass();
             clazz != Object.class;
             clazz = clazz.getSuperclass()) {

            for (Field field : clazz.getDeclaredFields()) {


                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                boolean hasNotNull = field.isAnnotationPresent(NotNull.class);
                boolean hasNotBlank = field.isAnnotationPresent(NotBlank.class);
                Range range = field.getAnnotation(Range.class);


                if (!hasNotNull && !hasNotBlank && range == null) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    Object value = field.get(target);
                    Class<?> type = field.getType();
                    String prefix = field.getName() + ": ";

                    // @NotNull
                    if (hasNotNull && value == null) {
                        violations.add(prefix + "must not be null");
                    }

                    // @NotBlank
                    if (hasNotBlank && type == String.class) {
                        if (value == null || ((String) value).isBlank()) {
                            violations.add(prefix + "must not be blank");
                        }
                    }

                    // @Range
                    boolean supportedNumber =
                            type == int.class
                                    || type == long.class
                                    || type == Integer.class
                                    || type == Long.class;

                    if (range != null && supportedNumber && value != null) {
                        long number = ((Number) value).longValue();

                        if (number < range.min() || number > range.max()) {
                            violations.add(
                                    prefix + "must be between "
                                            + range.min() + " and " + range.max()
                            );
                        }
                    }

                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(
                            "Cannot access field: " + field.getName(), e
                    );
                }
            }
        }

        Collections.sort(violations);
        return violations;
    }
}
