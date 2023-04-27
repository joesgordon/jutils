package org.jutils.core.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

/*******************************************************************************
 * Defines a component that will paint using an {@link IPaintable}.
 ******************************************************************************/
public class PaintingComponent extends JPanel
{
    /** Generated UID. */
    private static final long serialVersionUID = -4340666978067812946L;

    /** The callback invoked on {@link #paintComponent(Graphics)}. */
    private final IPaintable paintable;

    /***************************************************************************
     * Creates a new {@link JComponent} that will be drawn according to the
     * provided callback.
     * @param paintable the callback invoked on
     * {@link #paintComponent(Graphics)}.
     **************************************************************************/
    public PaintingComponent( IPaintable paintable )
    {
        this.paintable = paintable;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        paintable.paint( this, ( Graphics2D )g );
    }
}
