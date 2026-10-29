import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Question6 {

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

    static class Person {
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

    public static final class Validator {
        private Validator() {
        }

        public static List<String> validate(Object target) {
            if (target == null) {
                throw new IllegalArgumentException("target must not be null");
            }

            List<String> violations = new ArrayList<>();

            // getDeclaredFields includes private fields, but not inherited fields.
            for (Class<?> type = target.getClass();
                 type != null && type != Object.class;
                 type = type.getSuperclass()) {
                for (Field field : type.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    boolean notNull = field.isAnnotationPresent(NotNull.class);
                    boolean notBlank = field.isAnnotationPresent(NotBlank.class)
                            && field.getType() == String.class;
                    Range range = field.getAnnotation(Range.class);
                    Class<?> fieldType = field.getType();
                    boolean checkRange = range != null
                            && (fieldType == int.class || fieldType == Integer.class
                            || fieldType == long.class || fieldType == Long.class);

                    if (!notNull && !notBlank && !checkRange) {
                        continue;
                    }

                    Object value;
                    try {
                        field.setAccessible(true);
                        value = field.get(target);
                    } catch (IllegalAccessException ex) {
                        throw new IllegalStateException("Cannot read field: " + field, ex);
                    }

                    String prefix = field.getName() + ": ";
                    // Independent checks allow several violations for one field.
                    if (notNull && value == null) {
                        violations.add(prefix + "must not be null");
                    }
                    if (notBlank && (value == null || ((String) value).isBlank())) {
                        violations.add(prefix + "must not be blank");
                    }
                    if (checkRange && value != null) {
                        long number = ((Number) value).longValue();
                        if (number < range.min() || number > range.max()) {
                            violations.add(prefix + "must be between "
                                    + range.min() + " and " + range.max());
                        }
                    }
                }
            }

            violations.sort(String::compareTo);
            return violations;
        }
    }
}
