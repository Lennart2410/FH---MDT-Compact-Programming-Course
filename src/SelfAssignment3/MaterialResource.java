package SelfAssignment3;

public class MaterialResource extends Resource {
    private String materialType;
    private double quantity;

    public MaterialResource(String id, String materialType, double quantity) {
        super(id);
        this.materialType = materialType;
        this.quantity = quantity;
    }

    public String getMaterialType() {
        return materialType;
    }

    public double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MaterialResource{" +
                "id='" + getId() + '\'' +
                ", materialType='" + materialType + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}