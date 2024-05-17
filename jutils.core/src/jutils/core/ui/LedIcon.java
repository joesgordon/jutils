package jutils.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JComponent;

import jutils.core.ui.j2d.LedPaintable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedIcon implements Icon
{
    /**  */
    public final LedPaintable led;

    /***************************************************************************
     * @param color
     **************************************************************************/
    public LedIcon( Color color )
    {
        this( color, 16 );
    }

    /***************************************************************************
     * @param color
     * @param size
     **************************************************************************/
    public LedIcon( Color color, int size )
    {
        this( color, size, size );
    }

    /***************************************************************************
     * @param color
     * @param width
     * @param height
     **************************************************************************/
    public LedIcon( Color color, int width, int height )
    {
        this( color, width, height, false );
    }

    /***************************************************************************
     * @param color
     * @param width
     * @param height
     * @param round
     **************************************************************************/
    public LedIcon( Color color, int width, int height, boolean round )
    {
        this( color, width, height, round, null );
    }

    /***************************************************************************
     * @param color
     * @param width
     * @param height
     * @param round
     * @param borderColor
     **************************************************************************/
    public LedIcon( Color color, int width, int height, boolean round,
        Color borderColor )
    {
        this.led = new LedPaintable( color, width, height, round, borderColor );
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public void setColor( Color color )
    {
        led.setColor( color );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Color getColor()
    {
        return led.getColor();
    }

    /***************************************************************************
     * @param isOval
     **************************************************************************/
    public void setShape( boolean isOval )
    {
        led.setShape( isOval );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean getShape()
    {
        return led.getShape();
    }

    /***************************************************************************
     * @param height
     **************************************************************************/
    public void setIconHeight( int height )
    {
        led.setHeight( height );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIconHeight()
    {
        return led.getHeight();
    }

    /***************************************************************************
     * @param width
     **************************************************************************/
    public void setIconWidth( int width )
    {
        led.setWidth( width );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIconWidth()
    {
        return led.getWidth();
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public void setIconSize( int size )
    {
        setIconWidth( size );
        setIconHeight( size );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        Graphics2D graphics = ( Graphics2D )g.create( x, y, led.getWidth(),
            led.getHeight() );

        led.paint( ( JComponent )c, graphics );

        graphics.dispose();
    }
}
