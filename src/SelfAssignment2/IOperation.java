package SelfAssignment2;

public interface IOperation {
	    String getId();
	    String getDescription();
	    /** How many AGVs this step needs while it runs. */
	    int getAgvRequired();
	    /** Nominal duration in minutes. */
	    double getNominalTimeMinutes();
}