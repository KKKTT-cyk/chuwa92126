package Coding.Question12;

import java.util.*;
import java.util.stream.Collectors;

public class StudentAnalyzer {

    private static double avg(Student s) {
        return s.getScores().stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(StudentAnalyzer::avg).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getMajor, Collectors.averagingDouble(StudentAnalyzer::avg)));
    }

    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
                .max(Comparator.comparingDouble(s -> Collections.max(s.getScores())));
    }

    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        double majorAvg = getAverageScoreByMajor(students).get(major);
        return students.stream()
                .filter(s -> s.getMajor().equals(major) && avg(s) > majorAvg)
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(s -> avg(s) >= passingScore));
    }
}