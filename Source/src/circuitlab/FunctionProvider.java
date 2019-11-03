package circuitlab;

import ca.uqac.lif.petitpoucet.functions.Function;

public interface FunctionProvider 
{
	/**
	 * The name of parameter "function"
	 */
	public static final String FUNCTION = "Function";
	
	public Function getFunction();
	
	public void writeInto(CircuitExperiment e);
	
	public String getName();
}
