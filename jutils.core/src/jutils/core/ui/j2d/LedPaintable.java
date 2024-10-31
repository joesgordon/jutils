package jutils.core.ui.j2d;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import jutils.core.ui.IPaintable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedPaintable implements IPaintable
{
    /** The color light gray, {@code 0xC0C0C0} ({@link Color#LIGHT_GRAY}. */
    public static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    /**  */
    public static final int DEFAULT_SIZE = 16;
    /** {@code false} to indicate rounded rectangle. */
    public static final boolean DEFAULT_SHAPE = false;

    /**  */
    private int width;
    /**  */
    private int height;
    /**  */
    private Color color;
    /**  */
    private boolean round;
    /**  */
    private Color borderColor;
    /**  */
    private int borderSize;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedPaintable()
    {
        this( DEFAULT_COLOR );
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public LedPaintable( Color color )
    {
        this( color, DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param color
     * @param size
     **************************************************************************/
    public LedPaintable( Color color, int size )
    {
        this( color, size, size );
    }

    /***************************************************************************
     * @param color
     * @param width
     * @param height
     **************************************************************************/
    public LedPaintable( Color color, int width, int height )
    {
        this( color, width, height, false );
    }

    /***************************************************************************
     * @param color
     * @param width
     * @param height
     * @param round
     **************************************************************************/
    public LedPaintable( Color color, int width, int height, boolean round )
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
    public LedPaintable( Color color, int width, int height, boolean round,
        Color borderColor )
    {
        this.width = width;
        this.height = height;
        this.color = color;
        this.round = round;
        this.borderColor = borderColor == null ? Color.darkGray : borderColor;
        this.borderSize = 1;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getWidth()
    {
        return width;
    }

    /***************************************************************************
     * @param width
     **************************************************************************/
    public void setWidth( int width )
    {
        this.width = width;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHeight()
    {
        return height;
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
        return color;
    }

    /***************************************************************************
     * @param isOval
     **************************************************************************/
    public void setShape( boolean isOval )
    {
        this.round = isOval;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean getShape()
    {
        return round;
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public void setBorder( Color color )
    {
        this.borderColor = color;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Color getBorder()
    {
        return borderColor;
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public void setBorderSize( int size )
    {
        this.borderSize = size;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getBorderSize()
    {
        return this.borderSize;
    }

    /***************************************************************************
     * @param height
     **************************************************************************/
    public void setHeight( int height )
    {
        this.height = height;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paint( JComponent c, Graphics2D graphics )
    {
        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        // graphics.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
        // RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        graphics.setColor( borderColor );

        if( round )
        {
            graphics.fillOval( 0, 0, width, height );
        }
        else
        {
            graphics.fillRoundRect( 0, 0, width, height, 4, 4 );
        }

        Paint storedPaint = graphics.getPaint();
        Paint ledPaint = new GradientPaint( 0, 0, this.color, width, height,
            color.darker() );

        graphics.setPaint( ledPaint );
        // graphics.setColor( this.color );

        int doubleBorder = 2 * borderSize;

        if( round )
        {
            graphics.fillOval( borderSize, borderSize, width - doubleBorder,
                height - doubleBorder );
        }
        else
        {
            graphics.fillRoundRect( borderSize, borderSize,
                width - doubleBorder, height - doubleBorder, 4, 4 );
        }

        graphics.setPaint( storedPaint );
    }
}
