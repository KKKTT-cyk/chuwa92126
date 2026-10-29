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
class Validator {
    public static List<String> validate(Object target) {
        if (target == null) throw new IllegalArgumentException();

        List<String> errors = new ArrayList<>();

        for (Class<?> c = target.getClass();
             c != null && c != Object.class;
             c = c.getSuperclass()) {

            for (Field f : c.getDeclaredFields()) {
                if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
                if (!f.isAnnotationPresent(NotNull.class)
                        && !f.isAnnotationPresent(NotBlank.class)
                        && !f.isAnnotationPresent(Range.class)) continue;

                f.setAccessible(true);
                Object v;
                try {
                    v = f.get(target);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }

                String prefix = f.getName() + ": ";

                if (f.isAnnotationPresent(NotNull.class) && v == null)
                    errors.add(prefix + "must not be null");

                if (f.isAnnotationPresent(NotBlank.class)
                        && f.getType() == String.class
                        && (v == null || ((String) v).isBlank()))
                    errors.add(prefix + "must not be blank");

                Range r = f.getAnnotation(Range.class);
                Class<?> t = f.getType();
                if (r != null && v != null
                        && (t == int.class || t == Integer.class
                        || t == long.class || t == Long.class)) {
                    long n = ((Number) v).longValue();
                    if (n < r.min() || n > r.max())
                        errors.add(prefix + "must be between "
                                + r.min() + " and " + r.max());
                }
            }
        }

        errors.sort(null);
        return errors;
    }
}


public class Question6 {

}
