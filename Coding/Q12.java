import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String id;
    private String name;
    private int age;
    private String major;
    private List<Double> scores;

    public Student(String id, String name, int age,
                   String major, List<Double> scores) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getMajor() { return major; }
    public List<Double> getScores() { return scores; }

    @Override
    public String toString() {
        return name + " (" + major + ", scores=" + scores + ")";
    }
}

class StudentAnalyzer {
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
                .orElse(Double.NEGATIVE_INFINITY);
    }

    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(
                        this::averageScore).reversed())
                .limit(Math.max(n, 0))
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(
            List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.averagingDouble(this::averageScore)
                ));
    }

    public Optional<Student> findStudentWithHighestSingleScore(
            List<Student> students) {
        return students.stream()
                .filter(student -> !student.getScores().isEmpty())
                .max(Comparator.comparingDouble(this::highestScore));
    }

    public List<Student> getStudentsAboveAverageInMajor(
            List<Student> students, String major) {
        double majorAverage = getAverageScoreByMajor(students)
                .getOrDefault(major, 0.0);

        return students.stream()
                .filter(student -> major.equals(student.getMajor()))
                .filter(student ->
                        averageScore(student) > majorAverage)
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(
            List<Student> students, double passingScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(
                        student -> averageScore(student) >= passingScore
                ));
    }
}

public class Main {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("1", "Alice", 21, "CS",
                        Arrays.asList(90.0, 94.0, 88.0)),
                new Student("2", "Bob", 22, "CS",
                        Arrays.asList(65.0, 72.0, 70.0)),
                new Student("3", "Carol", 20, "CS",
                        Arrays.asList(82.0, 85.0, 80.0)),
                new Student("4", "David", 23, "Math",
                        Arrays.asList(96.0, 89.0, 93.0)),
                new Student("5", "Emma", 21, "Math",
                        Arrays.asList(74.0, 78.0, 76.0)),
                new Student("6", "Frank", 22, "Biology",
                        Arrays.asList(55.0, 60.0, 62.0)),
                new Student("7", "Grace", 20, "Biology",
                        Arrays.asList(81.0, 84.0, 79.0))
        );

        StudentAnalyzer analyzer = new StudentAnalyzer();

        System.out.println("Top 3 students: "
                + analyzer.getTopStudentNames(students, 3));

        System.out.println("Average score by major: "
                + analyzer.getAverageScoreByMajor(students));

        analyzer.findStudentWithHighestSingleScore(students)
                .ifPresent(student -> System.out.println(
                        "Highest single score: " + student));

        System.out.println("CS students above their major average: "
                + analyzer.getStudentsAboveAverageInMajor(students, "CS"));

        Map<Boolean, List<Student>> groups =
                analyzer.partitionByPassFail(students, 70.0);
        System.out.println("Passed: " + groups.get(true));
        System.out.println("Failed: " + groups.get(false));
    }
}