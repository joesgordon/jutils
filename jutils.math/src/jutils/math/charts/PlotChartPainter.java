package jutils.math.charts;

import java.awt.Graphics2D;
import java.awt.Insets;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotChartPainter implements IChartPainter
{
    /**  */
    private final TicksPainter ticks;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlotChartPainter()
    {
        this.ticks = new TicksPainter();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void getMargin( Insets insets )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paint( Graphics2D g )
    {
        // TODO Auto-generated method stub
    }
}
