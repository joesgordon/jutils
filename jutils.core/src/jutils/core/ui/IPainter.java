package jutils.core.ui;

import java.awt.Graphics2D;

/*******************************************************************************
 * Defines a callback that paints using a {@link Graphics2D}.
 ******************************************************************************/
public interface IPainter
{
    /***************************************************************************
     * Called to paint using the provided graphics.
     * @param g the graphics used to paint.
     **************************************************************************/
    void paint( Graphics2D g );
}
