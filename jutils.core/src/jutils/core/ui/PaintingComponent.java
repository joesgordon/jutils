package jutils.core.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );

        Graphics2D g = ( Graphics2D )graphics;

        g.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );
        g.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING,
            RenderingHints.VALUE_COLOR_RENDER_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_DITHERING,
            RenderingHints.VALUE_DITHER_ENABLE );
        g.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON );
        g.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g.setRenderingHint( RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY );
        g.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL,
            RenderingHints.VALUE_STROKE_PURE );
        g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        paintable.paint( this, g );
    }
}
