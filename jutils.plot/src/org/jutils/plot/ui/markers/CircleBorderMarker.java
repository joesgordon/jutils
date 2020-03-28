package org.jutils.plot.ui.markers;

import java.awt.*;

import org.jutils.plot.model.IMarker;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CircleBorderMarker implements IMarker
{
    /**  */
    public Color color;
    /**  */
    private Color borderColor;
    /**  */
    private int diameter;
    /**  */
    private int radius;

    /***************************************************************************
     * 
     **************************************************************************/
    public CircleBorderMarker()
    {
        color = new Color( 0x0066CC );
        borderColor = new Color( 0xCC0000 );

        setSize( 6 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location )
    {
        graphics.setColor( borderColor );

        graphics.fillOval( location.x - radius - 2, location.y - radius - 2,
            diameter + 4, diameter + 4 );

        graphics.setColor( color );

        graphics.fillOval( location.x - radius, location.y - radius, diameter,
            diameter );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setBorderColor( Color color )
    {
        this.borderColor = color;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setSize( int r )
    {
        this.diameter = r;
        this.radius = r / 2;
    }
}
