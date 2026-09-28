package com.lant.hw3;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String id;
    private String name;
    private int age;
    private String major;
    private List<Double> scores;

    public Student(String id, String name, int age, String major, List<Double> scores) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public List<Double> getScores() {
        return scores;
    }

    public void setScores(List<Double> scores) {
        this.scores = scores;
    }
}

class StudentAnalyzer {
    List<String> getTopStudentNames(List<Student> students, int n) {
        Map<Student, Double> studentAvgScore = students.stream()
                .collect(Collectors.toMap(s -> s,
                        s -> s.getScores().stream().mapToDouble(Double::doubleValue).average().orElse(0)
                ));
        return studentAvgScore.keySet().stream().sorted(Comparator.comparingDouble(
                (Student s) -> studentAvgScore.get(s)).reversed()).limit(n).map(Student::getName).collect(Collectors.toList());
    }

    Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getMajor,
                        Collectors.averagingDouble(s -> s.getScores().stream().mapToDouble(Double::doubleValue).average().orElse(0))));
    }

    Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream().max(
                Comparator.comparingDouble(s -> s.getScores().stream().mapToDouble(Double::doubleValue).max().orElse(0))
        );
    }

    List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        Map<String, Double> averageScoreByMajor = getAverageScoreByMajor(students);
        return students.stream()
                .filter(s ->s.getMajor().equals(major)).filter(
                        s -> s.getScores().stream().mapToDouble(Double::doubleValue).average().orElse(0) > averageScoreByMajor.get(s.getMajor())
                )
                .collect(Collectors.toList());

    }
    Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream().collect(
                Collectors.partitioningBy(s -> s.getScores().stream().mapToDouble(Double::doubleValue).average().orElse(0) >= passingScore)
        );
    }
}

public class Lambda_Student {
    public static void main(String[] args) {
        List<Student> students = List.of(
                new Student("S001", "Alice", 20, "Computer Science", List.of(90.0, 90.0, 90.0, 90.0)),
                new Student("S002", "Bob", 21, "Computer Science", List.of(60.0)),
                new Student("S003", "Charlie", 22, "Computer Science", List.of(100.0, 50.0)),
                new Student("S004", "Diana", 20, "Mathematics", List.of(90.0, 95.0, 85.0)),
                new Student("S005", "Ethan", 23, "Mathematics", List.of(55.0, 62.0, 58.0)),
                new Student("S006", "Fiona", 21, "Physics", List.of(60.0, 60.0, 60.0)),
                new Student("S007", "George", 22, "Physics", List.of(88.0, 95.0)),
                new Student("S008", "Hannah", 19, "Physics", List.of())
        );
        StudentAnalyzer analyzer = new StudentAnalyzer();


        System.out.println("\n=== average score by major ===");
        analyzer.getAverageScoreByMajor(students)
                .forEach((major, avg) -> System.out.printf("%-17s %6.2f%n", major, avg));

        System.out.println("\n=== student above average score by major ===");
        analyzer.getStudentsAboveAverageInMajor(students, "Computer Science")
                .forEach(s -> System.out.println(s.getName()));

        System.out.println("\n=== average score top 3 ===");
        analyzer.getTopStudentNames(students, 3)
                .forEach(System.out::println);

        System.out.println("\n=== highest score ===");
        analyzer.findStudentWithHighestSingleScore(students)
                .ifPresent(s -> System.out.println(s.getName() + " " + s.getScores()));

        System.out.println("\n=== pass score 60 ===");
        Map<Boolean, List<Student>> result = analyzer.partitionByPassFail(students, 60.0);
        System.out.println("Passed: " + result.get(true).stream().map(Student::getName).toList());
        System.out.println("Failed: " + result.get(false).stream().map(Student::getName).toList());
    }
}
