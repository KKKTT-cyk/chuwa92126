package Question12;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentAnalyzer {
  // Helper function to get a student's average score
  public double getAverageScore(Student student) {
    if (student == null || student.getScores() == null || student.getScores().isEmpty()) {
      return 0.0;
    }
    return student.getScores().stream()
        .mapToDouble(Double::doubleValue)
        .average()
        .orElse(0.0);
  }

  // Helper function to find highest single score of a student
  public double getMaxScore(Student student) {
    if (student == null || student.getScores() == null || student.getScores().isEmpty()) {
      return 0.0;
    }
    return student.getScores().stream()
        .mapToDouble(Double::doubleValue)
        .max()
        .orElse(0.0);
  }

  List<String> getTopStudentNames(List<Student> students, int n) {
    if (students == null || students.isEmpty() || n <= 0) {
      return Collections.emptyList();
    }
    return students.stream()
        .sorted(Comparator.comparingDouble(this::getAverageScore).reversed())
        .limit(n)
        .map(Student::getName)
        .collect(Collectors.toList());
  }

  Map<String, Double> getAverageScoreByMajor(List<Student> students) {
    if (students == null || students.isEmpty()) {
      return Collections.emptyMap();
    }
    return students.stream()
        .collect(Collectors.groupingBy(
            Student::getMajor,
            Collectors.averagingDouble(this::getAverageScore)
        ));
  }

  Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
    if (students == null || students.isEmpty()) {
      return Optional.empty();
    }
    return students.stream()
        .max(Comparator.comparingDouble(this::getMaxScore));
  }

  List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
    if (students == null || students.isEmpty() || major == null) {
      return Collections.emptyList();
    }
    double majorAverage = getAverageScoreByMajor(students)
        .getOrDefault(major, 0.0);

    return students.stream()
        .filter(s -> s.getMajor().equals(major))
        .filter(s -> getAverageScore(s) > majorAverage)
        .collect(Collectors.toList());
  }

  Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
    if (students == null) {
      students = Collections.emptyList();
    }
    return students.stream()
        .collect(Collectors.partitioningBy(s -> getAverageScore(s) >= passingScore));
  }
}
