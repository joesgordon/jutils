package jutils.core.laf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

/*******************************************************************************
 * 
 ******************************************************************************/
@SuppressWarnings( "serial")
public class SimpleButtonBorder extends AbstractBorder implements UIResource
{
    /**  */
    private static final Color DEFAULT_COLOR = new Color( 0x9B9B9B );
    // private static final Color DEFAULT_COLOR = new Color( 0x204060 );
    // private static final Color DEFAULT_COLOR = new Color( 0x2F2F2F );

    /**  */
    private final Insets insets;
    /**  */
    private final Color color;

    /***************************************************************************
     * Constructs a {@code ButtonBorder}.
     **************************************************************************/
    public SimpleButtonBorder()
    {
        this( DEFAULT_COLOR );
    }

    /***************************************************************************
     * Constructs a {@code ButtonBorder}.
     * @param color
     **************************************************************************/
    public SimpleButtonBorder( Color color )
    {
        this.color = color;

        this.insets = new Insets( 4, 4, 4, 4 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintBorder( Component c, Graphics g, int x, int y, int w,
        int h )
    {
        AbstractButton button = ( AbstractButton )c;
        ButtonModel model = ( ( AbstractButton )c ).getModel();

        boolean draw = false;

        if( LafUtils.isToolBarButton( button ) )
        {
            if( model.isEnabled() &&
                ( model.isSelected() || model.isRollover() ) )
            {
                draw = true;
            }
        }
        else if( model.isEnabled() )
        {
            draw = true;
        }

        if( draw )
        {
            drawBorder( ( Graphics2D )g, x, y, w, h );
        }
    }

    /***************************************************************************
     * @param g2d
     * @param x
     * @param y
     * @param w
     * @param h
     **************************************************************************/
    private void drawBorder( Graphics2D g2d, int x, int y, int w, int h )
    {
        int thickness = 1;
        float targetRadius = 3f;
        float arcDiameter = targetRadius * 2f;

        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        g2d.setColor( color );
        g2d.setStroke( new BasicStroke( thickness ) );

        float offset = thickness / 2f;
        RoundRectangle2D.Float bdr = new RoundRectangle2D.Float( x + offset,
            y + offset, w - thickness, h - thickness, arcDiameter,
            arcDiameter );
        g2d.draw( bdr );

        g2d.dispose();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Insets getBorderInsets( Component c )
    {
        return insets;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Insets getBorderInsets( Component c, Insets newInsets )
    {
        newInsets.top = insets.top;
        newInsets.left = insets.left;
        newInsets.bottom = insets.bottom;
        newInsets.right = insets.right;
        return newInsets;
    }
}
