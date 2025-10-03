package SelfAssignment3;

public abstract class Resource {
    private String id;

    public Resource(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  "Id: " + getId() + "\n";
    }
}
