import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
    long max(); }
class Person {
    @NotBlank
    private String name;
    @NotNull
    @Range(min = 0, max = 150)
    private Integer age;
    Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    } }
public final class Validator {
    private Validator() {
    }

    public static List<String> validate(Object target) {
        if (target == null) {
            throw new IllegalArgumentException(
                    "target must not be null");
        }

        List<String> violations = new ArrayList<>();

        // Traverse the class and all its superclasses.
        for (Class<?> type = target.getClass();
             type != null && type != Object.class;
             type = type.getSuperclass()) {

            for (Field field : type.getDeclaredFields()) {

                // Ignore static fields.
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                NotNull notNull =
                        field.getAnnotation(NotNull.class);
                NotBlank notBlank =
                        field.getAnnotation(NotBlank.class);
                Range range =
                        field.getAnnotation(Range.class);

                // Skip fields without validation annotations.
                if (notNull == null
                        && notBlank == null
                        && range == null) {
                    continue;
                }

                try {
                    // Allow access to private fields.
                    field.setAccessible(true);

                    Object value = field.get(target);
                    String fieldName = field.getName();

                    // Validate @NotNull.
                    if (notNull != null && value == null) {
                        violations.add(
                                fieldName + ": must not be null");
                    }

                    // Validate @NotBlank on String fields.
                    if (notBlank != null
                            && field.getType() == String.class
                            && (value == null
                            || ((String) value).isBlank())) {
                        violations.add(
                                fieldName + ": must not be blank");
                    }

                    // Validate @Range on int/long types.
                    Class<?> fieldType = field.getType();

                    boolean supportedNumericType =
                            fieldType == int.class
                                    || fieldType == Integer.class
                                    || fieldType == long.class
                                    || fieldType == Long.class;

                    if (range != null
                            && supportedNumericType
                            && value != null) {

                        long number =
                                ((Number) value).longValue();

                        if (number < range.min()
                                || number > range.max()) {

                            violations.add(
                                    fieldName
                                            + ": must be between "
                                            + range.min()
                                            + " and "
                                            + range.max());
                        }
                    }

                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(
                            "Unable to read field: "
                                    + field.getName(), e);
                }
            }
        }

        // Sort messages alphabetically.
        Collections.sort(violations);

        return violations;
    }

}