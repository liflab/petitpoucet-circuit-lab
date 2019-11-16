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
import examples.Utilities;

public class TriangleListProvider extends RandomInputProvider 
{	
	/**
	 * The name of this input provider
	 */
	public static final transient String TRIANGLE_LIST = "Triangle list";

	public TriangleListProvider(int num_lines, float threshold, Random r)
	{
		super(num_lines, threshold, r);
	}

	@Override
	public Object[] getInput() 
	{
		m_random.reseed();
		List<List<Object>> lines = new ArrayList<List<Object>>(m_numLines);
		for (int i = 0; i < m_numLines; i++)
		{
			List<Object> triangle_list = Utilities.createList(3, 4, 5);
			float f = m_random.nextFloat();
			if (f > m_threshold)
			{
				triangle_list = Utilities.createList(2, "foo", 3);
			}
			lines.add(triangle_list);
		}
		return new Object[] {lines};
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		super.writeInto(e);
		e.describe(LINES, "The number of triangles in the input file");
	}

	@Override
	public String getName()
	{
		return TRIANGLE_LIST;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof TriangleListProvider))
		{
			return false;
		}
		return ((TriangleListProvider) o).m_numLines == m_numLines;
	}

	/**
	 * Static factory method to obtain an instance of this provider
	 * @param num_lines The number of input elements to produce
	 * @param threshold A threshold value used to generate "special" inputs
	 * @param r The random number generator used to produce values
	 * @return An instance of this provider
	 */
	public static TriangleListProvider getProvider(Region r, float threshold, Random rand)
	{
		int num_lines = r.getInt(LINES);
		return new TriangleListProvider(num_lines, threshold, rand);
	}
}
