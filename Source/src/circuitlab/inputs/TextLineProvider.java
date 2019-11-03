package circuitlab.inputs;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.labpal.Region;
import circuitlab.CircuitExperiment;
import circuitlab.InputProvider;

public class TextLineProvider implements InputProvider 
{
	/**
	 * The parameter "number of lines"
	 */
	public static final transient String LINES = "Number of lines";
	
	/**
	 * The name of this input provider
	 */
	public static final transient String CSV_FILE = "CSV file";
	
	/**
	 * The number of lines to produce
	 */
	protected int m_numLines;
	
	public TextLineProvider(int num_lines)
	{
		super();
		m_numLines = num_lines;
	}

	@Override
	public Object[] getInput() 
	{
		List<String> lines = new ArrayList<String>(m_numLines);
		for (int i = 0; i < m_numLines; i++)
		{
			String line = "foo,40,bar";
			lines.add(line);
		}
		return new Object[] {lines};
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.describe(LINES, "The number of lines in the input file");
		e.setInput(LINES, m_numLines);
		e.describe(INPUT_NAME, "The input given to the circuit");
		e.setInput(INPUT_NAME, CSV_FILE);
	}
	
	@Override
	public String getName()
	{
		return CSV_FILE;
	}
	
	@Override
	public int hashCode()
	{
		return m_numLines;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof TextLineProvider))
		{
			return false;
		}
		return ((TextLineProvider) o).m_numLines == m_numLines;
	}
	
	public static TextLineProvider getProvider(Region r)
	{
		int num_lines = r.getInt(LINES);
		return new TextLineProvider(num_lines);
	}
}
