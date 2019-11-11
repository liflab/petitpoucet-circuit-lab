package circuitlab.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.Formatter;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.provenance.ExperimentValue;
import ca.uqac.lif.labpal.table.VersusTable.ExperimentPair;
import ca.uqac.lif.petitpoucet.AggregateFunction;
import ca.uqac.lif.petitpoucet.NodeFunction;
import circuitlab.CircuitExperiment;
import circuitlab.CircuitExperimentFactory;
import circuitlab.FunctionProvider;
import circuitlab.MainLab;

public class MaxBlowupTime extends ExperimentPairMacro
{
	/**
	 * The lineage dependency of this parameter
	 */
	protected NodeFunction m_dependency;
	
	/**
	 * The minimum running time (in ms) for a pair of experiments to be included
	 * in the calculation. We deliberately exclude experiments with extremely 
	 * short running times, as the imprecision of the system's clock becomes
	 * larger than any actual "blowup" incurred by the use of lineage.
	 */
	public static final int s_exclusionFactor = 30;
	
	protected String m_parameter;
	
	public MaxBlowupTime(MainLab lab, CircuitExperimentFactory factory, Region r) 
	{
		super(lab, factory, r);
		add("maxBlowupTime", "The maximum blowup on execution time incurred by the use of lineage tracking across all experiments");
		m_parameter = CircuitExperiment.EVAL_TIME;
	}
	
	@Override
	public void computeValues(Map<String, JsonElement> map)
	{
		float max_blowup = 0f;
		for (JsonElement je : m_region.getAll(FunctionProvider.FUNCTION))
		{
			String name = ((JsonString) je).stringValue();
			Set<ExperimentPair> pairs = getPairs(name);
			for (ExperimentPair pair : pairs)
			{
				float without = pair.getExperimentX().readFloat(m_parameter);
				float with = pair.getExperimentY().readFloat(m_parameter);
				if (without > 0 && with > s_exclusionFactor && without >= s_exclusionFactor)
				{
					float blowup = with / without;
					if (blowup > max_blowup)
					{
						max_blowup = blowup;
						m_dependency = setDependency(pair.getExperimentX(), pair.getExperimentY());
					}
				}
			}
		}
		map.put("maxBlowupTime", new JsonNumber(Formatter.sigDig(max_blowup, 2)));
	}
	
	protected AggregateFunction setDependency(Experiment e1, Experiment e2)
	{
		List<NodeFunction> set = new ArrayList<NodeFunction>(2);
		set.add(new ExperimentValue(e1, m_parameter));
		set.add(new ExperimentValue(e2, m_parameter));
		AggregateFunction af = new AggregateFunction("The rate of increase of parameter " + m_parameter, set);
		return af;
	}
	
	@Override
	public NodeFunction getDependency()
	{
		return m_dependency;
	}
}
