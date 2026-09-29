import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("S001", "Alice", 20, "Computer Science", Arrays.asList(95.0, 88.0, 92.0)),
                new Student("S002", "Bob", 21, "Computer Science", Arrays.asList(72.0, 65.0, 80.0)),
                new Student("S003", "Carol", 22, "Mathematics", Arrays.asList(85.0, 99.0, 78.0)),
                new Student("S004", "David", 20, "Mathematics", Arrays.asList(55.0, 60.0, 58.0)),
                new Student("S005", "Emma", 23, "Physics", Arrays.asList(90.0, 91.0, 89.0)),
                new Student("S006", "Frank", 21, "Physics", Arrays.asList(45.0, 70.0, 62.0)),
                new Student("S007", "Grace", 22, "Computer Science", Arrays.asList(88.0, 82.0, 90.0))
        );

        StudentAnalyzer analyzer = new StudentAnalyzer();

        System.out.println("--- Top 3 students ---");
        analyzer.getTopStudentNames(students, 3).forEach(System.out::println);

        System.out.println("--- Average score by major ---");
        Map<String, Double> averageByMajor = analyzer.getAverageScoreByMajor(students);
        averageByMajor.forEach((major, average) -> System.out.printf("%s: %.2f%n", major, average));

        System.out.println("--- Student with highest single score ---");
        analyzer.findStudentWithHighestSingleScore(students)
                .map(Student::getName)
                .ifPresent(System.out::println);

        System.out.println("--- Computer Science students above major average ---");
        analyzer.getStudentsAboveAverageInMajor(students, "Computer Science").stream()
                .map(Student::getName)
                .forEach(System.out::println);

        System.out.println("--- Pass/Fail (passing score 70) ---");
        Map<Boolean, List<Student>> passFail = analyzer.partitionByPassFail(students, 70);
        System.out.println("Passed: " + passFail.get(true).stream()
                .map(Student::getName).collect(Collectors.toList()));
        System.out.println("Failed: " + passFail.get(false).stream()
                .map(Student::getName).collect(Collectors.toList()));

        System.out.println("--- Optional with an empty list ---");
        String name = analyzer.findStudentWithHighestSingleScore(Collections.emptyList())
                .map(Student::getName)
                .orElse("No students");
        System.out.println(name);
    }
}
