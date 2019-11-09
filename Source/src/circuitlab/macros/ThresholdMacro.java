package circuitlab.macros;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.macro.MacroScalar;
import circuitlab.MainLab;

public class ThresholdMacro extends MacroScalar
{
	protected float m_threshold;
	
	public ThresholdMacro(MainLab lab, float threshold)
	{
		super(lab, "fractionInvolved", "The fraction of input elements that are involved in the designation tree");
		m_threshold = threshold;
	}
	
	@Override
	public JsonElement getValue()
	{
		return new JsonString(((int) (m_threshold * 100f)) + "%");
	}
}
