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
package circuitlab.circuits;

import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.petitpoucet.functions.Function;
import circuitlab.CircuitExperiment;
import circuitlab.FunctionProvider;
import examples.TriangleAreas;

public class SumTriangleAreas implements FunctionProvider
{
	/**
	 * The name of this function provider
	 */
	public static final transient String TRIANGLE_AREAS = "Sum of valid triangle areas";
	
	public SumTriangleAreas()
	{
		super();
	}

	@Override
	public Function getFunction() 
	{
		return new TriangleAreas.SumOfAreas();
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.describe(FUNCTION, "The circuit that is being evaluated");
		e.setInput(FUNCTION, TRIANGLE_AREAS);
	}

	@Override
	public String getName()
	{
		return TRIANGLE_AREAS;
	}
	
	public static SumTriangleAreas getProvider(Region r)
	{
		return new SumTriangleAreas();
	}

}
