/*
    A benchmark for Petit Poucet
    Copyright (C) 2019 Laboratoire d'informatique formelle

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package circuitlab;

import static circuitlab.CircuitExperiment.SIZE;
import static circuitlab.FunctionProvider.FUNCTION;
import static circuitlab.InputProvider.INPUT_NAME;

import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;
import ca.uqac.lif.mtnp.table.ExpandAsColumns;
import ca.uqac.lif.mtnp.table.TransformedTable;
import circuitlab.circuits.AverageWindow;
import circuitlab.circuits.GetAllNumbers;
import circuitlab.inputs.TextLineProvider;

public class MainLab extends Laboratory
{

	@Override
	public void setup() 
	{
		// The factory to create the experiments
		CircuitExperimentFactory factory = new CircuitExperimentFactory(this);
		
		// Basic lab stats
		add(new LabStats(this));
		
		// Nicknamers
		LatexNamer l_namer = new LatexNamer();
		
		// Impact of file length
		Region big_r = new Region();
		big_r.add(FUNCTION, GetAllNumbers.GET_ALL_NUMBERS, AverageWindow.AVERAGE_WINDOW);
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
		l_namer.setNickname(tt, big_r, "tFileLen", "");
		add(tt);
		Scatterplot plot = new Scatterplot(tt);
		plot.setTitle(tt.getTitle());
		plot.setCaption(Axis.X, "Length").setCaption(Axis.Y, "Size (B)");
		l_namer.setNickname(plot, big_r, "pFileLen", "");
		add(plot);
		
		// Size of an empty queryable
		add(new QueryableSize(this));
	}
	
	public static void main(String[] args)
	{
		initialize(args, MainLab.class);
	}
}
