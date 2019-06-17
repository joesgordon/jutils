package org.jutils.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class GradientButtonUI extends BasicButtonUI
{
    private static final int ROUNDEDNESS = 16; // 15;
    private Color color1, color2, colorMouseHover, colorMouseClick, colorFocus;
    private static GradientButtonUI gbui;

    static
    {
        gbui = new GradientButtonUI();
    }

    public GradientButtonUI()
    {
        this.color1 = new Color( 0xCDCDCD );
        this.color2 = new Color( 0x050505 );
        this.colorMouseHover = new Color( 192, 192, 192, 100 /* alpha */ );
        this.colorMouseClick = new Color( 90, 90, 90, 100 /* alpha */ );
        this.colorFocus = new Color( 0xE0E0E0 );
    }

    public static ComponentUI createUI( JComponent c )
    {
        return gbui;
    }

    @Override
    public void update( Graphics g, JComponent c )
    {
        if( c.isOpaque() )
        {
            Graphics2D g2 = ( Graphics2D )g;

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
            g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

            // Create a gradient paint with the colors provided.
            GradientPaint gradient;
            gradient = new GradientPaint( 0, 0, color1, 0,
                c.getHeight() - c.getHeight() / 3, color2 );

            Paint oldPaint = g2.getPaint();
            g2.setPaint( gradient );
            g2.fillRoundRect( 0, 0, c.getWidth(), c.getHeight(), ROUNDEDNESS,
                ROUNDEDNESS );
            g2.setPaint( oldPaint );

            if( c.hasFocus() )
            {
                g2.setColor( colorFocus );
                g2.drawRoundRect( 0, 0, c.getWidth() - 1, c.getHeight() - 1,
                    ROUNDEDNESS, ROUNDEDNESS );
            }
        }

        paint( g, c );
    }

    @Override
    protected void paintFocus( Graphics g, AbstractButton b, Rectangle viewRect,
        Rectangle textRect, Rectangle iconRect )
    {
        super.paintFocus( g, b, viewRect, textRect, iconRect );
    }

    @Override
    public void paint( Graphics g, JComponent c )
    {
        JButton b = ( JButton )c;
        ButtonModel model = b.getModel();

        // Check for mouse hovering
        if( model.isRollover() )
        {
            g.setColor( colorMouseHover );
            // Draw semi-transparent rectangle to indicate a mouse hover.
            g.fillRoundRect( 0, 0, c.getWidth(), c.getHeight(), 15, 15 );
        }

        // Check for click events
        if( model.isPressed() )
        {
            g.setColor( colorMouseClick );
            // Draw semi-transparent rectangle to indicate a mouse hover.
            g.fillRoundRect( 0, 0, c.getWidth(), c.getHeight(), 15, 15 );
        }

        super.paint( g, c );
    }
}
