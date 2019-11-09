/*
    A benchmark for Petit Poucet
    Copyright (C) 2019 Laboratoire d'informatique formelle

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package circuitlab.inputs;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.labpal.Random;
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
	
	/**
	 * The random number generator used to produce values
	 */
	protected transient Random m_random;
	
	protected float m_threshold = 0.01f;
	
	public TextLineProvider(int num_lines, float threshold, Random r)
	{
		super();
		m_numLines = num_lines;
		m_random = r;
		m_threshold = threshold;
	}

	@Override
	public Object[] getInput() 
	{
		m_random.reseed();
		List<String> lines = new ArrayList<String>(m_numLines);
		for (int i = 0; i < m_numLines; i++)
		{
			String value = "4";
			float f = m_random.nextFloat();
			if (f < m_threshold)
			//if (i == 0)
			{
				value = "-80";
				System.out.println("ARF");
			}
			String line = "foo," + value + ",bar";
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
	
	public static TextLineProvider getProvider(Region r, float threshold, Random rand)
	{
		int num_lines = r.getInt(LINES);
		return new TextLineProvider(num_lines, threshold, rand);
	}
}
