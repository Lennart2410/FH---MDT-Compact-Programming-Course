package SelfAssignment3;

public class HumanResource extends Resource {
    private String name;
    private String role;
    private int skillLevel;

    public HumanResource(String id, String name, String role, int skillLevel) {
        super(id);
        this.name = name;
        this.role = role;
        this.skillLevel = skillLevel;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getSkillLevel() {
        return skillLevel;
    }
}