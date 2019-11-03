package circuitlab;

public interface InputProvider 
{
	/**
	 * The name of parameter "input"
	 */
	public static final String INPUT_NAME = "Input";
	
	public Object[] getInput();
	
	public void writeInto(CircuitExperiment e);
	
	public String getName();
}
