package circuitlab;

import static circuitlab.CircuitExperiment.SIZE;
import static circuitlab.FunctionProvider.FUNCTION;
import static circuitlab.InputProvider.INPUT_NAME;

import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;
import ca.uqac.lif.mtnp.table.ExpandAsColumns;
import ca.uqac.lif.mtnp.table.TransformedTable;
import circuitlab.circuits.AverageWindow;
import circuitlab.inputs.TextLineProvider;

public class MainLab extends Laboratory
{

	@Override
	public void setup() 
	{
		// The factory to create the experiments
		CircuitExperimentFactory factory = new CircuitExperimentFactory(this);
		
		// Impact of file length
		Region big_r = new Region();
		big_r.add(FUNCTION, AverageWindow.AVERAGE_WINDOW);
		big_r.add(INPUT_NAME, TextLineProvider.CSV_FILE);
		big_r.add(TextLineProvider.LINES, 1, 1000, 5000, 10000);
		ExperimentTable et = new ExperimentTable(TextLineProvider.LINES, FUNCTION, SIZE);
		et.setShowInList(false);
		for (Region r : big_r.all(FUNCTION, TextLineProvider.LINES))
		{
			CircuitExperiment exp = factory.get(r);
			et.add(exp);
		}
		TransformedTable tt = new TransformedTable(new ExpandAsColumns(FUNCTION, SIZE), et);
		tt.setTitle("Impact of file length");
		add(tt);
		Scatterplot plot = new Scatterplot(tt);
		plot.setTitle(tt.getTitle());
		plot.setCaption(Axis.X, "Length").setCaption(Axis.Y, "Size (B)");
		add(plot);
	}
	
	public static void main(String[] args)
	{
		initialize(args, MainLab.class);
	}
}
