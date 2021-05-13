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
import static circuitlab.CircuitExperiment.GRAPH_GEN_TIME;
import static circuitlab.CircuitExperiment.GRAPH_SIZE;
import static circuitlab.CircuitExperiment.QUERYABLE_SIZE;
import static circuitlab.CircuitExperiment.TOTAL_MEM;
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
import circuitlab.circuits.BoundingBoxes;
import circuitlab.circuits.GetAllNumbers;
import circuitlab.circuits.SumTriangleAreas;
import circuitlab.inputs.TextLineProvider;
import circuitlab.inputs.TriangleListProvider;
import circuitlab.inputs.WebPageProvider;
import circuitlab.macros.Blowup;
import circuitlab.macros.LabStats;
import circuitlab.macros.MaxBlowupTime;
import circuitlab.macros.MaxGenerationTime;
import circuitlab.macros.QueryableSize;
import circuitlab.macros.SingleExperimentData;
import circuitlab.macros.LabThreshold;
import circuitlab.macros.LinesOfCode;
import circuitlab.plots.CustomScatterplot;

public class MainLab extends Laboratory
{
	/**
	 * The fraction of input elements that are involved in the designation tree.
	 */
	protected static float s_threshold = 0.01f;

	@Override
	public void setup() 
	{
		// Metadata
		setAuthor("Sylvain Hallé, Hugo Tremblay");
		setDoi("10.5281/zenodo.4717794");

		// The factory to create the experiments
		CircuitExperimentFactory factory = new CircuitExperimentFactory(this);

		// Basic lab stats
		add(new LabStats(this));

		// Nicknamers
		LatexNamer l_namer = new LatexNamer();

		// Setup of global region
		CompatibleRegion big_r = new CompatibleRegion();
		big_r.add(FUNCTION, GetAllNumbers.GET_ALL_NUMBERS, AverageWindow.AVERAGE_WINDOW, SumTriangleAreas.TRIANGLE_AREAS, BoundingBoxes.BOUNDING_BOXES);
		big_r.add(INPUT_NAME, TextLineProvider.CSV_FILE, TriangleListProvider.TRIANGLE_LIST, WebPageProvider.WEB_PAGE);
		big_r.add(TextLineProvider.LINES, 10, 100, 1000, 2000, 5000, 10000, 15000);
		big_r.add(TRACKING_ENABLED, CircuitExperiment.TRACKING_NO, CircuitExperiment.TRACKING_YES);

		// Impact of file length
		{
			CompatibleRegion sub_r = new CompatibleRegion(big_r);
			sub_r = sub_r.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_YES);
			ExperimentTable et_size = new ExperimentTable(TextLineProvider.LINES, FUNCTION, QUERYABLE_SIZE);
			et_size.setShowInList(false);
			ExperimentTable et_time = new ExperimentTable(TextLineProvider.LINES, FUNCTION, GRAPH_GEN_TIME);
			et_time.setShowInList(false);
			for (Region r : sub_r.all(FUNCTION, InputProvider.INPUT_NAME, TextLineProvider.LINES))
			{
				CircuitExperiment exp = factory.get(r);
				et_size.add(exp);
				et_time.add(exp);
			}
			TransformedTable tt_size = new TransformedTable(new ExpandAsColumns(FUNCTION, QUERYABLE_SIZE), et_size);
			tt_size.setTitle("Impact of file length on queyrable size");
			l_namer.setNickname(tt_size, sub_r, "tFileLenSize", "");
			add(tt_size);
			Scatterplot plot_size = new Scatterplot(tt_size);
			plot_size.setTitle(tt_size.getTitle());
			plot_size.setCaption(Axis.X, "Length").setCaption(Axis.Y, "Size (B)");
			l_namer.setNickname(plot_size, sub_r, "pFileLenSize", "");
			add(plot_size);
			TransformedTable tt_time = new TransformedTable(new ExpandAsColumns(FUNCTION, GRAPH_GEN_TIME), et_time);
			tt_time.setTitle("Impact of file length on graph generation time");
			l_namer.setNickname(tt_time, sub_r, "tFileLenTime", "");
			add(tt_time);
			Scatterplot plot_time = new Scatterplot(tt_time);
			plot_time.setTitle(tt_time.getTitle());
			plot_time.setCaption(Axis.X, "Length").setCaption(Axis.Y, "Time (ms)");
			l_namer.setNickname(plot_time, sub_r, "pFileLenTime", "");
			add(plot_time);
		}

