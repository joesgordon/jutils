package jutils.plot.ui.lines;

import java.awt.*;

import jutils.plot.model.ILine;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SolidLine implements ILine
{
    /**  */
    public Color color;
    /**  */
    private int size;

    /**  */
    private BasicStroke solidStroke;

    /***************************************************************************
     * 
     **************************************************************************/
    public SolidLine()
    {
        this.color = new Color( 0x0066CC );
        this.setSize( 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point from, Point to )
    {
        graphics.setColor( color );

        graphics.setStroke( solidStroke );

        graphics.drawLine( from.x, from.y, to.x, to.y );
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public void setSize( int size )
    {
        this.size = size;
        this.solidStroke = new BasicStroke( size, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getSize()
    {
        return size;
    }
}
