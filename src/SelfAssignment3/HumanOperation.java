package SelfAssignment3;

import java.util.List;

public class HumanOperation extends IOperation {
    private int skillLevel;

    public HumanOperation(String id, String description, List<Resource> resources) {
        super(id, description, resources);
        this.skillLevel = resources.stream()
                .filter(r -> r instanceof HumanResource)
                .map(r -> ((HumanResource) r).getSkillLevel())
                .mapToInt(Integer::intValue)
                .max()
                .orElse(1);
    }

    @Override
    public double getNominalTimeMinutes() {
        double baseTime = 60.0;
        return baseTime / skillLevel;
    }

    @Override
    public int getResourceRequired() {
        return resources.size();
    }
}