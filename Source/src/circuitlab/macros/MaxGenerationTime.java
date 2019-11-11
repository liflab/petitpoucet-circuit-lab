package circuitlab.macros;

import java.util.Collection;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.macro.MacroScalar;
import ca.uqac.lif.labpal.provenance.ExperimentValue;
import ca.uqac.lif.petitpoucet.NodeFunction;
import circuitlab.CircuitExperiment;
import circuitlab.MainLab;

public class MaxGenerationTime extends MacroScalar
{
	/**
	 * The lineage dependency of this parameter
	 */
	protected NodeFunction m_dependency;
	
	public MaxGenerationTime(MainLab lab)
	{
		super(lab, "maxGraphTime", "The maximum time (in ms) required to generate a designation graph across all experiments");
	}
	
	@Override
	public JsonElement getValue()
	{
		int max_gen_time = 0;
		Collection<Experiment> exps = m_lab.getExperiments();
		for (Experiment exp : exps)
		{
			if (!(exp instanceof CircuitExperiment))
			{
				continue;
			}
			if (exp.readString(CircuitExperiment.TRACKING_ENABLED).compareTo(CircuitExperiment.TRACKING_YES) != 0)
			{
				continue;
			}
			int gen_time = exp.readInt(CircuitExperiment.GRAPH_GEN_TIME);
			if (gen_time > max_gen_time)
			{
				max_gen_time = gen_time;
				m_dependency = new ExperimentValue(exp, CircuitExperiment.GRAPH_GEN_TIME);
			}
		}
		return new JsonString(max_gen_time + " ms");
	}
}