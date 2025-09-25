package SelfAssignment2;


public class SingleOperation implements IOperation {
	    private final String id;
	    private final String description;
	    private final int agvRequired;
	    private final double nominalTimeMinutes;
	  

	    public SingleOperation(String id, String description, int agvRequired,
	                           double nominalTimeMinutes) {
	        this.id = id;
	        this.description = description;
	        this.agvRequired = agvRequired;
	        this.nominalTimeMinutes = nominalTimeMinutes;
	     
	    }

	    public String getId() { return id; }
	    public String getDescription() { return description; }
	    public int getAgvRequired() { return agvRequired; }
	    public double getNominalTimeMinutes() { return nominalTimeMinutes; }
}


