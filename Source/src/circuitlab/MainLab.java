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

import static circuitlab.CircuitExperiment.EVAL_TIME;
import static circuitlab.CircuitExperiment.GRAPH_SIZE;
import static circuitlab.CircuitExperiment.SIZE;
import static circuitlab.CircuitExperiment.TOTAL_MEM;
import static circuitlab.CircuitExperiment.TOTAL_MEM_SQUASHED;
import static circuitlab.CircuitExperiment.TRACKING_ENABLED;
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
import circuitlab.macros.BlowupMacro;
import circuitlab.macros.LabStats;
import circuitlab.macros.QueryableSize;
import circuitlab.macros.SingleExperimentData;
import circuitlab.macros.ThresholdMacro;
import circuitlab.plots.CustomScatterplot;

public class MainLab extends Laboratory
{
	protected static float s_threshold = 0.01f;

	@Override
	public void setup() 
	{
		// Metadata
		setAuthor("Sylvain Hallé, Hugo Tremblay");

		// The factory to create the experiments
		CircuitExperimentFactory factory = new CircuitExperimentFactory(this);

		// Basic lab stats
		add(new LabStats(this));

		// Nicknamers
		LatexNamer l_namer = new LatexNamer();

		// Setup of global region
		Region big_r = new Region();
		big_r.add(FUNCTION, GetAllNumbers.GET_ALL_NUMBERS, AverageWindow.AVERAGE_WINDOW);
		big_r.add(INPUT_NAME, TextLineProvider.CSV_FILE);
		big_r.add(TextLineProvider.LINES, 5, 10, 1000, 2000, 5000, 10000);
		big_r.add(TRACKING_ENABLED, CircuitExperiment.TRACKING_NO, CircuitExperiment.TRACKING_YES);

		// Impact of file length
		{
			Region sub_r = new Region(big_r);
			sub_r = sub_r.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_YES);
			ExperimentTable et = new ExperimentTable(TextLineProvider.LINES, FUNCTION, SIZE);
			et.setShowInList(false);
			for (Region r : sub_r.all(FUNCTION, TextLineProvider.LINES))
			{
				CircuitExperiment exp = factory.get(r);
				et.add(exp);
			}
			TransformedTable tt = new TransformedTable(new ExpandAsColumns(FUNCTION, SIZE), et);
			tt.setTitle("Impact of file length");
			l_namer.setNickname(tt, sub_r, "tFileLen", "");
			add(tt);
			Scatterplot plot = new Scatterplot(tt);
			plot.setTitle(tt.getTitle());
			plot.setCaption(Axis.X, "Length").setCaption(Axis.Y, "Size (B)");
			l_namer.setNickname(plot, sub_r, "pFileLen", "");
			add(plot);
		}

		// Impact of enabling tracking
		{
			Region sub_r = new Region(big_r);
			CategoryVersusTable et_t = new CategoryVersusTable(EVAL_TIME, FUNCTION, "Time (without)");
			et_t.setTitle("Impact of enabling tracking on execution time");
			l_namer.setNickname(et_t, sub_r, "tTrackingImpactTime", "");
			add(et_t);
			CategoryVersusTable et_m = new CategoryVersusTable(TOTAL_MEM, FUNCTION, "Memory (without)");
			et_m.setTitle("Impact of enabling tracking on memory");
			l_namer.setNickname(et_m, sub_r, "tTrackingImpactMemory", "");
			add(et_m);
			CategoryVersusTable et_m_s = new CategoryVersusTable(SIZE, GRAPH_SIZE, FUNCTION, "Memory (full)");
			et_m_s.setTitle("Impact of enabling tracking on memory (squashed version)");
			l_namer.setNickname(et_m_s, sub_r, "tTrackingImpactMemorySquashed", "");
			add(et_m_s);
			for (Region r : sub_r.all(FUNCTION, TextLineProvider.LINES))
			{
				Region r_with = new Region(r);
				r_with = r_with.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_YES);
				CircuitExperiment exp_with = factory.get(r_with);
				Region r_without = new Region(r);
				r_without = r_without.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_NO);
				CircuitExperiment exp_without = factory.get(r_without);
				if (exp_with != null && exp_without != null)
				{
					et_t.add(exp_without, exp_with);
					et_m.add(exp_without, exp_with);
					et_m_s.add(exp_with, exp_with);
				}
			}
			Scatterplot plot_t = new CustomScatterplot(et_t);
			plot_t.setTitle(et_t.getTitle());
			plot_t.setCaption(Axis.X, "Time without (ms)").setCaption(Axis.Y, "Time with (ms)");
			plot_t.withLines(false);
			l_namer.setNickname(plot_t, sub_r, "pTrackingImpactTime", "");
			add(plot_t);
			Scatterplot plot_m = new CustomScatterplot(et_m);
			plot_m.setTitle(et_m.getTitle());
			plot_m.setCaption(Axis.X, "Memory without (B)").setCaption(Axis.Y, "Memory with (B)");
			plot_m.withLines(false);
			l_namer.setNickname(plot_m, sub_r, "pTrackingImpactMemory", "");
			add(plot_m);
			Scatterplot plot_m_s = new CustomScatterplot(et_m_s);
			plot_m_s.setTitle(et_m_s.getTitle());
			plot_m_s.setCaption(Axis.X, "Memory without (B)").setCaption(Axis.Y, "Memory with (B)");
			plot_m_s.withLines(false);
			l_namer.setNickname(plot_m_s, sub_r, "pTrackingImpactMemorySquashed", "");
			add(plot_m_s);
		}

		// Size of an empty queryable
		add(new QueryableSize(this));

		// Blow-up macros
		add(new BlowupMacro(TOTAL_MEM, this, factory, big_r));
		add(new BlowupMacro(EVAL_TIME, this, factory, big_r));

		// Threshold for explanation queries
		add(new ThresholdMacro(this, s_threshold));

		// Stats for a single experiment taken as an example in the text
		add(new SingleExperimentData(this, big_r.set(FUNCTION, AverageWindow.AVERAGE_WINDOW)
				.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_YES)
				.set(INPUT_NAME, TextLineProvider.CSV_FILE)
				.set(TextLineProvider.LINES, 10000)));
	}

	public static void main(String[] args)
	{
		initialize(args, MainLab.class);
	}
}
