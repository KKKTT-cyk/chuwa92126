import java.util.*;
import java.util.stream.Collectors;

class Student {

    private String id;
    private String name;
    private int age;
    private String major;
    private List<Double> scores;

    public Student(
            String id,
            String name,
            int age,
            String major,
            List<Double> scores) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMajor() {
        return major;
    }

    public List<Double> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return name + " (" + major + ")";
    }
}


class StudentAnalyzer {

    private double getAverageScore(Student student) {

        return student.getScores()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }


    public List<String> getTopStudentNames(
            List<Student> students,
            int n) {

        return students.stream()
                .sorted(
                        Comparator.comparingDouble(
                                this::getAverageScore
                        ).reversed()
                )
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }


    public Map<String, Double>
    getAverageScoreByMajor(List<Student> students) {

        return students.stream()
                .collect(
                        Collectors.groupingBy(
                                Student::getMajor,
                                Collectors.averagingDouble(
                                        this::getAverageScore
                                )
                        )
                );
    }


    public Optional<Student>
    findStudentWithHighestSingleScore(
            List<Student> students) {

        return students.stream()
                .filter(student ->
                        !student.getScores().isEmpty())
                .max(
                        Comparator.comparingDouble(
                                this::getHighestScore
                        )
                );
    }


    private double getHighestScore(Student student) {

        return student.getScores()
                .stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(
                        Double.NEGATIVE_INFINITY
                );
    }


    public List<Student>
    getStudentsAboveAverageInMajor(
            List<Student> students,
            String major) {

        double majorAverage =
                students.stream()
                        .filter(student ->
                                student.getMajor()
                                        .equals(major))
                        .mapToDouble(
                                this::getAverageScore
                        )
                        .average()
                        .orElse(0.0);

        return students.stream()
                .filter(student ->
                        student.getMajor()
                                .equals(major))
                .filter(student ->
                        getAverageScore(student)
                                > majorAverage)
                .collect(Collectors.toList());
    }


    public Map<Boolean, List<Student>>
    partitionByPassFail(
            List<Student> students,
            double passingScore) {

        return students.stream()
                .collect(
                        Collectors.partitioningBy(
                                student ->
                                        getAverageScore(student)
                                                >= passingScore
                        )
                );
    }
}


public class StudentAnalysisDemo {

    public static void main(String[] args) {

        Student s1 =
                new Student(
                        "S001",
                        "Alice",
                        20,
                        "Computer Science",
                        Arrays.asList(
                                95.0, 90.0, 92.0
                        )
                );

        Student s2 =
                new Student(
                        "S002",
                        "Bob",
                        21,
                        "Computer Science",
                        Arrays.asList(
                                78.0, 82.0, 80.0
                        )
                );

        Student s3 =
                new Student(
                        "S003",
                        "Charlie",
                        22,
                        "Business",
                        Arrays.asList(
                                88.0, 84.0, 90.0
                        )
                );

        Student s4 =
                new Student(
                        "S004",
                        "David",
                        20,
                        "Business",
                        Arrays.asList(
                                70.0, 72.0, 68.0
                        )
                );

        Student s5 =
                new Student(
                        "S005",
                        "Emma",
                        21,
                        "Engineering",
                        Arrays.asList(
                                98.0, 96.0, 99.0
                        )
                );

        Student s6 =
                new Student(
                        "S006",
                        "Frank",
                        23,
                        "Engineering",
                        Arrays.asList(
                                75.0, 80.0, 77.0
                        )
                );

        List<Student> students =
                Arrays.asList(
                        s1, s2, s3,
                        s4, s5, s6
                );

        StudentAnalyzer analyzer =
                new StudentAnalyzer();


        // Top 3 students
        System.out.println(
                "Top 3 students:"
        );

        analyzer.getTopStudentNames(
                students,
                3
        ).forEach(System.out::println);


        // Average score by major
        System.out.println(
                "\nAverage score by major:"
        );

        analyzer.getAverageScoreByMajor(students)
                .forEach(
                        (major, average) ->
                                System.out.println(
                                        major
                                                + ": "
                                                + average
                                )
                );


        // Student with highest single score
        System.out.println(
                "\nStudent with highest single score:"
        );

        Optional<Student> highest =
                analyzer.findStudentWithHighestSingleScore(
                        students
                );

        highest.ifPresent(System.out::println);


        // Students above average in Computer Science
        System.out.println(
                "\nComputer Science students "
                        + "above major average:"
        );

        analyzer.getStudentsAboveAverageInMajor(
                students,
                "Computer Science"
        ).forEach(System.out::println);


        // Partition pass/fail
        System.out.println(
                "\nPass/Fail with passing score = 80:"
        );

        Map<Boolean, List<Student>> result =
                analyzer.partitionByPassFail(
                        students,
                        80.0
                );

        System.out.println(
                "Passed: " + result.get(true)
        );

        System.out.println(
                "Failed: " + result.get(false)
        );
    }
}