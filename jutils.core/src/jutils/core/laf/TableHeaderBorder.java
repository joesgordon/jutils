package jutils.core.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TableHeaderBorder extends javax.swing.border.AbstractBorder
{
    /**  */
    private static final long serialVersionUID = -5540118410744649882L;

    /** The border insets. */
    protected Insets editorBorderInsets = new Insets( 2, 2, 2, 0 );

    /**  */
    private final Color shadow;
    /**  */
    private final Color highlight;

    /***************************************************************************
     * @param shadow
     * @param highlight
     **************************************************************************/
    public TableHeaderBorder( Color shadow, Color highlight )
    {
        this.shadow = shadow;
        this.highlight = highlight;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintBorder( Component c, Graphics g, int x, int y, int w,
        int h )
    {
        g.translate( x, y );

        g.setColor( shadow );
        g.drawLine( w - 1, 0, w - 1, h - 1 );
        g.drawLine( 1, h - 1, w - 1, h - 1 );

        g.setColor( highlight );
        g.drawLine( 0, 0, w - 2, 0 );
        g.drawLine( 0, 0, 0, h - 2 );

        g.translate( -x, -y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Insets getBorderInsets( Component c, Insets nset )
    {
        nset.set( nset.top, nset.left, nset.bottom, nset.right );
        return nset;
    }
}
