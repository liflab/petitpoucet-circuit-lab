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
package circuitlab.circuits;

import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.petitpoucet.functions.CircuitFunction;
import ca.uqac.lif.petitpoucet.functions.Function;
import ca.uqac.lif.petitpoucet.functions.GroupFunction;
import ca.uqac.lif.petitpoucet.functions.lists.GetSize;
import ca.uqac.lif.petitpoucet.functions.strings.RegexFindAll;
import circuitlab.CircuitExperiment;
import circuitlab.FunctionProvider;

public class CountMatches implements FunctionProvider
{
	/**
	 * The name of this function provider
	 */
	public static final transient String COUNT_REGEX_MATCHES = "Count regex matches";
	
	public CountMatches()
	{
		super();
	}

	@Override
	public Function getFunction() 
	{
		GroupFunction get = new GroupFunction(1, 1).setName("COUNT");
		{
			CircuitFunction matches = new CircuitFunction(new RegexFindAll("ab*c"));
			CircuitFunction size = new CircuitFunction(GetSize.instance);
			get.connect(matches, 0, size, 0);
			get.associateInput(0, matches, 0);
			get.associateOutput(0, size, 0);
		}
		return get;
		
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.describe(FUNCTION, "The circuit that is being evaluated");
		e.setInput(FUNCTION, COUNT_REGEX_MATCHES);
	}

	@Override
	public String getName()
	{
		return COUNT_REGEX_MATCHES;
	}
	
	public static CountMatches getProvider(Region r)
	{
		return new CountMatches();
	}

}
