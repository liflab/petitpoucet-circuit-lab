package circuitlab;

import static circuitlab.CircuitExperiment.TRACKING_ENABLED;
import static circuitlab.FunctionProvider.FUNCTION;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.Formatter;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.macro.MacroMap;
import ca.uqac.lif.labpal.table.VersusTable.ExperimentPair;
import circuitlab.inputs.TextLineProvider;

public class BlowupMacro extends MacroMap
{
	protected CircuitExperimentFactory m_factory;

	protected Region m_region;

	protected String m_parameter;

	public BlowupMacro(String parameter, Laboratory lab, CircuitExperimentFactory factory, Region r) 
	{
		super(lab);
		m_factory = factory;
		m_region = r;
		m_parameter = parameter;
		List<JsonElement> functions = r.getAll(FunctionProvider.FUNCTION);
		for (JsonElement je : functions)
		{
			String name = ((JsonString) je).stringValue();
			add(getMacroName(name), "The blowup on " + m_parameter + " incurred by the use of lineage tracking for function " + name);
		}
	}

	@Override
	public void computeValues(Map<String, JsonElement> map)
	{
		for (JsonElement je : m_region.getAll(FunctionProvider.FUNCTION))
		{
			String name = ((JsonString) je).stringValue();
			Region sub_r = new Region(m_region).set(FunctionProvider.FUNCTION, name);
			Set<ExperimentPair> pairs = new HashSet<ExperimentPair>();
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
			float total = 0f, num = 0f;
			for (ExperimentPair pair : pairs)
			{
				float without = pair.getExperimentX().readFloat(m_parameter);
				float with = pair.getExperimentY().readFloat(m_parameter);
				if (without > 0)
				{
					float blowup = with / without;
					total += blowup;
					num++;
				}
			}
			map.put(getMacroName(name), new JsonNumber(Formatter.sigDig(Formatter.divide(total, num))));
		}
	}

	protected String getMacroName(String problem_name)
	{
		return LatexNamer.latexify("blowup" + m_parameter + problem_name);
	}
}
