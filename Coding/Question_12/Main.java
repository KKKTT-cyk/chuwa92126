package chuwa92126.Coding.Question_12;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        StudentAnalyzer analyzer = new StudentAnalyzer();

        // Create 6 students
        Student s1 = new Student(
                "S1", "Alice", 20, "Computer Science",
                Arrays.asList(90.0, 94.0, 87.5));

        Student s2 = new Student(
                "S2", "Bob", 21, "Computer Science",
                Arrays.asList(72.0, 83.0, 75.0));

        Student s3 = new Student(
                "S3", "Cathy", 22, "Biology",
                Arrays.asList(93.0, 86.0, 92.0));

        Student s4 = new Student(
                "S4", "David", 20, "Biology",
                Arrays.asList(73.0, 77.0, 78.0));

        Student s5 = new Student(
                "S5", "Emma", 23, "Math",
                Arrays.asList(95.0, 98.0, 92.0));

        Student s6 = new Student(
                "S6", "Frank", 21, "Math",
                Arrays.asList(83.0, 84.0, 80.0));

        List<Student> students =
                Arrays.asList(s1, s2, s3, s4, s5, s6);


        // Top 3 students
        System.out.println("Top 3 students:");

        List<String> topStudents =
                analyzer.getTopStudentNames(students, 3);

        topStudents.forEach(System.out::println);


        // Average score by major
        System.out.println("\nAverage score by major:");

        Map<String, Double> averages =
                analyzer.getAverageScoreByMajor(students);

        averages.forEach((major, average) ->
                System.out.println(
                        major + ": " + average
                )
        );


        // Student with highest single score
        System.out.println("\nHighest single score:");

        Optional<Student> highest =
                analyzer.findStudentWithHighestSingleScore(students);

        highest.ifPresent(student ->
                System.out.println(student.getName())
        );


        // Students above CS major average
        System.out.println(
                "\nAbove average Computer Science students:"
        );

        List<Student> aboveAverage =
                analyzer.getStudentsAboveAverageInMajor(
                        students,
                        "Computer Science"
                );

        aboveAverage.forEach(
                student ->
                        System.out.println(student.getName())
        );


        // Pass / Fail
        System.out.println("\nPass / Fail:");

        Map<Boolean, List<Student>> result =
                analyzer.partitionByPassFail(
                        students,
                        80.0
                );

        System.out.println("Passed: " + result.get(true));
        System.out.println("Failed: " + result.get(false));
    }
}