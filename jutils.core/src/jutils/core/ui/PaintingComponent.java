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
     * Creates a new {@link JPanel} that will be
     * {@link IPaintable#paint(JComponent, Graphics2D) drawn} according to the
     * provided callback using high-quality hints.
     * @param paintable the callback invoked on
     * @see #setHighQualityRendering(Graphics2D)
     **************************************************************************/
    public PaintingComponent( IPaintable paintable )
    {
        this( paintable, true );
    }

    /***************************************************************************
     * Creates a new {@link JPanel} that will be
     * {@link IPaintable#paint(JComponent, Graphics2D) drawn} according to the
     * provided callback using high-quality hints if specified.
     * @param paintable the callback invoked on
     * @param setHighQuality sets high-quality hints if specified.
     * @see #setHighQualityRendering(Graphics2D)
     **************************************************************************/
    public PaintingComponent( IPaintable paintable, boolean setHighQuality )
    {
        this.paintable = setHighQuality ? new HighQualityPaintable( paintable )
            : paintable;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintComponent( Graphics graphics )
    {
        super.paintComponent( graphics );

        Graphics2D g = ( Graphics2D )graphics;

        setHighQualityRendering( g );

        paintable.paint( this, g );
    }

    /***************************************************************************
     * Sets the following hints: <ul>
     * <li>{@link RenderingHints#KEY_ALPHA_INTERPOLATION Alpha Interpolation} to
     * {@link RenderingHints#VALUE_ALPHA_INTERPOLATION_QUALITY Quality}</li>
     * <li>{@link RenderingHints#KEY_ANTIALIASING Antialiasing} to
     * {@link RenderingHints#VALUE_ANTIALIAS_ON On}</li>
     * <li>{@link RenderingHints#KEY_COLOR_RENDERING Color Rendering} to
     * {@link RenderingHints#VALUE_COLOR_RENDER_QUALITY Quality}</li>
     * <li>{@link RenderingHints#KEY_DITHERING Key Dithering} to
     * {@link RenderingHints#VALUE_DITHER_ENABLE Enable}</li>
     * <li>{@link RenderingHints#KEY_FRACTIONALMETRICS Fractional Metrics} to
     * {@link RenderingHints#VALUE_FRACTIONALMETRICS_ON On}</li>
     * <li>{@link RenderingHints#KEY_INTERPOLATION Interpolation} to
     * {@link RenderingHints#VALUE_INTERPOLATION_BICUBIC Bicubic}</li>
     * <li>{@link RenderingHints#KEY_RENDERING Rendering} to
     * {@link RenderingHints#VALUE_RENDER_QUALITY Quality}</li>
     * <li>{@link RenderingHints#KEY_STROKE_CONTROL Stroke Control} to
     * {@link RenderingHints#VALUE_STROKE_PURE Pure}</li>
     * <li>{@link RenderingHints#KEY_TEXT_ANTIALIASING Text Antialiasing} to
     * {@link RenderingHints#VALUE_TEXT_ANTIALIAS_ON On}</li> </ul>
     * @param g the graphics to be updated.
     **************************************************************************/
    public static void setHighQualityRendering( Graphics2D g )
    {
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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class HighQualityPaintable implements IPaintable
    {
        /**  */
        private final IPaintable paintable;

        /**
         * @param paintable
         */
        public HighQualityPaintable( IPaintable paintable )
        {
            this.paintable = paintable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            setHighQualityRendering( g );
            paintable.paint( c, g );
        }
    }
}
