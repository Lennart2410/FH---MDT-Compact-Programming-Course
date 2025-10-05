package SelfAssignment3;

import java.util.List;

public interface IOperation {
	    String getId();
	    String getDescription();
		List<Resource> getResourceList();
	    /** How many AGVs this step needs while it runs. */
	    int getResourcesRequired();
	    /** Nominal duration in minutes. */
	    double getNominalTimeMinutes();
}
