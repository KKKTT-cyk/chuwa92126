/*
Complete Validator.validate, which checks an object against the validation annotations on its fields
using reflection. @NotNull fails when the field value is null, with the message must not be null.
@NotBlank applies to String fields and fails when the value is null or contains only whitespace, with
the message must not be blank. @Range applies to int and long fields (including the wrapper types)
and fails when the value is less than min or greater than max, with the message must be between
<min> and <max>; a null wrapper value is not checked by @Range. Every violation is reported as
<fieldName>: <message>. A field can carry several annotations and then can produce several
violations. The fields of all superclasses (up to but not including Object) are checked as well as the
fields of the object's own class, and fields can be private. Static fields are ignored. The returned list
contains every violation and is sorted alphabetically (natural String order). The method throws
IllegalArgumentException if target is null.
*/

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