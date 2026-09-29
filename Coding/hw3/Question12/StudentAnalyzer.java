import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentAnalyzer {

    // Names of the top n students by average score, in descending order
    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(this::averageScore).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    // Major -> average score of the students in that major
    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getMajor,
                        Collectors.averagingDouble(this::averageScore)));
    }

    // Student who has the highest single score among all their scores
    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
                .max(Comparator.comparingDouble(this::highestScore));
    }

    // Students in the given major whose average score is above the major's average
    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        Double majorAverage = getAverageScoreByMajor(students).get(major);
        if (majorAverage == null) {
            return Collections.emptyList();
        }
        return students.stream()
                .filter(student -> student.getMajor().equals(major))
                .filter(student -> averageScore(student) > majorAverage)
                .collect(Collectors.toList());
    }

    // true -> passed (average >= passingScore), false -> failed
    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(student -> averageScore(student) >= passingScore));
    }

    private double averageScore(Student student) {
        return student.getScores().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private double highestScore(Student student) {
        return student.getScores().stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);
    }
}
