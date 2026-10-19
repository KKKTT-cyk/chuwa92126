package chuwa92126.Coding.Question_12;

import java.util.*;
import java.util.stream.Collectors;

public class StudentAnalyzer {

    // 1. Get top N student names
    public List<String> getTopStudentNames(
            List<Student> students, int n) {

        return students.stream()
                .sorted(Comparator.comparingDouble(
                        Student::getAverageScore).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }


    // 2. Get average score by major
    public Map<String, Double> getAverageScoreByMajor(
            List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.averagingDouble(
                                Student::getAverageScore)
                ));
    }


    // 3. Find student with highest single score
    public Optional<Student> findStudentWithHighestSingleScore(
            List<Student> students) {

        return students.stream()
                .filter(s -> !s.getScores().isEmpty())
                .max(Comparator.comparingDouble(
                        s -> s.getScores()
                                .stream()
                                .mapToDouble(Double::doubleValue)
                                .max()
                                .orElse(0.0)
                ));
    }


    // 4. Students above their major's average
    public List<Student> getStudentsAboveAverageInMajor(
            List<Student> students, String major) {

        List<Student> majorStudents = students.stream()
                .filter(s -> s.getMajor().equals(major))
                .collect(Collectors.toList());

        double majorAverage = majorStudents.stream()
                .mapToDouble(Student::getAverageScore)
                .average()
                .orElse(0.0);

        return majorStudents.stream()
                .filter(s -> s.getAverageScore() > majorAverage)
                .collect(Collectors.toList());
    }


    // 5. Partition students into pass/fail
    public Map<Boolean, List<Student>> partitionByPassFail(
            List<Student> students, double passingScore) {

        return students.stream()
                .collect(Collectors.partitioningBy(
                        s -> s.getAverageScore() >= passingScore
                ));
    }
}