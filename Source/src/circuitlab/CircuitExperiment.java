package circuitlab;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.size.SizePrinter;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.ExperimentException;
import ca.uqac.lif.petitpoucet.Queryable;
import ca.uqac.lif.petitpoucet.functions.Function;

public class CircuitExperiment extends Experiment
{
	/**
	 * The function to evaluate
	 */
	protected FunctionProvider m_function;
	
	/**
	 * The input given to the function
	 */
	protected InputProvider m_input;
	
	/**
	 * The parameter "queryable size"
	 */
	public static final transient String SIZE = "Queryable size";
	
	public CircuitExperiment(FunctionProvider fp, InputProvider ip)
	{
		super();
		describe(SIZE, "The size (in bytes) of the queryable resulting from the evaluation of the circuit");
		m_function = fp;
		m_function.writeInto(this);
		m_input = ip;
		m_input.writeInto(this);
	}
	
	@Override
	public void execute() throws ExperimentException, InterruptedException 
	{
		Function f = m_function.getFunction();
		Object[] inputs = m_input.getInput();
		Object[] outputs = new Object[f.getOutputArity()];
		Queryable q = f.evaluate(inputs, outputs);
		SizePrinter size_p = new SizePrinter();
		int size = 0;
		try
		{
			size = size_p.print(q).intValue();
		}
		catch (PrintException e) 
		{
			throw new ExperimentException(e);
		}
		write(SIZE, size);
	}

}
