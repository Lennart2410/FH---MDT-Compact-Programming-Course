package HomeworkAssignment1.general;

public class Employee {

    private String name;
    private int age;
    private JobType jobType;

    public Employee(String name, int age, JobType jobType) {
        this.name = name;
        this.age = age;
        this.jobType = jobType;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public JobType getJobType() {
        return jobType;
    }
}
