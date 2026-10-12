package Q12;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        Student s1 = new Student("s1","Amy",18,"Finance", Arrays.asList(90.0,85.5,88.0));
        Student s2 = new Student("s2","Bob",19,"Computer Science", Arrays.asList(88.0,90.0));
        Student s3 = new Student("s3","Cindy",20,"Arts", Arrays.asList(88.5,80.0,95.5));
        Student s4 = new Student("s4","Dave",20,"Finance", Arrays.asList(70.0,71.0));
        Student s5 = new Student("s5","Emma",18,"Computer Science", Arrays.asList(71.0,70.0,72.5));
        Student s6 = new Student("s6","Fiona",21,"English", Arrays.asList(90.5,88.0));
        Student s7 = new Student("s7","George",22,"Finance", Arrays.asList(92.5,89.5,88.0));

        List<Student> students = Arrays.asList(s1,s2,s3,s4,s5,s6,s7);

        StudentAnalyzer studentAnalyzer = new StudentAnalyzer();
        // 1. Top n students
        List<String> topStudents = studentAnalyzer.getTopStudentNames(students,3);
        System.out.println("Top students: "+ topStudents);

        System.out.println("-----------------------");

        // 2. Average score by major
        Map<String,Double> averagesMajor = studentAnalyzer.getAverageScoreByMajor(students);
        System.out.println("Average scores by major: "+averagesMajor);
        System.out.println("-----------------------");

        // 3. Student with the highest single score
        Optional<Student> highestScoresStudent = studentAnalyzer.findStudentWithHighestSingleScore(students);
        highestScoresStudent.ifPresent(student ->
                System.out.println("Student with the highest single score: "+student.getName())
                );
        System.out.println("-----------------------");

        // 4. Students above their major's average
        List<Student> aboveAverageStudents = studentAnalyzer.getStudentsAboveAverageInMajor(students,"Finance");
        System.out.println("Finance students above average: ");
        aboveAverageStudents.stream()
                .map(Student::getName)
                .forEach(System.out::println);

        System.out.println("-----------------------");

        // 5. Partition students into pass and fail
        Map<Boolean, List<Student>> passFail = studentAnalyzer.partitionByPassFail(students,75.0);
        System.out.println("Passed students:");
        passFail.get(true).stream()
                .map(Student::getName)
                .forEach(System.out::println);

        System.out.println("Failed students:");
        passFail.get(false).stream()
                .map(Student::getName)
                .forEach(System.out::println);

    }
}
