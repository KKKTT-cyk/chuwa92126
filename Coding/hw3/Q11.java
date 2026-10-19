import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StudentAnalysisDemo {

    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("S1", "Alice", 21, "CS",      Arrays.asList(92.0, 88.5, 95.0)),
                new Student("S2", "Bob",   22, "CS",      Arrays.asList(70.0, 65.5, 80.0)),
                new Student("S3", "Carol", 20, "Math",    Arrays.asList(98.5, 91.0, 89.0)),
                new Student("S4", "David", 23, "Math",    Arrays.asList(55.0, 62.0, 58.5)),
                new Student("S5", "Emma",  21, "Physics", Arrays.asList(85.0, 79.5, 90.0)),
                new Student("S6", "Frank", 24, "Physics", Arrays.asList(60.0, 99.0, 45.0)),
                new Student("S7", "Grace", 22, "Biology", Arrays.asList(77.0, 83.0, 80.5))
        );

        StudentAnalyzer analyzer = new StudentAnalyzer();

        System.out.println("Students:");
        students.forEach(System.out::println);

        System.out.println();
        System.out.println("Top 3: " + analyzer.getTopStudentNames(students, 3));

        System.out.println();
        System.out.println("Average by major:");
        analyzer.getAverageScoreByMajor(students)
                .forEach((major, avg) -> System.out.printf("%s %.2f%n", major, avg));

        System.out.println();
        Optional<Student> best = analyzer.findStudentWithHighestSingleScore(students);
        best.ifPresent(s -> System.out.println("Highest single score: " + s.getName()
                + " (" + s.getHighestScore() + ")"));

        Student bestStudent = best.orElseThrow(() -> new IllegalStateException("no students"));
        System.out.println("orElseThrow -> " + bestStudent.getName());

        String emptyCase = analyzer.findStudentWithHighestSingleScore(Collections.<Student>emptyList())
                .map(Student::getName)
                .orElse("none");
        System.out.println("empty list -> " + emptyCase);

        System.out.println();
        for (String major : Arrays.asList("CS", "Math", "Physics", "Art")) {
            List<String> names = analyzer.getStudentsAboveAverageInMajor(students, major).stream()
                    .map(Student::getName)
                    .collect(Collectors.toList());
            System.out.println("Above average in " + major + ": " + names);
        }

        System.out.println();
        Map<Boolean, List<Student>> result = analyzer.partitionByPassFail(students, 75.0);
        System.out.println("Passed (>= 75): " + namesOf(result.get(true)));
        System.out.println("Failed: " + namesOf(result.get(false)));
    }

    private static List<String> namesOf(List<Student> list) {
        return list.stream().map(Student::getName).collect(Collectors.toList());
    }
}

class Student {
    private final String id;
    private final String name;
    private final int age;
    private final String major;
    private final List<Double> scores;

    public Student(String id, String name, int age, String major, List<Double> scores) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores == null ? new ArrayList<Double>() : new ArrayList<>(scores);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getMajor() { return major; }
    public List<Double> getScores() { return Collections.unmodifiableList(scores); }

    // 0.0 if the student has no scores
    public double getAverageScore() {
        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public double getHighestScore() {
        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s avg=%.2f", id, name, major, getAverageScore());
    }
}

class StudentAnalyzer {

    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        // TreeMap keeps majors in alphabetical order
        return students.stream()
                .collect(Collectors.groupingBy(Student::getMajor,
                        TreeMap::new,
                        Collectors.averagingDouble(Student::getAverageScore)));
    }

    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
                .filter(s -> !s.getScores().isEmpty())
                .max(Comparator.comparingDouble(Student::getHighestScore));
    }

    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        List<Student> inMajor = students.stream()
                .filter(s -> s.getMajor().equals(major))
                .collect(Collectors.toList());

        double majorAvg = inMajor.stream()
                .mapToDouble(Student::getAverageScore)
                .average()
                .orElse(0.0);

        return inMajor.stream()
                .filter(s -> s.getAverageScore() > majorAvg)
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(s -> s.getAverageScore() >= passingScore));
    }
}