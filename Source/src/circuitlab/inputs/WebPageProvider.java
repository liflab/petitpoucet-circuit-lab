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
package circuitlab.inputs;

import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.petitpoucet.functions.trees.TreeNode;
import circuitlab.CircuitExperiment;

public class WebPageProvider extends RandomInputProvider 
{
	/**
	 * The name of this input provider
	 */
	public static final transient String WEB_PAGE = "Web page";
	
	/**
	 * The degree of each non-leaf node in the tree (i.e. we generate balanced
	 * trees)
	 */
	protected int m_degree = 4;
	
	protected static String[] s_elementNames = {"p", "a", "ul", "li", "b", "div"};

	public WebPageProvider(int num_lines, float threshold, Random r)
	{
		super(num_lines, threshold, r);
	}
	
	@Override
	public Object[] getInput()
	{
		TreeNode root = new TreeNode("root");
		int max_depth = (int) Math.floor(Math.log(m_numLines) / Math.log(m_degree));
		generateTree(root, max_depth, 1024);
		return new Object[] {root};
	}
	
	protected void generateTree(TreeNode root, int depth, int box_size)
	{
		if (depth == 0)
		{
			return;
		}
		for (int i = 0; i < m_degree; i++)
		{
			boolean special = m_random.nextFloat() < m_threshold;
			String name = s_elementNames[m_random.nextInt(s_elementNames.length)];
			TreeNode child = new TreeNode(name);
			child.set("x", 200);
			child.set("y", 200);
			int new_box_size = box_size - 1;
			if (special)
			{
				new_box_size += 2;
			}
			child.set("height", new_box_size);
			child.set("width", new_box_size);
			generateTree(child, depth - 1, new_box_size);
			root.addChild(child);
		}
	}

	@Override
	public void writeInto(CircuitExperiment e) 
	{
		super.writeInto(e);
		e.describe(LINES, "The number of elements in the web page");
	}

	@Override
	public String getName()
	{
		return WEB_PAGE;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof WebPageProvider))
		{
			return false;
		}
		return ((TriangleListProvider) o).m_numLines == m_numLines;
	}

	/**
	 * Static factory method to obtain an instance of this provider
	 * @param num_lines The number of input elements to produce
	 * @param threshold A threshold value used to generate "special" inputs
	 * @param r The random number generator used to produce values
	 * @return An instance of this provider
	 */
	public static WebPageProvider getProvider(Region r, float threshold, Random rand)
	{
		int num_lines = r.getInt(LINES);
		return new WebPageProvider(num_lines, threshold, rand);
	}
}
