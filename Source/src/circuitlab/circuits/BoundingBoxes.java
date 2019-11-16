package circuitlab.circuits;

import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.petitpoucet.functions.Function;
import circuitlab.CircuitExperiment;
import circuitlab.FunctionProvider;
import examples.trees.FetchAll;

public class BoundingBoxes implements FunctionProvider
{
	/**
	 * The name of this function provider
	 */
	public static final transient String BOUNDING_BOXES = "Size of nested bounding boxes";
	
	public BoundingBoxes()
	{
		super();
	}

	@Override
	public Function getFunction() 
	{
		return new FetchAll.AllBoxes();
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		e.describe(FUNCTION, "The circuit that is being evaluated");
		e.setInput(FUNCTION, BOUNDING_BOXES);
	}

	@Override
	public String getName()
	{
		return BOUNDING_BOXES;
	}
	
	public static BoundingBoxes getProvider(Region r)
	{
		return new BoundingBoxes();
	}
}
