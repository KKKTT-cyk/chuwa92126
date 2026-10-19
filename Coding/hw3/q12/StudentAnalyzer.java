import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentAnalyzer {

    private double averageScore(Student student) {
        return student.getScores().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(this::averageScore).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                sameMajor -> sameMajor.stream()
                                        .flatMap(student -> student.getScores().stream())
                                        .mapToDouble(Double::doubleValue)
                                        .average()
                                        .orElse(0.0)
                        )
                ));
    }

    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
                .max(Comparator.comparingDouble(student ->
                        student.getScores().stream()
                                .mapToDouble(Double::doubleValue)
                                .max()
                                .orElse(Double.NEGATIVE_INFINITY)
                ));
    }

    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        double majorAverage = students.stream()
                .filter(student -> student.getMajor().equals(major))
                .flatMap(student -> student.getScores().stream())
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return students.stream()
                .filter(student -> student.getMajor().equals(major))
                .filter(student -> averageScore(student) > majorAverage)
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(
            List<Student> students,
            double passingScore) {

        return students.stream()
                .collect(Collectors.partitioningBy(
                        student -> averageScore(student) >= passingScore
                ));
    }
}
