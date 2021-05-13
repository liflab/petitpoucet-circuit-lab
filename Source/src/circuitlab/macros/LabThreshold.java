/*
    A benchmark for Petit Poucet
    Copyright (C) 2019-2021 Laboratoire d'informatique formelle

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
package circuitlab.macros;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.macro.MacroScalar;
import circuitlab.MainLab;

/**
 * Macro that exports the threshold used in the experiments to generate
 * designation trees.
 */
public class LabThreshold extends MacroScalar
{
	/**
	 * The threshold to export.
	 */
	protected float m_threshold;
	
	public LabThreshold(MainLab lab, float threshold)
	{
		super(lab, "fractionInvolved", "The fraction of input elements that are involved in the designation tree");
		m_threshold = threshold;
	}
	
	@Override
	public JsonElement getValue()
	{
		return new JsonString(((int) (m_threshold * 100f)) + "%");
	}
}
