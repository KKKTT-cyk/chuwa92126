package org.tiff.homework12.question6;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Validator {
    private Validator() {
    }

    public static List<String> validate(Object target) {
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }
        List<String> violations = new ArrayList<>();
        for (Class<?> type = target.getClass(); type != Object.class; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                NotNull notNull = field.getAnnotation(NotNull.class);
                NotBlank notBlank = field.getAnnotation(NotBlank.class);
                Range range = field.getAnnotation(Range.class);
                if (notNull == null && notBlank == null && range == null) {
                    continue;
                }

                Object value;
                try {
                    field.setAccessible(true);
                    value = field.get(target);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot read field " + field.getName(), e);
                }

                String prefix = field.getName() + ": ";
                if (notNull != null && value == null) {
                    violations.add(prefix + "must not be null");
                }
                if (notBlank != null && field.getType() == String.class
                        && (value == null || ((String) value).isBlank())) {
                    violations.add(prefix + "must not be blank");
                }
                Class<?> fieldType = field.getType();
                boolean integerType = fieldType == int.class || fieldType == Integer.class
                        || fieldType == long.class || fieldType == Long.class;
                if (range != null && integerType && value != null) {
                    long number = ((Number) value).longValue();
                    if (number < range.min() || number > range.max()) {
                        violations.add(prefix + "must be between " + range.min() + " and " + range.max());
                    }
                }
            }
        }
        Collections.sort(violations);
        return violations;
    }
}
