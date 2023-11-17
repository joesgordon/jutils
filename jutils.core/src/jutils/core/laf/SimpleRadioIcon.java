package jutils.core.laf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.io.Serializable;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/*******************************************************************************
 *
 ******************************************************************************/
@SuppressWarnings( "serial")
public class SimpleRadioIcon implements Icon, UIResource, Serializable
{
    /**  */
    private int size;
    /**  */
    private Color disabledColor;
    /**  */
    private Color rolloverColor;
    /**  */
    private Color selectColor;

    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleRadioIcon()
    {
        this.size = 10;

        this.disabledColor = UIManager.getColor( "textInactiveText" );
        this.rolloverColor = UIProperty.RADIOBUTTON_HIGHLIGHT.getColor();
        this.selectColor = UIProperty.RADIOBUTTON_SELECT.getColor();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        JRadioButton button = ( JRadioButton )c;
        ButtonModel model = button.getModel();
        boolean enabled = model.isEnabled();
        boolean rollover = ( enabled && model.isRollover() );
        boolean pressed = ( enabled && model.isPressed() && model.isArmed() );
        boolean selected = model.isSelected();

        Ellipse2D.Float outer = createCentered( 1.0f, size );
        Ellipse2D.Float inner = createCentered( .85f, size );
        Ellipse2D.Float check = createCentered( .5f, size );

        Color fg = enabled ? c.getForeground() : this.disabledColor;
        Color bg = enabled ? button.getBackground() : this.disabledColor;

        bg = pressed ? selectColor : bg;

        Graphics2D g2 = ( Graphics2D )g;

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        g2.setRenderingHint( RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY );

        g2.translate( x, y );

        g2.setColor( bg );
        g2.fill( outer );

        g2.setColor( fg );
        g2.draw( outer );

        if( rollover && !pressed )
        {
            Stroke s = g2.getStroke();

            g2.setStroke( new BasicStroke( 1.5f ) );
            g2.setColor( rolloverColor );
            g2.draw( inner );

            g2.setStroke( s );
        }

        if( selected && !pressed )
        {
            g2.setColor( fg );
            g2.fill( check );
        }

        g2.translate( -x, -y );
    }

    /***************************************************************************
     * @param scale
     * @param size
     * @return
     **************************************************************************/
    private static Float createCentered( float scale, int size )
    {
        float w = size * scale;
        float p = ( size - w - 1 ) / 2.0f;

        return new Ellipse2D.Float( p, p, w, w );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIconWidth()
    {
        return size;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIconHeight()
    {
        return size;
    }
}
