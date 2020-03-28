package org.jutils.chart.ui.lines;

import java.awt.*;

import org.jutils.chart.model.ILine;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimpleLine implements ILine
{
    /**  */
    public Color color;
    /**  */
    public Point p1;
    /**  */
    public Point p2;

    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleLine()
    {
        this.color = new Color( 0x0066CC );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point from, Point to )
    {
        graphics.setColor( color );
        graphics.drawLine( from.x, from.y, to.x, to.y );
    }

}
