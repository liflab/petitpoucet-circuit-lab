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

import ca.uqac.lif.labpal.ExperimentFactory;
import ca.uqac.lif.labpal.Region;
import circuitlab.MainLab;
import circuitlab.circuits.AverageWindow;
import circuitlab.circuits.BoundingBoxes;
import circuitlab.circuits.GetAllNumbers;
import circuitlab.circuits.SumTriangleAreas;
import circuitlab.inputs.TextLineProvider;
import circuitlab.inputs.TriangleListProvider;
import circuitlab.inputs.WebPageProvider;

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
		if (r.getString(CircuitExperiment.TRACKING_ENABLED).compareTo(CircuitExperiment.TRACKING_NO) == 0)
		{
			ce.enableTracking(false);
		}
		return ce;
	}
	
	protected InputProvider getInputProvider(Region r)
	{
		String name = r.getString(InputProvider.INPUT_NAME);
		if (name.compareTo(TextLineProvider.CSV_FILE) == 0)
		{
			return TextLineProvider.getProvider(r, MainLab.s_threshold, m_lab.getRandom());
		}
		if (name.compareTo(TriangleListProvider.TRIANGLE_LIST) == 0)
		{
			return TriangleListProvider.getProvider(r, MainLab.s_threshold, m_lab.getRandom());
		}
		if (name.compareTo(WebPageProvider.WEB_PAGE) == 0)
		{
			return WebPageProvider.getProvider(r, MainLab.s_threshold, m_lab.getRandom());
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
		if (name.compareTo(GetAllNumbers.GET_ALL_NUMBERS) == 0)
		{
			return GetAllNumbers.getProvider(r);
		}
		if (name.compareTo(SumTriangleAreas.TRIANGLE_AREAS) == 0)
		{
			return SumTriangleAreas.getProvider(r);
		}
		if (name.compareTo(BoundingBoxes.BOUNDING_BOXES) == 0)
		{
			return BoundingBoxes.getProvider(r);
		}
		return null;
	}

}
