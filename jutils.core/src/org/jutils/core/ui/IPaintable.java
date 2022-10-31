package org.jutils.core.ui;

import java.awt.Graphics2D;

import javax.swing.JComponent;

/*******************************************************************************
 * Defines a callback that paints on a {@link JComponent}.
 ******************************************************************************/
public interface IPaintable
{
    /***************************************************************************
     * Called when the provided component is to be painted using the provided
     * graphics.
     * @param c the component to be painted.
     * @param g the graphics use to paint.
     **************************************************************************/
    void paint( JComponent c, Graphics2D g );
}
