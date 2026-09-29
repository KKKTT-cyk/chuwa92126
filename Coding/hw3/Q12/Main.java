import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        Student student1 = new Student(
                "S1", "Amy", 20, "CS",
                Arrays.asList(90.0, 85.0, 95.0)
        );

        Student student2 = new Student(
                "S2", "Becky", 21, "CS",
                Arrays.asList(70.0, 75.0, 80.0)
        );

        Student student3 = new Student(
                "S3", "Cassy", 19, "Math",
                Arrays.asList(88.0, 92.0, 90.0)
        );

        Student student4 = new Student(
                "S4", "Dave", 22, "Math",
                Arrays.asList(60.0, 65.0, 70.0)
        );

        Student student5 = new Student(
                "S5", "Emily", 21, "Physics",
                Arrays.asList(95.0, 98.0, 100.0)
        );

        Student student6 = new Student(
                "S6", "Frank", 20, "Physics",
                Arrays.asList(75.0, 80.0, 85.0)
        );

        List<Student> students = Arrays.asList(
                student1,
                student2,
                student3,
                student4,
                student5,
                student6
        );

        StudentAnalyzer analyzer = new StudentAnalyzer();

        // Top n
        List<String> topStudents =
                analyzer.getTopStudentNames(students, 3);

        System.out.println("Top 3 students:");
        topStudents.forEach(System.out::println);

        // Average score by major
        Map<String, Double> averageByMajor =
                analyzer.getAverageScoreByMajor(students);

        System.out.println("\nAverage score by major:");
        averageByMajor.forEach((major, average) ->
                System.out.println(major + ": " + average)
        );

        // Student w/ highest single score
        Optional<Student> highest =
                analyzer.findStudentWithHighestSingleScore(students);

        highest.ifPresent(student ->
                System.out.println(
                        "\nStudent with highest single score: "
                                + student.getName()
                )
        );

        List<Student> aboveAverage =
                analyzer.getStudentsAboveAverageInMajor(students, "CS");

        System.out.println("\nStudents above CS average:");
        aboveAverage.stream()
                .map(Student::getName)
                .forEach(System.out::println);

        // Pass / Fail
        Map<Boolean, List<Student>> passFail =
                analyzer.partitionByPassFail(students, 75.0);

        System.out.println("\nPassed:");
        passFail.get(true).stream()
                .map(Student::getName)
                .forEach(System.out::println);

        System.out.println("\nFailed:");
        passFail.get(false).stream()
                .map(Student::getName)
                .forEach(System.out::println);
    }
}