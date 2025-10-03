package SelfAssignment3;

public abstract class Resource {
    protected String id;

    public Resource(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}