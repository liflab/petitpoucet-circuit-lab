package circuitlab.plots;

import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;
import ca.uqac.lif.mtnp.table.Table;

public class CustomScatterplot extends Scatterplot
{
	protected int m_pointSize = 2;
	
	public CustomScatterplot(Table t)
	{
		super(t);
	}
	
	public CustomScatterplot setPointSize(int size)
	{
		m_pointSize = size;
		return this;
	}
	
	@Override
	public String toGnuplot(ImageType term, String lab_title, boolean with_caption)
	{
		String out = super.toGnuplot(term, lab_title, with_caption);
		out = "set pointsize " + m_pointSize + "\n" + out;
		return out;
	}
}
