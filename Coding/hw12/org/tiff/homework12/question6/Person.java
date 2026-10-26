package org.tiff.homework12.question6;

public class Person {
    @NotBlank
    private String name;

    @NotNull
    @Range(min = 0, max = 150)
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
