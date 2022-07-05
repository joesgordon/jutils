package org.jutils.core.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalToolBarUI;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JUtilsToolBarUI extends MetalToolBarUI
{
    /***************************************************************************
     * 
     **************************************************************************/
    public JUtilsToolBarUI()
    {
        super();
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    public static ComponentUI createUI( JComponent c )
    {
        return new JUtilsToolBarUI();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void installUI( JComponent c )
    {
        super.installUI( c );

        JToolBar bar = ( JToolBar )c;

        setRolloverBorders( true );

        // bar.setBorder( new LineBorder( Color.GREEN, 8 ) );
        bar.setRollover( true );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected Border createRolloverBorder()
    {
        return new CompoundBorder( // No UIResource
            new RolloverButtonBorder(), new RolloverMarginBorder() );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void setBorderToNormal( Component c )
    {
        if( c instanceof AbstractButton )
        {
            AbstractButton b = ( AbstractButton )c;

            // b.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
            b.setBorder( new LineBorder( Color.orange, 5 ) );
        }
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void setBorderToRollover( Component c )
    {
        if( c instanceof AbstractButton )
        {
            super.setBorderToRollover( c );
        }
        else if( c instanceof Container )
        {
            Container cont = ( Container )c;
            for( int i = 0; i < cont.getComponentCount(); i++ )
            {
                super.setBorderToRollover( cont.getComponent( i ) );
            }
        }
    }

    private static void drawDark3DBorder( Graphics g, int x, int y, int w,
        int h )
    {
        drawFlush3DBorder( g, x, y, w, h );
        g.setColor( PlasticLookAndFeel.getControl() );
        g.drawLine( x + 1, y + 1, 1, h - 3 );
        g.drawLine( y + 1, y + 1, w - 3, 1 );
    }

    private static void drawDisabledBorder( Graphics g, int x, int y, int w,
        int h )
    {
        g.setColor( MetalLookAndFeel.getControlShadow() );
        drawRect( g, x, y, w - 1, h - 1 );
    }

    /*
     * Unlike {@code MetalUtils} we first draw with highlight then dark shadow
     */
    private static void drawFlush3DBorder( Graphics g, int x, int y, int w,
        int h )
    {
        g.translate( x, y );
        g.setColor( PlasticLookAndFeel.getControlHighlight() );
        drawRect( g, 1, 1, w - 2, h - 2 );
        g.drawLine( 0, h - 1, 0, h - 1 );
        g.drawLine( w - 1, 0, w - 1, 0 );
        g.setColor( PlasticLookAndFeel.getControlDarkShadow() );
        drawRect( g, 0, 0, w - 2, h - 2 );
        g.translate( -x, -y );
    }

    /*
     * Copied from {@code MetalUtils}.
     */
    private static void drawPressed3DBorder( Graphics g, int x, int y, int w,
        int h )
    {
        g.translate( x, y );
        drawFlush3DBorder( g, 0, 0, w, h );
        g.setColor( MetalLookAndFeel.getControlShadow() );
        g.drawLine( 1, 1, 1, h - 3 );
        g.drawLine( 1, 1, w - 3, 1 );
        g.translate( -x, -y );
    }

    /*
     * Copied from {@code MetalUtils}.
     */
    private static void drawButtonBorder( Graphics g, int x, int y, int w,
        int h, boolean active )
    {
        if( active )
        {
            drawActiveButtonBorder( g, x, y, w, h );
        }
        else
        {
            drawFlush3DBorder( g, x, y, w, h );
        }
    }

    /*
     * Copied from {@code MetalUtils}.
     */
    private static void drawActiveButtonBorder( Graphics g, int x, int y, int w,
        int h )
    {
        drawFlush3DBorder( g, x, y, w, h );
        g.setColor( PlasticLookAndFeel.getPrimaryControl() );
        g.drawLine( x + 1, y + 1, x + 1, h - 3 );
        g.drawLine( x + 1, y + 1, w - 3, x + 1 );
        g.setColor( PlasticLookAndFeel.getPrimaryControlDarkShadow() );
        g.drawLine( x + 2, h - 2, w - 2, h - 2 );
        g.drawLine( w - 2, y + 2, w - 2, h - 2 );
    }

    /**
     * Modified edges.
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param active
     */
    private static void drawDefaultButtonBorder( Graphics g, int x, int y,
        int w, int h, boolean active )
    {
        drawButtonBorder( g, x + 1, y + 1, w - 1, h - 1, active );
        g.translate( x, y );
        g.setColor( PlasticLookAndFeel.getControlDarkShadow() );
        drawRect( g, 0, 0, w - 3, h - 3 );
        g.drawLine( w - 2, 0, w - 2, 0 );
        g.drawLine( 0, h - 2, 0, h - 2 );
        g.setColor( PlasticLookAndFeel.getControl() );
        g.drawLine( w - 1, 0, w - 1, 0 );
        g.drawLine( 0, h - 1, 0, h - 1 );
        g.translate( -x, -y );
    }

    private static void drawDefaultButtonPressedBorder( Graphics g, int x,
        int y, int w, int h )
    {
        drawPressed3DBorder( g, x + 1, y + 1, w - 1, h - 1 );
        g.translate( x, y );
        g.setColor( PlasticLookAndFeel.getControlDarkShadow() );
        drawRect( g, 0, 0, w - 3, h - 3 );
        g.drawLine( w - 2, 0, w - 2, 0 );
        g.drawLine( 0, h - 2, 0, h - 2 );
        g.setColor( PlasticLookAndFeel.getControl() );
        g.drawLine( w - 1, 0, w - 1, 0 );
        g.drawLine( 0, h - 1, 0, h - 1 );
        g.translate( -x, -y );
    }

    // Low level graphics ***************************************************

    /**
     * An optimized version of Graphics.drawRect.
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     */
    private static void drawRect( Graphics g, int x, int y, int w, int h )
    {
        g.fillRect( x, y, w + 1, 1 );
        g.fillRect( x, y + 1, 1, h );
        g.fillRect( x + 1, y + h, w, 1 );
        g.fillRect( x + w, y + 1, 1, h );
    }

    /**
     * 
     */
    private static class ButtonBorder extends AbstractBorder
        implements UIResource
    {
        /**  */
        private static final long serialVersionUID = 1L;

        /**  */
        protected final Insets insets;

        /**
         * @param insets
         */
        protected ButtonBorder( Insets insets )
        {
            this.insets = insets;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void paintBorder( Component c, Graphics g, int x, int y, int w,
            int h )
        {
            AbstractButton button = ( AbstractButton )c;
            ButtonModel model = button.getModel();

            if( model.isEnabled() )
            {
                boolean isPressed = model.isPressed() && model.isArmed();
                boolean isDefault = button instanceof JButton &&
                    ( ( JButton )button ).isDefaultButton();

                if( isPressed && isDefault )
                {
                    drawDefaultButtonPressedBorder( g, x, y, w, h );
                }
                else if( isPressed )
                {
                    drawPressed3DBorder( g, x, y, w, h );
                }
                else if( isDefault )
                {
                    drawDefaultButtonBorder( g, x, y, w, h, false );
                }
                else
                {
                    drawButtonBorder( g, x, y, w, h, false );
                }
            }
            else
            {
                // disabled state
                drawDisabledBorder( g, x, y, w - 1, h - 1 );
            }
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Insets getBorderInsets( Component c )
        {
            return insets;
        }

        /**
         * @{@inheritDoc}
         */
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RolloverButtonBorder extends ButtonBorder
    {
        /**  */
        private static final long serialVersionUID = 1L;

        /**
         * 
         */
        private RolloverButtonBorder()
        {
            super( new Insets( 3, 3, 3, 3 ) );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void paintBorder( Component c, Graphics g, int x, int y, int w,
            int h )
        {
            AbstractButton b = ( AbstractButton )c;
            ButtonModel model = b.getModel();

            if( !model.isEnabled() )
            {
                return;
            }

            if( !( c instanceof JToggleButton ) )
            {
                if( model.isRollover() &&
                    !( model.isPressed() && !model.isArmed() ) )
                {
                    super.paintBorder( c, g, x, y, w, h );
                }
                return;
            }

            // if ( model.isRollover() && !( model.isPressed() &&
            // !model.isArmed() ) ) {
            // super.paintBorder( c, g, x, y, w, h );
            // }

            if( model.isRollover() )
            {
                if( model.isPressed() && model.isArmed() )
                {
                    drawPressed3DBorder( g, x, y, w, h );
                }
                else
                {
                    drawFlush3DBorder( g, x, y, w, h );
                }
            }
            else if( model.isSelected() )
            {
                drawDark3DBorder( g, x, y, w, h );
            }
        }
    }

    /**
     * A border which is like a Margin border but it will only honor the margin
     * if the margin has been explicitly set by the developer.
     */
    private static final class RolloverMarginBorder extends EmptyBorder
    {
        /**  */
        private static final long serialVersionUID = 1L;

        /**
         * 
         */
        RolloverMarginBorder()
        {
            super( 1, 1, 1, 1 );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Insets getBorderInsets( Component c )
        {
            return getBorderInsets( c, new Insets( 0, 0, 0, 0 ) );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Insets getBorderInsets( Component c, Insets insets )
        {
            Insets margin = null;

            if( c instanceof AbstractButton )
            {
                margin = ( ( AbstractButton )c ).getMargin();
            }
            if( margin == null || margin instanceof UIResource )
            {
                // default margin so replace
                insets.left = left;
                insets.top = top;
                insets.right = right;
                insets.bottom = bottom;
            }
            else
            {
                // Margin which has been explicitly set by the user.
                insets.left = margin.left;
                insets.top = margin.top;
                insets.right = margin.right;
                insets.bottom = margin.bottom;
            }
            return insets;
        }
    }
}
