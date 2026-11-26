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
        // Walk up from the object's own class to its superclasses, but not Object itself
        for (Class<?> type = target.getClass(); type != null && type != Object.class; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;                                  // static fields are ignored
                }
                checkField(target, field, violations);
            }
        }
        Collections.sort(violations);                          // natural String order
        return violations;
    }

    private static void checkField(Object target, Field field, List<String> violations) {
        boolean checkNotNull = field.isAnnotationPresent(NotNull.class);
        boolean checkNotBlank = field.isAnnotationPresent(NotBlank.class) && field.getType() == String.class;
        Range range = isRangeType(field.getType()) ? field.getAnnotation(Range.class) : null;
        if (!checkNotNull && !checkNotBlank && range == null) {
            return;                                            // nothing to check: do not touch the field
        }

        Object value = readValue(target, field);
        String name = field.getName();

        if (checkNotNull && value == null) {
            violations.add(name + ": must not be null");
        }
        if (checkNotBlank && (value == null || ((String) value).isBlank())) {
            violations.add(name + ": must not be blank");
        }
        if (range != null && value != null) {                  // a null wrapper value is not checked by @Range
            long number = ((Number) value).longValue();
            if (number < range.min() || number > range.max()) {
                violations.add(name + ": must be between " + range.min() + " and " + range.max());
            }
        }
    }

    private static boolean isRangeType(Class<?> type) {
        return type == int.class || type == long.class || type == Integer.class || type == Long.class;
    }

    private static Object readValue(Object target, Field field) {
        try {
            field.setAccessible(true);                         // fields can be private
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read field " + field.getName(), e);
        }
    }
}
