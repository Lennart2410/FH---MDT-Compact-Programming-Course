package SelfAssignment2;

import java.util.List;

public interface IOperation {
	    String getId();
	    String getDescription();
		List<AGV> getAGVList();
	    /** How many AGVs this step needs while it runs. */
	    int getAgvRequired();
	    /** Nominal duration in minutes. */
	    double getNominalTimeMinutes();
}
