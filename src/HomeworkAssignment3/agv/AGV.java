package HomeworkAssignment3.agv;

public class AGV {
    private final String id;
    private boolean isBusy;

    public AGV(String id) {
        this.id = id;
        this.isBusy = false;
    }

    public String getId() {
        return id;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        this.isBusy = busy;
    }

}
