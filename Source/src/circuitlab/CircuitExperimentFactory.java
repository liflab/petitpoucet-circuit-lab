package circuitlab;

import ca.uqac.lif.labpal.ExperimentFactory;
import ca.uqac.lif.labpal.Region;
import circuitlab.MainLab;
import circuitlab.circuits.AverageWindow;
import circuitlab.inputs.TextLineProvider;

public class CircuitExperimentFactory extends ExperimentFactory<MainLab,CircuitExperiment>
{
	public CircuitExperimentFactory(MainLab lab)
	{
		super(lab, CircuitExperiment.class);
	}

	@Override
	protected CircuitExperiment createExperiment(Region r) 
	{
		InputProvider ip = getInputProvider(r);
		FunctionProvider fp = getFunctionProvider(r);
		if (ip == null || fp == null)
		{
			return null;
		}
		CircuitExperiment ce = new CircuitExperiment(fp, ip);
		return ce;
	}
	
	protected InputProvider getInputProvider(Region r)
	{
		String name = r.getString(InputProvider.INPUT_NAME);
		if (name.compareTo(TextLineProvider.CSV_FILE) == 0)
		{
			return TextLineProvider.getProvider(r);
		}
		return null;
	}
	
	protected FunctionProvider getFunctionProvider(Region r)
	{
		String name = r.getString(FunctionProvider.FUNCTION);
		if (name.compareTo(AverageWindow.AVERAGE_WINDOW) == 0)
		{
			return AverageWindow.getProvider(r);
		}
		return null;
	}

}
