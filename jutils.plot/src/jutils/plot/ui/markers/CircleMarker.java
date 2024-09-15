package jutils.plot.ui.markers;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import jutils.plot.model.IMarker;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CircleMarker implements IMarker
{
    /**  */
    public Color color;
    /**  */
    private int radius;
    /**  */
    private final Ellipse2D.Float ellipse;

    /***************************************************************************
     * 
     **************************************************************************/
    public CircleMarker()
    {
        this.color = new Color( 0x0066CC );
        this.ellipse = new Ellipse2D.Float( 0, 0, 6, 6 );

        setSize( 6 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location )
    {
        graphics.setColor( color );

        ellipse.x = location.x - radius;
        ellipse.y = location.y - radius;

        graphics.fill( ellipse );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setSize( int diameter )
    {
        this.ellipse.height = diameter;
        this.ellipse.width = diameter;
        this.radius = diameter / 2;
    }

    public int getSize()
    {
        return ( int )ellipse.height;
    }
}
