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
import ca.uqac.lif.petitpoucet.TraceabilityNode;
import ca.uqac.lif.petitpoucet.TraceabilityQuery.CausalityQuery;
import ca.uqac.lif.petitpoucet.circuit.CircuitDesignator.NthOutput;
import ca.uqac.lif.petitpoucet.functions.Function;
import ca.uqac.lif.petitpoucet.graph.ConcreteTraceabilityTree;
import ca.uqac.lif.petitpoucet.graph.ConcreteTracer;
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
	
	/**
	 * The parameter "designation graph size"
	 */
	public static final transient String GRAPH_SIZE = "Designation graph size";
	
	/**
	 * The parameter "evaluation time"
	 */
	public static final transient String EVAL_TIME = "Evaluation time";
	
	/**
	 * The parameter "total memory"
	 */
	public static final transient String TOTAL_MEM = "Total memory";
	
	/**
	 * The parameter "total memory squashed"
	 */
	public static final transient String TOTAL_MEM_SQUASHED = "Total memory squashed";
	
	/**
	 * The parameter "tracking enabled"
	 */
	public static final transient String TRACKING_ENABLED = "Tracking enabled";
	
	/**
	 * The value "yes" for the parameter "tracking enabled"
	 */
	public static final transient String TRACKING_YES = "Yes";
	
	/**
	 * The value "no" for the parameter "tracking enabled"
	 */
	public static final transient String TRACKING_NO = "No";
	
	public CircuitExperiment(FunctionProvider fp, InputProvider ip)
	{
		super();
		describe(SIZE, "The size (in bytes) of the queryable resulting from the evaluation of the circuit");
		describe(TOTAL_MEM, "The total memory consumption (in bytes) resulting from the evaluation of the circuit");
		describe(TOTAL_MEM_SQUASHED, "The total memory consumption (in bytes) resulting from the evaluation of the circuit, by counting only the size of the designation tree");
		describe(GRAPH_SIZE, "The size of the designation graph (in bytes)");
		describe(EVAL_TIME, "The time (in ms) required to evaluate the circuit on the input");
		describe(TRACKING_ENABLED, "Whether lineage tracking is enabled for the evaluation of the circuit");
		setInput(TRACKING_ENABLED, TRACKING_YES);
		m_function = fp;
		m_function.writeInto(this);
		m_input = ip;
		m_input.writeInto(this);
	}
	
	/**
	 * Sets whether lineage tracking is enabled for the evaluation of the circuit.
	 * Note that calling this method has no effect if the experiment is already done.
	 * @param b <tt>true</tt> to enable tracking (default), <tt>false</tt> to
	 * disable it
	 */
	public void enableTracking(boolean b)
	{
		if (b)
		{
			setInput(TRACKING_ENABLED, TRACKING_YES);
		}
		else
		{
			setInput(TRACKING_ENABLED, TRACKING_NO);
		}
	}
	
	@Override
	public void execute() throws ExperimentException, InterruptedException 
	{
		Function f = m_function.getFunction();
		Object[] inputs = m_input.getInput();
		Object[] outputs = new Object[f.getOutputArity()];
		boolean track = true;
		if (readString(TRACKING_ENABLED).compareTo(TRACKING_NO) == 0)
		{
			track = false;
		}
		long start_time = System.currentTimeMillis();
		Queryable q = f.evaluate(inputs, outputs, track);
		long end_time = System.currentTimeMillis();
		write(EVAL_TIME, end_time - start_time);
		SizePrinter size_q = new SizePrinter();
		SizePrinter size_f = new SizePrinter();
		SizePrinter size_t = new SizePrinter();
		MapSizePrinter msp = new MapSizePrinter();
		ConcreteTracer tracer = new ConcreteTracer();
		ConcreteTraceabilityTree tree = tracer.trace(CausalityQuery.instance, NthOutput.get(0), q);
		ConcreteTracer squash_tracer = new ConcreteTracer();
		TraceabilityNode tn_squashed = squash_tracer.squash(tree.getRoot());
		int q_size = 0, f_size = 0, q_size_squashed = 0;
		try
		{
			q_size = size_q.print(q).intValue();
			q_size_squashed = size_t.print(tn_squashed).intValue();
			f_size = size_f.print(f).intValue();
			//SizeEntry se = msp.print(q);
			//FileHelper.writeFromString(new File("/tmp/out.txt"), se.toString());
		}
		catch (PrintException e) 
		{
			throw new ExperimentException(e);
		}
		write(SIZE, q_size);
		write(GRAPH_SIZE, q_size_squashed);
		write(TOTAL_MEM, q_size + f_size);
		write(TOTAL_MEM_SQUASHED, q_size_squashed + f_size);
	}

}
