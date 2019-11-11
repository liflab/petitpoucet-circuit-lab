package circuitlab.macros;

import static circuitlab.CircuitExperiment.TRACKING_ENABLED;
import static circuitlab.FunctionProvider.FUNCTION;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.macro.MacroMap;
import ca.uqac.lif.labpal.table.VersusTable.ExperimentPair;
import circuitlab.CircuitExperiment;
import circuitlab.CircuitExperimentFactory;
import circuitlab.FunctionProvider;
import circuitlab.MainLab;
import circuitlab.inputs.TextLineProvider;

public abstract class ExperimentPairMacro extends MacroMap 
{
	protected Region m_region;
	
	protected CircuitExperimentFactory m_factory;

	public ExperimentPairMacro(MainLab lab, CircuitExperimentFactory factory, Region r)
	{
		super(lab);
		m_region = r;
		m_factory = factory;
	}
	
	protected Set<ExperimentPair> getPairs(String name)
	{
		Set<ExperimentPair> pairs = new HashSet<ExperimentPair>();
		Region sub_r = new Region(m_region).set(FunctionProvider.FUNCTION, name);
		for (Region r : sub_r.all(FUNCTION, TextLineProvider.LINES))
		{
			Region r_with = new Region(r);
			r_with = r_with.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_YES);
			CircuitExperiment exp_with = m_factory.get(r_with);
			Region r_without = new Region(r);
			r_without = r_without.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_NO);
			CircuitExperiment exp_without = m_factory.get(r_without);
			if (exp_with != null && exp_without != null)
			{
				pairs.add(new ExperimentPair(exp_without, exp_with));
			}
		}
		return pairs;
	}

}
