package SelfAssignment3;

public class SoftwareResource extends Resource {
    private String softwareName;
    private String version;

    public SoftwareResource(String id, String softwareName, String version) {
        super(id);
        this.softwareName = softwareName;
        this.version = version;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "SoftwareResource{" +
                "id='" + getId() + '\'' +
                ", softwareName='" + softwareName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}