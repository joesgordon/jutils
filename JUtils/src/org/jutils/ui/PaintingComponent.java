package org.jutils.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PaintingComponent extends JComponent
{
    /**  */
    private static final long serialVersionUID = -4340666978067812946L;
    /**  */
    private final IPaintable paintable;

    /***************************************************************************
     * @param paintable
     **************************************************************************/
    public PaintingComponent( IPaintable paintable )
    {
        this.paintable = paintable;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        paintable.paint( this, ( Graphics2D )g );
    }
}
