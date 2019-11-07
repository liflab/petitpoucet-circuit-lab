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
package circuitlab;

import java.io.File;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.size.SizePrinter;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.ExperimentException;
import ca.uqac.lif.mtnp.util.FileHelper;
import ca.uqac.lif.petitpoucet.Queryable;
import ca.uqac.lif.petitpoucet.functions.Function;
import circuitlab.MapSizePrinter.SizeEntry;

public class CircuitExperiment extends Experiment
{
	/**
	 * The function to evaluate
	 */
	protected FunctionProvider m_function;
	
	/**
	 * The input given to the function
	 */
	protected InputProvider m_input;
	
	/**
	 * The parameter "queryable size"
	 */
	public static final transient String SIZE = "Queryable size";
	
	public CircuitExperiment(FunctionProvider fp, InputProvider ip)
	{
		super();
		describe(SIZE, "The size (in bytes) of the queryable resulting from the evaluation of the circuit");
		m_function = fp;
		m_function.writeInto(this);
		m_input = ip;
		m_input.writeInto(this);
	}
	
	@Override
	public void execute() throws ExperimentException, InterruptedException 
	{
		Function f = m_function.getFunction();
		Object[] inputs = m_input.getInput();
		Object[] outputs = new Object[f.getOutputArity()];
		Queryable q = f.evaluate(inputs, outputs);
		SizePrinter size_p = new SizePrinter();
		MapSizePrinter msp = new MapSizePrinter();
		int size = 0;
		try
		{
			size = size_p.print(q).intValue();
			SizeEntry se = msp.print(q);
			FileHelper.writeFromString(new File("/tmp/out.txt"), se.toString());
		}
		catch (PrintException e) 
		{
			throw new ExperimentException(e);
		}
		write(SIZE, size);
	}

}
