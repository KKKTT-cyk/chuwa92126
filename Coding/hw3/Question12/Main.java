package Question12;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
  public static void main(String[] args) {
    List<Student> students = Arrays.asList(
        new Student("S101", "Alice", 20, "Computer Science", Arrays.asList(95.0, 92.0, 98.0)),
        new Student("S102", "Bob", 21, "Computer Science", Arrays.asList(88.0, 75.0, 82.0)),
        new Student("S103", "Charlie", 22, "Computer Science", Arrays.asList(60.0, 65.0, 70.0)),
        new Student("S104", "Diana", 19, "Mathematics", Arrays.asList(99.0, 100.0, 94.0)),
        new Student("S105", "Evan", 20, "Mathematics", Arrays.asList(70.0, 72.0, 68.0)),
        new Student("S106", "Fiona", 21, "Physics", Arrays.asList(85.0, 90.0, 88.0))
    );

    StudentAnalyzer analyzer = new StudentAnalyzer();

    // 1. Top N student names
    System.out.println("=== 1. Top 3 Students by Average Score ===");
    List<String> top3Names = analyzer.getTopStudentNames(students, 3);
    top3Names.forEach(System.out::println);

    // 2. Average score by major
    System.out.println("\n=== 2. Average Score by Major ===");
    Map<String, Double> avgByMajor = analyzer.getAverageScoreByMajor(students);
    avgByMajor.forEach((major, avg) ->
        System.out.printf("Major: %-18s | Average Score: %.2f%n", major, avg)
    );

    // 3. Student with the highest single score (Optional handling)
    System.out.println("\n=== 3. Student with Highest Single Score ===");
    Optional<Student> highestSingleScoreOpt =
        analyzer.findStudentWithHighestSingleScore(students);

    String result = highestSingleScoreOpt
        .map(student -> String.format(
            "%s with max score of %.1f",
            student.getName(),
            analyzer.getMaxScore(student)
        ))
        .orElse("No students found");
    System.out.println(result);

    // 4. Students above major average
    System.out.println("\n=== 4. Students Above Major Average (Computer Science) ===");
    List<Student> csAboveAvg =
        analyzer.getStudentsAboveAverageInMajor(students, "Computer Science");
    csAboveAvg.forEach(s ->
        System.out.printf("  - %s (Avg: %.2f)%n", s.getName(), analyzer.getAverageScore(s))
    );

    // 5. Partition by pass/fail threshold
    System.out.println("\n=== 5. Partition by Pass/Fail (Threshold = 75.0) ===");
    Map<Boolean, List<Student>> passFailMap = analyzer.partitionByPassFail(students, 75.0);

    System.out.println("PASSED (>= 75.0):");
    passFailMap.get(true).forEach(s ->
        System.out.printf("  - %s (Avg: %.2f)%n", s.getName(), analyzer.getAverageScore(s))
    );

    System.out.println("FAILED (< 75.0):");
    passFailMap.get(false).forEach(s ->
        System.out.printf("  - %s (Avg: %.2f)%n", s.getName(), analyzer.getAverageScore(s))
    );
  }
}
