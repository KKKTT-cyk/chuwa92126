import java.util.List;

public class Student {
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
        this.scores = scores;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getMajor() { return major; }
    public List<Double> getScores() { return scores; }

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
