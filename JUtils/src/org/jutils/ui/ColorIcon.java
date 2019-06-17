package org.jutils.ui;

import java.awt.*;

import javax.swing.Icon;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorIcon implements Icon
{
    /**  */
    private int height;
    /**  */
    private int width;
    /**  */
    private Color color;

    /***************************************************************************
     * @param color
     **************************************************************************/
    public ColorIcon( Color color )
    {
        this( color, 16 );
    }

    /***************************************************************************
     * @param color
     * @param size
     **************************************************************************/
    public ColorIcon( Color color, int size )
    {
        this( color, size, size );
    }

    /***************************************************************************
     * @param color
     * @param width
     * @param height
     **************************************************************************/
    public ColorIcon( Color color, int width, int height )
    {
        setColor( color );
        setIconWidth( width );
        this.height = height;
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public void setColor( Color color )
    {
        this.color = color;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Color getColor()
    {
        return this.color;
    }

    /***************************************************************************
     * @param height
     **************************************************************************/
    public void setIconHeight( int height )
    {
        this.height = height;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIconHeight()
    {
        return height;
    }

    /***************************************************************************
     * @param width
     **************************************************************************/
    public void setIconWidth( int width )
    {
        this.width = width;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIconWidth()
    {
        return width;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        Graphics2D g2 = ( Graphics2D )g;

        Object aaHint = g2.getRenderingHint( RenderingHints.KEY_ANTIALIASING );

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );

        g.setColor( color );
        g.fill3DRect( x, y, getIconWidth(), getIconHeight(), true );

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaHint );
    }
}
