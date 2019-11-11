package circuitlab.macros;

import java.util.Collection;
import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.macro.MacroMap;
import circuitlab.CircuitExperiment;
import circuitlab.MainLab;
import circuitlab.inputs.TextLineProvider;

public class SingleExperimentData extends MacroMap
{
	/*@ non_null @*/ protected Region m_region;
	
	protected static final transient long s_64gb = 64 * 1024 * 1024;

	public SingleExperimentData(/*@ non_null @*/ MainLab lab, /*@ non_null @*/ Region r)
	{
		super(lab);
		m_region = r;
		add("singleExpFullSize", "Total memory consumption of a single experiment");
		add("singleExpBaseSize", "Total memory consumption of a single experiment without lineage tracking");
		add("singleExpQueryable", "Size of the queryable object for a single experiment");
		add("singleExpGraph", "Size of the designation graph for a single experiment");
		add("singleExpNumLines", "Number of text lines required to fill 64 GB of RAM");
	}

	@Override
	public void computeValues(Map<String,JsonElement> map)
	{
		Collection<Experiment> exps = m_lab.filterExperiments(m_region);
		CircuitExperiment exp = null;
		for (Experiment e : exps)
		{
			if (e instanceof CircuitExperiment)
			{
				exp = (CircuitExperiment) e;
				break;
			}
		}
		if (exp == null)
		{
			return;
		}
		int num_lines = m_region.getInt(TextLineProvider.LINES);
		int mem = exp.readInt(CircuitExperiment.QUERYABLE_SIZE);
		if (num_lines == 0 || mem == 0)
		{
			return;
		}
		long total_lines = s_64gb / (long) (mem / num_lines) * 1000;
		map.put("singleExpFullSize", formatMemory(exp.readInt(CircuitExperiment.TOTAL_MEM)));
		map.put("singleExpBaseSize", formatMemory(exp.readInt(CircuitExperiment.TOTAL_MEM) - exp.readInt(CircuitExperiment.QUERYABLE_SIZE)));
		map.put("singleExpQueryable", formatMemory(exp.readInt(CircuitExperiment.QUERYABLE_SIZE)));
		map.put("singleExpGraph", formatMemory(exp.readInt(CircuitExperiment.GRAPH_SIZE)));
		map.put("singleExpNumLines", formatLines(total_lines));
	} 
	
	protected static JsonString formatLines(long value)
	{
		long millions = value / 1000000;
		return new JsonString(millions + " million");
	}
	
	protected static JsonString formatMemory(int value)
	{
		String out = Integer.toString(value);
		if (value < 1024) // under 1 kB
		{
			out = value + " B";
		}
		else if (value < 1024 * 1024) // under 1 MB
		{
			out = value / 1024 + " kB";
		}
		else if (value < 1024 * 1024 * 1024) // under 1 GB
		{
			out = value / (1024 * 1024) + " MB";
		}
		return new JsonString(out);
	}
}
