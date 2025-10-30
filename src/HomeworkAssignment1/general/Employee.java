package HomeworkAssignment1.general;

public class Employee {

    private String name;
    private int age;
    private JobType jobType;
    private boolean currentlyOccupied;

    public Employee(String name, int age, JobType jobType) {
        this.name = name;
        this.age = age;
        this.jobType = jobType;
        this.currentlyOccupied = false;
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

    public boolean isCurrentlyOccupied() {
        return currentlyOccupied;
    }

    public void setCurrentlyOccupied(boolean currentlyOccupied) {
        this.currentlyOccupied = currentlyOccupied;
    }
}
