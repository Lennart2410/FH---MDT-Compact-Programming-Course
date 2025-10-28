package HomeworkAssignment1.packing;

public class Parcel {

    private final String id, boxType;
    private final double weightKg;
    public Parcel(String id, String boxType, double weightKg)
    { this.id=id; this.boxType=boxType; this.weightKg=weightKg;
    }
    public String getId() {
        return id;
    }

    public double getWeightKg(){ return weightKg; }
    public String getBoxType(){ return boxType; }
}
