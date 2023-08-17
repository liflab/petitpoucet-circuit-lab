/*
    A benchmark for Petit Poucet
    Copyright (C) 2019-2023 Laboratoire d'informatique formelle

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

public class MethodCallProvider extends RandomInputProvider 
{	
	/**
	 * The name of this input provider
	 */
	public static final transient String METHOD_CALLS = "Method calls";

	public MethodCallProvider(int num_lines, float threshold, Random r)
	{
		super(num_lines, threshold, r);
	}

	@Override
	public Object[] getInput() 
	{
		m_random.reseed();
		List<String> calls = new ArrayList<String>(m_numLines);
		for (int i = 0; i < m_numLines; i++)
		{
			float f = m_random.nextFloat();
			if (f < m_threshold)
			{
				calls.add("next");
				calls.add("next");
				i++;
			}
			else
			{
				calls.add("hasNext");
			}
		}
		return new Object[] {calls};
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		super.writeInto(e);
		e.describe(LINES, "The number of method calls in the input list");
	}

	@Override
	public String getName()
	{
		return METHOD_CALLS;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof MethodCallProvider))
		{
			return false;
		}
		return ((MethodCallProvider) o).m_numLines == m_numLines;
	}

	/**
	 * Static factory method to obtain an instance of this provider
	 * @param num_lines The number of input elements to produce
	 * @param threshold A threshold value used to generate "special" inputs
	 * @param r The random number generator used to produce values
	 * @return An instance of this provider
	 */
	public static MethodCallProvider getProvider(Region r, float threshold, Random rand)
	{
		int num_lines = r.getInt(LINES);
		return new MethodCallProvider(num_lines, threshold, rand);
	}
}
