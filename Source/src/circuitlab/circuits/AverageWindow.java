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
import ca.uqac.lif.petitpoucet.functions.Constant;
import ca.uqac.lif.petitpoucet.functions.Function;
import ca.uqac.lif.petitpoucet.functions.GroupFunction;
import ca.uqac.lif.petitpoucet.functions.lists.ApplyToAll;
import ca.uqac.lif.petitpoucet.functions.lists.GetElement;
import ca.uqac.lif.petitpoucet.functions.lists.SlidingWindow;
import ca.uqac.lif.petitpoucet.functions.ltl.Ltl;
import ca.uqac.lif.petitpoucet.functions.numbers.Numbers;
import ca.uqac.lif.petitpoucet.functions.strings.Split;
import circuitlab.CircuitExperiment;
import circuitlab.FunctionProvider;

public class AverageWindow implements FunctionProvider
{
	/**
	 * The name of this function provider
	 */
	public static final transient String AVERAGE_WINDOW = "Sliding window average";
	
	public AverageWindow()
	{
		super();
	}

	@Override
	public Function getFunction() 
	{
		GroupFunction global = new GroupFunction(1, 1).setName("All");
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
			CircuitFunction tonum = new CircuitFunction(new ApplyToAll(get));
			global.associateInput(0, tonum, 0);
			CircuitFunction avg_win = new CircuitFunction(new SlidingWindow(3, Numbers.avg));
			global.connect(tonum, 0, avg_win, 0);
			GroupFunction gt2 = new GroupFunction(1, 1).setName("GT 3?");
			{
				CircuitFunction igt = new CircuitFunction(Numbers.isGreaterThan);
				CircuitFunction two = new CircuitFunction(new Constant(3));
				gt2.connect(two, 0, igt, 1);
				gt2.add(igt, two);
				gt2.associateInput(0, igt, 0);
				gt2.associateOutput(0, igt, 0);
			}
			CircuitFunction igt2 = new CircuitFunction(new ApplyToAll(gt2));
			global.connect(avg_win, 0, igt2, 0);
			CircuitFunction g = new CircuitFunction(Ltl.globally);
			global.connect(igt2, 0, g, 0);
			CircuitFunction first = new CircuitFunction(new GetElement(0));
			global.connect(g, 0, first, 0);
			global.associateOutput(0, first, 0);
		}
		return global;
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.describe(FUNCTION, "The circuit that is being evaluated");
		e.setInput(FUNCTION, AVERAGE_WINDOW);
	}

	@Override
	public String getName()
	{
		return AVERAGE_WINDOW;
	}
	
	public static AverageWindow getProvider(Region r)
	{
		return new AverageWindow();
	}

}
