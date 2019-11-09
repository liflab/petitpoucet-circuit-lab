package circuitlab.macros;

import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.size.SizePrinter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.macro.MacroMap;
import ca.uqac.lif.petitpoucet.circuit.CircuitQueryable.QueryableCircuitConnection;
import ca.uqac.lif.petitpoucet.functions.CircuitFunction.CircuitFunctionQueryable;

public class QueryableSize extends MacroMap
{
	public QueryableSize(Laboratory lab) 
	{
		super(lab);
		add("queryableSize", "The size of an empty CircuitFunctionQueryable");
		add("connectionSize", "The size of an empty QueryableCircuitConnection");
	}

	@Override
	public void computeValues(Map<String, JsonElement> map)
	{
		try
		{
			{
				CircuitFunctionQueryable cfq = new CircuitFunctionQueryable("", 1, 1);
				SizePrinter printer = new SizePrinter();
				map.put("queryableSize", new JsonNumber(printer.print(cfq).intValue()));
			}
			{
				QueryableCircuitConnection ccq = new QueryableCircuitConnection(0, null);
				SizePrinter printer = new SizePrinter();
				map.put("connectionSize", new JsonNumber(printer.print(ccq).intValue()));
			}
		}
		catch (PrintException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
