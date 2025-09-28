
package SelfAssignment2;

import java.util.List;

public interface IOperation {
	String getId();

	String getDescription();

	double getNominalTimeMinutes();

	int getAgvRequired();

	List<AGV> getAGVList();
}