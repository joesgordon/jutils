package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

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
     * @param f
     **************************************************************************/
    public HighlightedLabel( Font font )
    {
        this();
        setFont( font );
    }

    /***************************************************************************
     * Constructor
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
     * @param color
     **************************************************************************/
    public void setHighlightColor( Color color )
    {
        highlightColor = color;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.JComponent#setFont(java.awt.Font)
     */
    @Override
    public void setFont( Font font )
    {
        super.setFont( font );
        fontMetrics = getFontMetrics( getFont() );
    }

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

    /***************************************************************************
     * UHighlightedLabel demo
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        class DemoRunner implements IFrameApp
        {
            @Override
            public JFrame createFrame()
            {
                JFrame frame = new JFrame();
                JPanel panel = new JPanel();

                HighlightedLabel label1 = new HighlightedLabel(
                    new Font( "Monospaced", Font.PLAIN, 12 ) );
                label1.setText( "Sample un-highlighted" );

                HighlightedLabel label2 = new HighlightedLabel(
                    new Font( "Sans Serif", Font.BOLD, 16 ) );
                label2.setText( "Sample with highlight" );
                label2.setHighlight( 12, 9 );

                HighlightedLabel label3 = new HighlightedLabel(
                    new Font( "Sans Serif", Font.BOLD, 16 ) );
                label3.setText( "Sample with highlight" );
                label3.setHighlight( 12, 9 );
                label3.setHorizontalAlignment( SwingConstants.CENTER );

                HighlightedLabel label4 = new HighlightedLabel(
                    "Another sample" );
                label4.setHighlightColor( Color.magenta );
                label4.setHighlight( 8, 3 );

                panel.setLayout( new GridBagLayout() );
                panel.add( label1,
                    new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.33,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label2,
                    new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.33,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label3,
                    new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.33,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label4,
                    new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.33,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "UHighlightedLabel demo" );
                frame.setContentPane( panel );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setPreferredSize( new Dimension( 350, 150 ) );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }

            @Override
            public void finalizeGui()
            {
            }
        }

        FrameRunner.invokeLater( new DemoRunner() );
    }
}
