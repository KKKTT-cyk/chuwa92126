package chuwa92126.Coding;

import java.util.*;
import java.util.stream.Collectors;


// Student class
class Student {

    private String id;
    private String name;
    private int age;
    private String major;
    private List<Double> scores;

    public Student(String id, String name, int age,
                   String major, List<Double> scores) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMajor() {
        return major;
    }

    public List<Double> getScores() {
        return scores;
    }

    // Calculate average score
    public double getAverageScore() {
        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    // Find highest score
    public double getHighestScore() {
        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return name;
    }
}


// StudentAnalyzer class
class StudentAnalyzer {

    // 1. Get top N students
    public List<String> getTopStudentNames(
            List<Student> students,
            int n) {

        return students.stream()
                .sorted(
                        Comparator.comparingDouble(
                                Student::getAverageScore
                        ).reversed()
                )
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }


    // 2. Get average score by major
    public Map<String, Double> getAverageScoreByMajor(
            List<Student> students) {

        return students.stream()
                .collect(
                        Collectors.groupingBy(
                                Student::getMajor,
                                Collectors.averagingDouble(
                                        Student::getAverageScore
                                )
                        )
                );
    }


    // 3. Find student with highest single score
    public Optional<Student> findStudentWithHighestSingleScore(
            List<Student> students) {

        return students.stream()
                .max(
                        Comparator.comparingDouble(
                                Student::getHighestScore
                        )
                );
    }


    // 4. Find students above average in their major
    public List<Student> getStudentsAboveAverageInMajor(
            List<Student> students,
            String major) {

        double majorAverage =
                students.stream()
                        .filter(
                                student ->
                                        student.getMajor().equals(major)
                        )
                        .mapToDouble(Student::getAverageScore)
                        .average()
                        .orElse(0.0);

        return students.stream()
                .filter(
                        student ->
                                student.getMajor().equals(major)
                )
                .filter(
                        student ->
                                student.getAverageScore() > majorAverage
                )
                .collect(Collectors.toList());
    }


    // 5. Partition students by pass/fail
    public Map<Boolean, List<Student>> partitionByPassFail(
            List<Student> students,
            double passingScore) {

        return students.stream()
                .collect(
                        Collectors.partitioningBy(
                                student ->
                                        student.getAverageScore()
                                                >= passingScore
                        )
                );
    }
}


// Main class
public class hw3_question12 {

    public static void main(String[] args) {

        // Create 6 students
        Student s1 = new Student(
                "S1",
                "Alice",
                20,
                "CS",
                Arrays.asList(90.0, 95.0, 92.0)
        );

        Student s2 = new Student(
                "S2",
                "Bob",
                21,
                "CS",
                Arrays.asList(80.0, 85.0, 82.0)
        );

        Student s3 = new Student(
                "S3",
                "Charlie",
                22,
                "Math",
                Arrays.asList(96.0, 94.0, 98.0)
        );

        Student s4 = new Student(
                "S4",
                "David",
                20,
                "Math",
                Arrays.asList(75.0, 78.0, 80.0)
        );

        Student s5 = new Student(
                "S5",
                "Emma",
                21,
                "Physics",
                Arrays.asList(88.0, 90.0, 91.0)
        );

        Student s6 = new Student(
                "S6",
                "Frank",
                22,
                "Physics",
                Arrays.asList(65.0, 70.0, 68.0)
        );

        List<Student> students =
                Arrays.asList(
                        s1, s2, s3,
                        s4, s5, s6
                );

        StudentAnalyzer analyzer =
                new StudentAnalyzer();


        // 1. Top 3 students
        System.out.println("Top 3 students:");

        analyzer.getTopStudentNames(students, 3)
                .forEach(System.out::println);


        // 2. Average score by major
        System.out.println("\nAverage score by major:");

        analyzer.getAverageScoreByMajor(students)
                .forEach(
                        (major, average) ->
                                System.out.println(
                                        major + ": " + average
                                )
                );


        // 3. Highest single score
        System.out.println(
                "\nStudent with highest single score:"
        );

        analyzer.findStudentWithHighestSingleScore(students)
                .ifPresent(System.out::println);


        // 4. Students above CS average
        System.out.println(
                "\nStudents above CS average:"
        );

        analyzer.getStudentsAboveAverageInMajor(
                        students,
                        "CS"
                )
                .forEach(System.out::println);


        // 5. Pass / Fail
        System.out.println("\nPass / Fail:");

        Map<Boolean, List<Student>> result =
                analyzer.partitionByPassFail(
                        students,
                        80.0
                );

        System.out.println(
                "Passed: " + result.get(true)
        );

        System.out.println(
                "Failed: " + result.get(false)
        );
    }
}
