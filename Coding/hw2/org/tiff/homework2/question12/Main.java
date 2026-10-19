package org.tiff.homework2.question12;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("S1", "Amy", 20, "CS", Arrays.asList(90.0, 80.0, 100.0)),
                new Student("S2", "Ben", 21, "CS", Arrays.asList(70.0, 80.0, 90.0)),
                new Student("S3", "Cara", 20, "Math", Arrays.asList(85.0, 95.0, 90.0)),
                new Student("S4", "Dan", 22, "Math", Arrays.asList(50.0, 60.0, 70.0)),
                new Student("S5", "Eva", 21, "Business", Arrays.asList(75.0, 80.0, 85.0)),
                new Student("S6", "Finn", 19, "Business", Arrays.asList(40.0, 50.0, 60.0))
        );
        StudentAnalyzer analyzer = new StudentAnalyzer();

        System.out.println("Top 3 students:");
        analyzer.getTopStudentNames(students, 3).forEach(System.out::println);

        System.out.println("Average score by major:");
        analyzer.getAverageScoreByMajor(students)
                .forEach((major, average) -> System.out.println(major + ": " + average));

        Optional<Student> highest = analyzer.findStudentWithHighestSingleScore(students);
        System.out.println("Student with highest single score: "
                + highest.map(Student::getName).orElse("No scores"));

        System.out.println("CS students above their major average:");
        analyzer.getStudentsAboveAverageInMajor(students, "CS").forEach(System.out::println);

        Map<Boolean, List<Student>> results = analyzer.partitionByPassFail(students, 60.0);
        System.out.println("Passed: " + results.get(true));
        System.out.println("Failed: " + results.get(false));
    }
}
