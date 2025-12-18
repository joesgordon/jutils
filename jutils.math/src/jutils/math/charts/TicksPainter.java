package jutils.math.charts;

import java.awt.Graphics2D;

import jutils.core.ui.IPainter;
import jutils.math.charts.props.TicksProperties;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TicksPainter implements IPainter
{
    public TicksProperties properties;

    public TicksPainter()
    {
        this.properties = new TicksProperties();
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