		// Impact of enabling tracking
		{
			CompatibleRegion sub_r = new CompatibleRegion(big_r);
			CategoryVersusTable et_t = new CategoryVersusTable(EVAL_TIME, FUNCTION, "Time (without)");
			et_t.setTitle("Impact of enabling tracking on execution time");
			l_namer.setNickname(et_t, sub_r, "tTrackingImpactTime", "");
			add(et_t);
			CategoryVersusTable et_m = new CategoryVersusTable(TOTAL_MEM, FUNCTION, "Memory (without)");
			et_m.setTitle("Impact of enabling tracking on memory");
			l_namer.setNickname(et_m, sub_r, "tTrackingImpactMemory", "");
			add(et_m);
			CategoryVersusTable et_m_s = new CategoryVersusTable(QUERYABLE_SIZE, GRAPH_SIZE, FUNCTION, "Queryable size");
			et_m_s.setTitle("Impact of enabling tracking on memory (squashed version)");
			l_namer.setNickname(et_m_s, sub_r, "tTrackingImpactMemorySquashed", "");
			add(et_m_s);
			for (Region r : sub_r.all(FUNCTION, InputProvider.INPUT_NAME, TextLineProvider.LINES))
			{
				CompatibleRegion r_with = new CompatibleRegion(r);
				r_with = r_with.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_YES);
				CircuitExperiment exp_with = factory.get(r_with);
				CompatibleRegion r_without = new CompatibleRegion(r);
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
		add(new Blowup(TOTAL_MEM, this, factory, big_r));
		add(new Blowup(EVAL_TIME, this, factory, big_r));
		add(new MaxBlowupTime(this, factory, big_r));

		// Generation time for designation graphs
		add(new MaxGenerationTime(this));

		// Threshold for explanation queries
		add(new LabThreshold(this, s_threshold));

		// Stats for a single experiment taken as an example in the text
		add(new SingleExperimentData(this, big_r.set(FUNCTION, AverageWindow.AVERAGE_WINDOW)
				.set(TRACKING_ENABLED, CircuitExperiment.TRACKING_YES)
				.set(INPUT_NAME, TextLineProvider.CSV_FILE)
				.set(TextLineProvider.LINES, 10000)));

		// Lines of code in Petit Poucet
		add(new LinesOfCode(this));
	}

	public static void main(String[] args)
	{
		initialize(args, MainLab.class);
	}

	public static class CompatibleRegion extends Region
	{
		public CompatibleRegion()
		{
			super();
		}

		public CompatibleRegion(Region r)
		{
			super(r);
		}

		@Override
		public CompatibleRegion set(String name, String value)
		{
			Region r = super.set(name, value);
			return new CompatibleRegion(r);
		}

		@Override
		public boolean isInRegion(Region point)
		{
			String fct = point.getString(FUNCTION);
			String pro = point.getString(INPUT_NAME);
			//System.out.println(fct + "," + pro);
			if (pro.compareTo(TriangleListProvider.TRIANGLE_LIST) == 0)
			{
				if (fct.compareTo(SumTriangleAreas.TRIANGLE_AREAS) == 0)
				{
					return true;
				}
			}
			if (pro.compareTo(TextLineProvider.CSV_FILE) == 0)
			{
				if (fct.compareTo(GetAllNumbers.GET_ALL_NUMBERS) == 0 ||
						fct.compareTo(AverageWindow.AVERAGE_WINDOW) == 0)
				{
					return true;
				}
			}
			if (pro.compareTo(WebPageProvider.WEB_PAGE) == 0)
			{
				if (fct.compareTo(BoundingBoxes.BOUNDING_BOXES) == 0)
				{
					return true;
				}
			}
			return false;
		}

		@Override
		protected CompatibleRegion getRegion(Region r)
		{
			return new CompatibleRegion(r);
		}

		@Override
		protected CompatibleRegion getEmptyRegion()
		{
			return new CompatibleRegion();
		}
	}
}
