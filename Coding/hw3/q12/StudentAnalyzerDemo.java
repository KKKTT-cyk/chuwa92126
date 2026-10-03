import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentAnalyzerDemo {
    public static void main(String[] args) {
        Student s1 = new Student("S001", "Alice", 21, "CS", Arrays.asList(95.0, 91.0, 93.0));
        Student s2 = new Student("S002", "Bob", 22, "CS", Arrays.asList(82.0, 85.0, 80.0));
        Student s3 = new Student("S003", "Carol", 20, "Math", Arrays.asList(98.0, 96.0, 99.0));
        Student s4 = new Student("S004", "David", 23, "Math", Arrays.asList(75.0, 78.0, 80.0));
        Student s5 = new Student("S005", "Eva", 21, "Physics", Arrays.asList(88.0, 90.0, 86.0));
        Student s6 = new Student("S006", "Frank", 22, "Physics", Arrays.asList(65.0, 70.0, 68.0));

        List<Student> students = Arrays.asList(s1, s2, s3, s4, s5, s6);
        StudentAnalyzer analyzer = new StudentAnalyzer();

        System.out.println("Top 3 students:");
        analyzer.getTopStudentNames(students, 3)
                .forEach(System.out::println);

        System.out.println("\nAverage score by major:");
        Map<String, Double> averageByMajor = analyzer.getAverageScoreByMajor(students);
        averageByMajor.forEach((major, average) ->
                System.out.println(major + " -> " + average));

        System.out.println("\nStudent with highest single score:");
        Optional<Student> highest = analyzer.findStudentWithHighestSingleScore(students);
        highest.ifPresent(System.out::println);

        System.out.println("\nCS students above their major average:");
        analyzer.getStudentsAboveAverageInMajor(students, "CS")
                .forEach(System.out::println);

        System.out.println("\nPartition by pass/fail (passing = 80):");
        Map<Boolean, List<Student>> partition = analyzer.partitionByPassFail(students, 80.0);
        System.out.println("Passed: " + partition.get(true));
        System.out.println("Failed: " + partition.get(false));
    }
}
