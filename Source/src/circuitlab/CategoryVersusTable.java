/*
  LabPal, a versatile environment for running experiments on a computer
  Copyright (C) 2015-2017 Sylvain Hallé

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package circuitlab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.provenance.ExperimentValue;
import ca.uqac.lif.labpal.table.VersusTable;
import ca.uqac.lif.mtnp.table.TableEntry;
import ca.uqac.lif.mtnp.table.TempTable;

/**
 * Table creating (x,y) points from the results of pairs of experiments,
 * separated into categories.
 * @author Sylvain Hallé
 */
public class CategoryVersusTable extends VersusTable
{
	/**
	 * The name of the parameter in each experiment used as the category
	 */
	protected String m_category;
	
	/**
	 * The name of the parameter to read in the second experiment
	 */
	protected String m_parameterY;
	
	/**
	 * The name of all the categories seen in the table
	 */
	protected List<String> m_categories;
	
	/**
	 * Creates a new empty table
	 * @param parameter The name of the parameter to read in each experiment
	 */
	public CategoryVersusTable(String parameter)
	{
		super(parameter);
		m_categories = new ArrayList<String>();
	}
	
	/**
	 * Creates a new empty table
	 * @param parameter The name of the parameter to read in each experiment
	 * @param parameter_y The name of the parameter to read in each experiment
	 * @param category The name of the parameter in each experiment used as the category
	 * @param caption_x The name of the first column
	 */
	public CategoryVersusTable(String parameter, String category, String caption_x)
	{
		this(parameter, parameter, category, caption_x);
	}
	
	/**
	 * Creates a new empty table
	 * @param parameter_x The name of the parameter to read in the first experiment
	 * @param parameter_y The name of the parameter to read in the second experiment
	 * @param category The name of the parameter in each experiment used as the category
	 * @param caption_x The name of the first column
	 */
	public CategoryVersusTable(String parameter_x, String parameter_y, String category, String caption_x)
	{
		super(parameter_x, caption_x, "");
		m_category = category;
		m_categories = new ArrayList<String>();
		m_categories.add(caption_x);
		m_parameterY = parameter_y;
	}
	
	@Override
	public VersusTable add(Experiment experiment_x, Experiment experiment_y)
	{
		String cx = experiment_x.readString(m_category);
		String cy = experiment_x.readString(m_category);
		if (!m_categories.contains(cx))
		{
			m_categories.add(cx);
		}
		if (!m_categories.contains(cy))
		{
			m_categories.add(cy);
		}
		return super.add(experiment_x, experiment_y);
	}

	@Override
	public TempTable getDataTable(boolean temporary)
	{
		String[] cats = new String[m_categories.size()];
		for (int i = 0; i < cats.length; i++)
		{
			cats[i] = m_categories.get(i);
		}
		TempTable table = new TempTable(getId(), cats);
		table.setId(getId());
		Map<Integer,TableEntry> entries = new HashMap<Integer,TableEntry>();
		for (ExperimentPair pair : m_pairs)
		{
			Object x = pair.getExperimentX().read(m_parameter);
			Object y = pair.getExperimentY().read(m_parameterY);
			if (x == null || y == null || x instanceof JsonNull || y instanceof JsonNull)
			{
				continue;
			}
			int x_coord = ((JsonNumber) x).numberValue().intValue();
			String category = pair.getExperimentX().readString(m_category);
			TableEntry te;
			if (entries.containsKey(x_coord))
			{
				te = entries.get(x_coord);
			}
			else
			{
				te = new TableEntry();
				te.put(m_captionX, x);
				te.addDependency(m_captionX, new ExperimentValue(pair.getExperimentX(), m_parameter));
				entries.put(x_coord, te);
			}
			te.put(category, y);
			te.addDependency(category, new ExperimentValue(pair.getExperimentY(), m_parameterY));
		}
		table.addAll(entries.values());
		//System.out.println(table);
		return table;
	}	
}
