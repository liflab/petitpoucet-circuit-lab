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
import ca.uqac.lif.petitpoucet.functions.CircuitFunction;
import ca.uqac.lif.petitpoucet.functions.Function;
import ca.uqac.lif.petitpoucet.functions.GroupFunction;
import ca.uqac.lif.petitpoucet.functions.lists.ApplyToAll;
import ca.uqac.lif.petitpoucet.functions.lists.GetElement;
import ca.uqac.lif.petitpoucet.functions.numbers.Numbers;
import ca.uqac.lif.petitpoucet.functions.strings.Split;
import circuitlab.CircuitExperiment;
import circuitlab.FunctionProvider;

public class GetAllNumbers implements FunctionProvider
{
	/**
	 * The name of this function provider
	 */
	public static final transient String GET_ALL_NUMBERS = "Get all numbers";
	
	public GetAllNumbers()
	{
		super();
	}

	@Override
	public Function getFunction() 
	{
		GroupFunction get = new GroupFunction(1, 1).setName("GET");
		{
			CircuitFunction split = new CircuitFunction(new Split(","));
			CircuitFunction ge = new CircuitFunction(new GetElement(1));
			get.connect(split, 0, ge, 0);
			CircuitFunction tonum = new CircuitFunction(Numbers.cast);
			get.connect(ge, 0, tonum, 0);
			get.add(split, ge, tonum);
			get.associateInput(0, split, 0);
			get.associateOutput(0, tonum, 0);
		}
		GroupFunction all = new GroupFunction(1, 1);
		CircuitFunction cf = new CircuitFunction(new ApplyToAll(get));
		all.add(cf);
		all.associateInput(0, cf, 0);
		all.associateOutput(0, cf, 0);
		return all;
		
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.describe(FUNCTION, "The circuit that is being evaluated");
		e.setInput(FUNCTION, GET_ALL_NUMBERS);
	}

	@Override
	public String getName()
	{
		return GET_ALL_NUMBERS;
	}
	
	public static GetAllNumbers getProvider(Region r)
	{
		return new GetAllNumbers();
	}

}
