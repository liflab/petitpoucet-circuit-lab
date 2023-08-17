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
import ca.uqac.lif.petitpoucet.functions.Constant;
import ca.uqac.lif.petitpoucet.functions.Equals;
import ca.uqac.lif.petitpoucet.functions.Fork;
import ca.uqac.lif.petitpoucet.functions.Function;
import ca.uqac.lif.petitpoucet.functions.GroupFunction;
import ca.uqac.lif.petitpoucet.functions.lists.ApplyToAll;
import ca.uqac.lif.petitpoucet.functions.lists.GetElement;
import ca.uqac.lif.petitpoucet.functions.ltl.Ltl;
import circuitlab.CircuitExperiment;
import circuitlab.FunctionProvider;

public class HasNext implements FunctionProvider
{
	/**
	 * The name of this function provider
	 */
	public static final transient String HAS_NEXT = "Iterator";
	
	public HasNext()
	{
		super();
	}

	@Override
	public Function getFunction() 
	{
		GroupFunction gf = new GroupFunction(1, 1).setName("G (next imp (X hasNext))");
		{
			CircuitFunction f = new CircuitFunction(new Fork(String.class, 2));
			GroupFunction eq_next = new GroupFunction(1, 1).setName("= next");
			{
				CircuitFunction eq = new CircuitFunction(Equals.instance);
				CircuitFunction c = new CircuitFunction(new Constant("next"));
				eq_next.connect(c, 0, eq, 1);
				eq_next.associateInput(0, eq, 0);
				eq_next.associateOutput(0, eq, 0);
			}
			CircuitFunction g_eq_next = new CircuitFunction(new ApplyToAll(eq_next));
			GroupFunction eq_hasnext = new GroupFunction(1, 1).setName("= hasNext");
			{
				CircuitFunction eq = new CircuitFunction(Equals.instance);
				CircuitFunction c = new CircuitFunction(new Constant("hasNext"));
				eq_hasnext.connect(c, 0, eq, 1);
				eq_hasnext.associateInput(0, eq, 0);
				eq_hasnext.associateOutput(0, eq, 0);
			}
			CircuitFunction g_eq_hasnext = new CircuitFunction(new ApplyToAll(eq_hasnext));
			CircuitFunction x = new CircuitFunction(Ltl.next);
			CircuitFunction imp = new CircuitFunction(Ltl.implies);
			CircuitFunction g = new CircuitFunction(Ltl.globally);
			CircuitFunction first = new CircuitFunction(new GetElement(0));
			gf.associateInput(0, f, 0);
			gf.connect(f, 0, g_eq_next, 0);
			gf.connect(f, 1, g_eq_hasnext, 0);
			gf.connect(g_eq_next, 0, imp, 0);
			gf.connect(g_eq_hasnext, 0, x, 0);
			gf.connect(x, 0, imp, 1);
			gf.connect(imp, 0, g, 0);
			gf.connect(g, 0, first, 0);
			gf.associateOutput(0, first, 0);
		}
		return gf;
		
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.describe(FUNCTION, "The circuit that is being evaluated");
		e.setInput(FUNCTION, HAS_NEXT);
	}

	@Override
	public String getName()
	{
		return HAS_NEXT;
	}
	
	public static HasNext getProvider(Region r)
	{
		return new HasNext();
	}

}
