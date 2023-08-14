package jutils.core.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/*******************************************************************************
 * Adds highlighting functionality to a standard JLabel.
 ******************************************************************************/
public class HighlightedLabel extends JLabel
{
    /**  */
    private static final long serialVersionUID = 2460200518187514263L;
    /**  */
    private Color highlightColor = Color.cyan;
    /**  */
    private int highlightOffset;
    /**  */
    private int highlightLength;
    /**  */
    private FontMetrics fontMetrics;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HighlightedLabel()
    {
    }

    /***************************************************************************
     * Constructor
     * @param font
     **************************************************************************/
    public HighlightedLabel( Font font )
    {
        this();
        setFont( font );
    }

    /***************************************************************************
     * Constructor
     * @param text
     **************************************************************************/
    public HighlightedLabel( String text )
    {
        super( text );
        highlightOffset = 0;
        highlightLength = 0;
        fontMetrics = getFontMetrics( getFont() );
    }

    /***************************************************************************
     * Sets the highlighted region of this label.
     * @param offset start offset
     * @param length length of highlight
     **************************************************************************/
    public void setHighlight( int offset, int length )
    {
        if( getText() == null )
        {
            return;
        }

        int len = getText().length();
        if( offset > len )
        {
            highlightOffset = 0;
            highlightLength = 0;
            return;
        }

        highlightOffset = offset;
        highlightLength = Math.min( len - offset, length );
        repaint();
    }

    /***************************************************************************
     * Clears the highlighted region.
     **************************************************************************/
    public void clearHighlight()
    {
        setHighlight( 0, 0 );
    }

    /***************************************************************************
     * Sets the color used to highlight.
     * @param color the highlighting color.
     **************************************************************************/
    public void setHighlightColor( Color color )
    {
        highlightColor = color;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setFont( Font font )
    {
        super.setFont( font );
        fontMetrics = getFontMetrics( getFont() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    protected void paintComponent( Graphics g )
    {
        int width = getWidth();
        int height = getHeight();
        // g.setColor( getBackground() );
        // g.fillRect( 0, 0, width, getHeight() );

        if( highlightLength != 0 )
        {
            String text = getText();
            if( text == null )
            {
                return;
            }
            int txtWidth = fontMetrics.stringWidth( text );
            int offWidth = fontMetrics.stringWidth(
                text.substring( 0, highlightOffset ) );
            int highWidth = fontMetrics.stringWidth( text.substring(
                highlightOffset, highlightOffset + highlightLength ) );
            int x = offWidth;

            switch( getHorizontalAlignment() )
            {
                case SwingConstants.CENTER:
                    x += ( int )( ( width - txtWidth ) / 2.0 );
                    break;
                case SwingConstants.RIGHT:
                    x += width - getInsets().right - txtWidth;
                    break;
                default:
                    x += getInsets().left;
                    break;
            }

            g.setColor( highlightColor );
            g.fillRect( x, 0, highWidth, height );
        }

        super.paintComponent( g );
    }
}
