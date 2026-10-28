import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Validator {
    private Validator() {}

    public static List<String> validate(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }

        List<String> violations = new ArrayList<>();
        for (Class<?> type = target.getClass(); type != null && type != Object.class;
             type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (!field.isAnnotationPresent(NotNull.class)
                        && !field.isAnnotationPresent(NotBlank.class)
                        && !field.isAnnotationPresent(Range.class)) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    Object value = field.get(target);
                    String name = field.getName();

                    if (field.isAnnotationPresent(NotNull.class) && value == null) {
                        violations.add(name + ": must not be null");
                    }
                    if (field.isAnnotationPresent(NotBlank.class)
                            && field.getType() == String.class
                            && (value == null || ((String) value).trim().isEmpty())) {
                        violations.add(name + ": must not be blank");
                    }
                    if (field.isAnnotationPresent(Range.class) && value != null
                            && (field.getType() == int.class || field.getType() == long.class
                                || field.getType() == Integer.class || field.getType() == Long.class)) {
                        Range range = field.getAnnotation(Range.class);
                        long number = ((Number) value).longValue();
                        if (number < range.min() || number > range.max()) {
                            violations.add(name + ": must be between " + range.min() + " and " + range.max());
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot access field: " + field.getName(), e);
                }
            }
        }
        Collections.sort(violations);
        return violations;
    }
}
