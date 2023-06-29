package org.jutils.core.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;

/*******************************************************************************
 * 
 ******************************************************************************/
@SuppressWarnings( "serial")
public class SimpleButtonBorder extends AbstractBorder implements UIResource
{
    private final AbstractBorder border;

    /***************************************************************************
     * Constructs a {@code ButtonBorder}.
     **************************************************************************/
    public SimpleButtonBorder()
    {
        this( new LineBorder( Color.gray, 1, true ), new MarginBorder() );
    }

    public SimpleButtonBorder( AbstractBorder outer, AbstractBorder inner )
    {
        this( new CompoundBorder( outer, inner ) );
    }

    public SimpleButtonBorder( AbstractBorder border )
    {
        this.border = border;
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
            border.paintBorder( c, g, x, y, w, h );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public Insets getBorderInsets( Component c, Insets newInsets )
    {
        newInsets = border.getBorderInsets( c, newInsets );

        newInsets.set( 3, 3, 3, 3 );

        return newInsets;
    }
}
