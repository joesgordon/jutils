package org.jutils.ui;

import java.awt.*;

import javax.swing.Icon;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedIcon implements Icon
{
    /**  */
    private int height;
    /**  */
    private int width;
    /**  */
    private Color color;
    /**  */
    private boolean round;
    /**  */
    private final Color borderColor;

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
        this.color = color;
        this.width = width;
        this.height = height;
        this.round = round;
        this.borderColor = borderColor == null ? Color.darkGray : borderColor;
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
     * {@inheritDoc}
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
     * @param size
     **************************************************************************/
    public void setIconSize( int size )
    {
        this.height = size;
        this.width = size;
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
        Graphics2D graphics = ( Graphics2D )g.create( x, y, width, height );

        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        graphics.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

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
        graphics.setPaint( new GradientPaint( 0, 0, this.color, width, height,
            color.darker() ) );
        // graphics.setColor( this.color );

        if( round )
        {
            graphics.fillOval( 1, 1, width - 2, height - 2 );
        }
        else
        {
            graphics.fillRoundRect( 1, 1, width - 2, height - 2, 4, 4 );
        }

        graphics.setPaint( storedPaint );

        graphics.dispose();
    }
}
