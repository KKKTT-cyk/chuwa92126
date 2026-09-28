package org.tiff.homework2.question12;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentAnalyzer {
    public double getAverageScore(Student student) {
        return student.getScores().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(this::getAverageScore).reversed())
                .limit(Math.max(0, n))
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        // Each student has equal weight, even if score counts differ.
        return students.stream()
                .collect(Collectors.groupingBy(Student::getMajor,
                        Collectors.averagingDouble(this::getAverageScore)));
    }

    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
                .filter(student -> !student.getScores().isEmpty())
                .max(Comparator.comparingDouble(student -> student.getScores().stream()
                        .mapToDouble(Double::doubleValue)
                        .max()
                        .orElse(Double.NEGATIVE_INFINITY)));
    }

    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        List<Student> sameMajor = students.stream()
                .filter(student -> major.equals(student.getMajor()))
                .collect(Collectors.toList());

        double majorAverage = sameMajor.stream()
                .mapToDouble(this::getAverageScore)
                .average()
                .orElse(0.0);

        return sameMajor.stream()
                .filter(student -> getAverageScore(student) > majorAverage)
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(
                        student -> getAverageScore(student) >= passingScore));
    }
}
