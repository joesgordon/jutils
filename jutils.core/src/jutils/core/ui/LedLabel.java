package jutils.core.ui;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class LedLabel
{
    /** The color yellow. */
    public static final Color DEFAULT_COLOR = Color.YELLOW;
    /** The default text, an empty string. */
    public static final String DEFAULT_TEXT = "";
    /** The default icon size of 16. */
    public static final int DEFAULT_SIZE = 16;

    /** */
    public final JLabel label;
    /** */
    public final LedIcon icon;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedLabel()
    {
        this( 16 );
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public LedLabel( Color color )
    {
        this( color, DEFAULT_TEXT, DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public LedLabel( String text )
    {
        this( DEFAULT_COLOR, text, DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public LedLabel( int size )
    {
        this( DEFAULT_COLOR, DEFAULT_TEXT, size );
    }

    /***************************************************************************
     * @param color
     * @param size
     **************************************************************************/
    public LedLabel( Color color, int size )
    {
        this( color, DEFAULT_TEXT, size );
    }

    /***************************************************************************
     * @param color
     * @param text
     * @param size
     **************************************************************************/
    public LedLabel( Color color, String text, int size )
    {
        this.icon = new LedIcon( color, size );
        this.label = new JLabel( icon );

        this.label.setText( text );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JComponent getView()
    {
        return label;
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public void setColor( Color color )
    {
        if( !icon.getColor().equals( color ) )
        {
            icon.setColor( color );
            label.revalidate();
            label.repaint();
        }
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        label.setText( text );
    }

    /***************************************************************************
     * @param color
     * @param text
     **************************************************************************/
    public void setStatus( Color color, String text )
    {
        setColor( color );
        setText( text );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setTooltip( String text )
    {
        label.setToolTipText( text );
    }
}
