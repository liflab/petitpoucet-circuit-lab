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

import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.labpal.Region;
import circuitlab.CircuitExperiment;

public class StringProvider extends RandomInputProvider
{	
	/**
	 * The name of this input provider
	 */
	public static final transient String CHARACTER_STRING = "Character string";
	
	public StringProvider(int chars, float threshold, Random r)
	{
		super(chars, threshold, r);
	}

	@Override
	public Object[] getInput() 
	{
		m_random.reseed();
		int num_chars = 0;
		StringBuilder out = new StringBuilder();
		while (num_chars < m_numLines)
		{
			float f = m_random.nextFloat();
			if (f < m_threshold)
			{
				out.append("abbbbc");
				num_chars += 6;
			}
			else
			{
				out.append("z");
				num_chars++;
			}
		}
		return new Object[] {out.toString()};
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		super.writeInto(e);
		e.describe(LINES, "The number of lines in the input file");
	}
	
	@Override
	public String getName()
	{
		return CHARACTER_STRING;
	}
	
	@Override
	public int hashCode()
	{
		return m_numLines;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof StringProvider))
		{
			return false;
		}
		return ((StringProvider) o).m_numLines == m_numLines;
	}
	
	/**
	 * Static factory method to obtain an instance of this provider
	 * @param num_lines The number of input elements to produce
	 * @param threshold A threshold value used to generate "special" inputs
	 * @param r The random number generator used to produce values
	 * @return An instance of this provider
	 */
	public static StringProvider getProvider(Region r, float threshold, Random rand)
	{
		int num_lines = r.getInt(LINES);
		return new StringProvider(num_lines, threshold, rand);
	}
}
