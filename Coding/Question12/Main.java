package Coding.Question12;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StudentAnalyzer analyzer = new StudentAnalyzer();

        List<Student> students = Arrays.asList(
                new Student("S1", "Alice", 20, "CS",   Arrays.asList(95.0, 88.0, 92.0)),
                new Student("S2", "Bob",   21, "CS",   Arrays.asList(70.0, 65.0, 80.0)),
                new Student("S3", "Carol", 22, "Math", Arrays.asList(85.0, 90.0, 78.0)),
                new Student("S4", "David", 20, "Math", Arrays.asList(55.0, 60.0, 58.0)),
                new Student("S5", "Eve",   23, "Art",  Arrays.asList(99.0, 70.0, 75.0)),
                new Student("S6", "Frank", 21, "Art",  Arrays.asList(50.0, 45.0, 62.0))
        );

        System.out.println(analyzer.getTopStudentNames(students, 3));
        System.out.println(analyzer.getAverageScoreByMajor(students));
        analyzer.findStudentWithHighestSingleScore(students).ifPresent(System.out::println);
        System.out.println(analyzer.getStudentsAboveAverageInMajor(students, "CS"));
        System.out.println(analyzer.partitionByPassFail(students, 60.0));
    }
}