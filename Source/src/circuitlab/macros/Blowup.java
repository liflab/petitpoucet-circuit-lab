package circuitlab.macros;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.Formatter;
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.table.VersusTable.ExperimentPair;
import circuitlab.CircuitExperimentFactory;
import circuitlab.FunctionProvider;
import circuitlab.MainLab;

public class Blowup extends ExperimentPairMacro
{	
	protected String m_parameter;

	public Blowup(String parameter, MainLab lab, CircuitExperimentFactory factory, Region r) 
	{
		super(lab, factory, r);
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
			Set<ExperimentPair> pairs = getPairs(name);
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