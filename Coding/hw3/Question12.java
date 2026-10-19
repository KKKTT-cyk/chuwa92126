import java.util.*;
import java.util.stream.Collectors;

public class Question12 {

    static class Student {
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
            return "Student{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", major='" + major + '\'' +
                    ", scores=" + scores +
                    '}';
        }
    }


    static class StudentAnalyzer {

        public double getAverageScore(Student student) {
            return student.getScores().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
        }

        // 1. names of top n students by average score, sorted in descending order
        public List<String> getTopStudentNames(
                List<Student> students, int n) {

            return students.stream()
                    .sorted(Comparator.comparingDouble(
                            this::getAverageScore
                    ).reversed())
                    .limit(n)
                    .map(Student::getName)
                    .collect(Collectors.toList());
        }


        // 2. map of major to average score across all students in that major
        public Map<String, Double> getAverageScoreByMajor(
                List<Student> students) {

            return students.stream()
                    .collect(Collectors.groupingBy(
                            Student::getMajor,
                            Collectors.averagingDouble(
                                    this::getAverageScore
                            )
                    ));
        }


        // 3. the student who has the highest single score among all their scores
        public Optional<Student> findStudentWithHighestSingleScore(
                List<Student> students) {

            return students.stream()
                    .max(Comparator.comparingDouble(
                            student -> student.getScores()
                                    .stream()
                                    .mapToDouble(Double::doubleValue)
                                    .max()
                                    .orElse(0.0)
                    ));
        }


        // 4. students whose average score is above the average of their major
        public List<Student> getStudentsAboveAverageInMajor(
                List<Student> students, String major) {

            double majorAverage = students.stream()
                    .filter(student -> student.getMajor().equals(major))
                    .mapToDouble(this::getAverageScore)
                    .average()
                    .orElse(0.0);

            return students.stream()
                    .filter(student -> student.getMajor().equals(major))
                    .filter(student -> getAverageScore(student) > majorAverage)
                    .collect(Collectors.toList());
        }


        // 5. Partitions students into those who passed (average >= passingScore) and those who failed
        public Map<Boolean, List<Student>> partitionByPassFail(
                List<Student> students, double passingScore) {

            return students.stream()
                    .collect(Collectors.partitioningBy(student -> getAverageScore(student) >= passingScore));
        }
    }


    public static void main(String[] args) {

        // list with least 6 students
        Student s1 = new Student(
                "001",
                "A",
                20,
                "Computer Science",
                Arrays.asList(90.0, 95.0, 88.0)
        );

        Student s2 = new Student(
                "002",
                "B",
                21,
                "Computer Science",
                Arrays.asList(75.0, 80.0, 78.0)
        );

        Student s3 = new Student(
                "003",
                "C",
                22,
                "Mathematics",
                Arrays.asList(92.0, 89.0, 96.0)
        );

        Student s4 = new Student(
                "004",
                "D",
                20,
                "Mathematics",
                Arrays.asList(30.0, 54.0, 72.0)
        );

        Student s5 = new Student(
                "005",
                "E",
                21,
                "Business",
                Arrays.asList(85.0, 82.0, 53.0)
        );

        Student s6 = new Student(
                "006",
                "F",
                23,
                "Business",
                Arrays.asList(50.0, 45.0, 60.0)
        );

        List<Student> students = Arrays.asList(
                s1, s2, s3, s4, s5, s6
        );

        StudentAnalyzer analyzer = new StudentAnalyzer();


        // 1. Top 3 students
        System.out.println("Top 3 Students");

        List<String> topStudents =
                analyzer.getTopStudentNames(students, 3);

        topStudents.forEach(System.out::println);


        // 2. Average score by major
        System.out.println("Average Score By Major");

        Map<String, Double> averageByMajor =
                analyzer.getAverageScoreByMajor(students);

        averageByMajor.forEach((major, average) ->
                System.out.println(
                        major + ": "
                                + String.format("%.2f", average)
                )
        );


        // 3. Student with highest single score
        System.out.println("Student With Highest Single Score");

        Optional<Student> highestStudent =
                analyzer.findStudentWithHighestSingleScore(students);

        highestStudent.ifPresent(student ->
                System.out.println(student.getName())
        );


        // 4. CS students above CS major average
        System.out.println("CS Students Above Major Average"
        );

        List<Student> aboveAverage =
                analyzer.getStudentsAboveAverageInMajor(students, "Computer Science");

        aboveAverage.forEach(System.out::println);


        // 5. Partition pass/fail
        System.out.println("Pass / Fail");

        Map<Boolean, List<Student>> passFail =
                analyzer.partitionByPassFail(
                        students,
                        60.0
                );

        System.out.println("Passed:");

        passFail.get(true)
                .forEach(System.out::println);

        System.out.println("Failed:");

        passFail.get(false)
                .forEach(System.out::println);
    }
}