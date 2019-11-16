package circuitlab.inputs;

import ca.uqac.lif.labpal.Random;
import circuitlab.CircuitExperiment;
import circuitlab.InputProvider;

public abstract class RandomInputProvider implements InputProvider
{
	/**
	 * The parameter "number of lines"
	 */
	public static final transient String LINES = "Number of lines";
	
	/**
	 * The number of input elements to produce
	 */
	protected int m_numLines;
	
	/**
	 * The random number generator used to produce values
	 */
	protected transient Random m_random;
	
	/**
	 * A threshold value used to generate "special" inputs (whose meaning
	 * depends on the actual provider used)
	 */
	protected float m_threshold = 0.01f;
	
	/**
	 * Creates a new random input provider
	 * @param num_lines The number of input elements to produce
	 * @param threshold A threshold value used to generate "special" inputs
	 * @param r The random number generator used to produce values
	 */
	public RandomInputProvider(int num_lines, float threshold, Random r)
	{
		super();
		m_numLines = num_lines;
		m_random = r;
		m_threshold = threshold;
	}
	
	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.setInput(LINES, m_numLines);
		e.describe(INPUT_NAME, "The input given to the circuit");
		e.setInput(INPUT_NAME, getName());
	}
	
	@Override
	public int hashCode()
	{
		return m_numLines;
	}
}
