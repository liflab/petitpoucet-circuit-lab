package circuitlab.macros;

import java.util.Map;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.labpal.Formatter;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.macro.MacroMap;

public class LinesOfCode extends MacroMap
{
	/**
	 * The hard-coded number of lines, obtained the last time <tt>cloc</tt>
	 * was run on the source repository.
	 */
	protected static int s_lines = 10000;
	
	public LinesOfCode(Laboratory lab) 
	{
		super(lab);
		add("pploc", "The number of lines of code in the Petit Poucet library");
	}

	@Override
	public void computeValues(Map<String, JsonElement> map)
	{
		map.put("pploc", new JsonNumber((int) Formatter.sigDig(s_lines, 2)));
	}
}
