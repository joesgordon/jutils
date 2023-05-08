package org.jutils.core.ui;

import java.awt.*;

import javax.swing.JPanel;

import org.jutils.core.laf.UIProperty;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GradientPanel extends JPanel
{
    /**  */
    private static final long serialVersionUID = -1594229192971198789L;

    /***************************************************************************
     * 
     **************************************************************************/
    public GradientPanel()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param lm
     **************************************************************************/
    public GradientPanel( LayoutManager lm )
    {
        this( lm, null );
    }

    /***************************************************************************
     * @param lm
     * @param background
     **************************************************************************/
    public GradientPanel( LayoutManager lm, Color background )
    {
        super( lm );
        setBackground( background );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setBackground( Color bg )
    {
        if( bg != null )
        {
            super.setBackground( bg );
        }
        else
        {
            super.setBackground( getDefaultBackground() );
        }
    }

    /***************************************************************************
     * Determines and answers the header's background color. Tries to lookup a
     * special color from the L&amp;F. In case it is absent, it uses the
     * standard internal frame background.
     * @return the color of the header's background
     **************************************************************************/
    private static Color getDefaultBackground()
    {
        Color c = UIProperty.PROGRESSBAR_FOREGROUND.getColor();
        return c != null ? c
            : UIProperty.INTERNALFRAME_ACTIVETITLEBACKGROUND.getColor();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        if( !isOpaque() )
        {
            return;
        }
        Color control = UIProperty.CONTROL.getColor();
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = ( Graphics2D )g;
        Paint storedPaint = g2.getPaint();
        g2.setPaint(
            new GradientPaint( 0, 0, getBackground(), width, 0, control ) );
        g2.fillRect( 0, 0, width, height );
        g2.setPaint( storedPaint );
    }
}
