package org.jutils.minesweeper.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.Icon;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FlagIcon implements Icon
{
    /**  */
    private int size;

    /***************************************************************************
     * @param size
     **************************************************************************/
    public FlagIcon( int size )
    {
        this.size = size;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIconHeight()
    {
        return size;
    }

    /***************************************************************************
     * 
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
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        Graphics2D graphics = ( Graphics2D )g.create( x, y, size, size );

        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        graphics.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        Paint storedPaint = graphics.getPaint();

        float tbase = 0.4f * size;
        float theight = 0.7f * size;
        float txy = 0.15f * size;

        float px = txy;
        float py = 0.05f * size;
        float pw = 0.1f * size;
        float ph = 0.9f * size;

        Polygon p;

        // ---------------------------------------------------------------------
        // Draw the triangle part of the flag.
        // ---------------------------------------------------------------------
        p = new Polygon();

        p.addPoint( Math.round( txy ), Math.round( txy ) );
        p.addPoint( Math.round( txy + theight ),
            Math.round( txy + 0.5f * tbase ) );
        p.addPoint( Math.round( txy ), Math.round( txy + tbase ) );

        graphics.setColor( Color.red );

        graphics.fill( p );

        // ---------------------------------------------------------------------
        // Draw the pole.
        // ---------------------------------------------------------------------
        graphics.fillRect( Math.round( px ), Math.round( py ), Math.round( pw ),
            Math.round( ph ) );

        // ---------------------------------------------------------------------
        // Draw debug border
        // ---------------------------------------------------------------------
        // graphics.setColor( Color.black );
        // graphics.drawRect( 0, 0, size - 1, size - 1 );

        // ---------------------------------------------------------------------
        // Restore the stored paint
        // ---------------------------------------------------------------------
        graphics.setPaint( storedPaint );

        graphics.dispose();
    }
}
