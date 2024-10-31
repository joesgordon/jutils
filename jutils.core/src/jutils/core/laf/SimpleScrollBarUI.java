package jutils.core.laf;

import static java.awt.geom.AffineTransform.TYPE_FLIP;
import static java.awt.geom.AffineTransform.TYPE_TRANSLATION;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimpleScrollBarUI extends BasicScrollBarUI
{
    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleScrollBarUI()
    {
        super();
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    public static ComponentUI createUI( JComponent c )
    {
        return new SimpleScrollBarUI();
    }

    /**
     * Creates a decrease button.
     * @param orientation the orientation
     * @return a decrease button
     */
    @Override
    protected JButton createDecreaseButton( int orientation )
    {
        return new BasicArrowButton2( orientation,
            UIManager.getColor( "ScrollBar.thumb" ),
            UIManager.getColor( "ScrollBar.thumbShadow" ),
            UIManager.getColor( "ScrollBar.thumbDarkShadow" ),
            UIManager.getColor( "ScrollBar.thumbHighlight" ) );
    }

    /**
     * Creates an increase button.
     * @param orientation the orientation
     * @return an increase button
     */
    @Override
    protected JButton createIncreaseButton( int orientation )
    {
        return new BasicArrowButton2( orientation,
            UIManager.getColor( "ScrollBar.thumb" ),
            UIManager.getColor( "ScrollBar.thumbShadow" ),
            UIManager.getColor( "ScrollBar.thumbDarkShadow" ),
            UIManager.getColor( "ScrollBar.thumbHighlight" ) );
    }

    /**
     * 
     */
    private static class BasicArrowButton2 extends BasicArrowButton
    {
        /**  */
        private static final long serialVersionUID = 895998808619413255L;

        /**  */
        private final Color shadow;
        /**  */
        private final Color highlight;

        /**
         * @param direction
         * @param background
         * @param shadow
         * @param darkShadow
         * @param highlight
         */
        public BasicArrowButton2( int direction, Color background, Color shadow,
            Color darkShadow, Color highlight )
        {
            super( direction, background, shadow, darkShadow, highlight );

            this.shadow = shadow;
            this.highlight = highlight;
        }

        /**
         * @param g
         * @return
         */
        public static boolean isScaledGraphics( Graphics g )
        {
            if( g instanceof Graphics2D )
            {
                AffineTransform tx = ( ( Graphics2D )g ).getTransform();
                return ( tx.getType() &
                    ~( TYPE_TRANSLATION | TYPE_FLIP ) ) != 0;
            }
            return false;
        }

        /**
         * Paints a triangle.
         * @param g the {@code Graphics} to draw to
         * @param x the x coordinate
         * @param y the y coordinate
         * @param size the size of the triangle to draw
         * @param direction the direction in which to draw the arrow; one of
         * {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
         * {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
         * @param isEnabled whether or not the arrow is drawn enabled
         */
        @Override
        public void paintTriangle( Graphics g, int x, int y, int size,
            int direction, boolean isEnabled )
        {
            if( isScaledGraphics( g ) )
            {
                paintScaledTriangle( g, x, y, size, direction, isEnabled );
            }
            else
            {
                paintUnscaledTriangle( g, x, y, size, direction, isEnabled );
            }
        }

        /**
         * @param g
         * @param x
         * @param y
         * @param size
         * @param direction
         * @param isEnabled
         */
        private void paintUnscaledTriangle( Graphics g, int x, int y, int size,
            int direction, boolean isEnabled )
        {
            Color oldColor = g.getColor();
            int mid, i, j;

            j = 0;
            size = Math.max( size, 2 );
            mid = ( size / 2 ) - 1;

            g.translate( x, y );

            if( isEnabled )
                g.setColor( highlight );
            else
                g.setColor( shadow );

            switch( direction )
            {
                case NORTH:
                    for( i = 0; i < size; i++ )
                    {
                        g.drawLine( mid - i, i, mid + i, i );
                    }
                    if( !isEnabled )
                    {
                        g.setColor( highlight );
                        g.drawLine( mid - i + 2, i, mid + i, i );
                    }
                    break;
                case SOUTH:
                    if( !isEnabled )
                    {
                        g.translate( 1, 1 );
                        g.setColor( highlight );
                        for( i = size - 1; i >= 0; i-- )
                        {
                            g.drawLine( mid - i, j, mid + i, j );
                            j++;
                        }
                        g.translate( -1, -1 );
                        g.setColor( shadow );
                    }

                    j = 0;
                    for( i = size - 1; i >= 0; i-- )
                    {
                        g.drawLine( mid - i, j, mid + i, j );
                        j++;
                    }
                    break;
                case WEST:
                    for( i = 0; i < size; i++ )
                    {
                        g.drawLine( i, mid - i, i, mid + i );
                    }
                    if( !isEnabled )
                    {
                        g.setColor( highlight );
                        g.drawLine( i, mid - i + 2, i, mid + i );
                    }
                    break;
                case EAST:
                    if( !isEnabled )
                    {
                        g.translate( 1, 1 );
                        g.setColor( highlight );
                        for( i = size - 1; i >= 0; i-- )
                        {
                            g.drawLine( j, mid - i, j, mid + i );
                            j++;
                        }
                        g.translate( -1, -1 );
                        g.setColor( shadow );
                    }

                    j = 0;
                    for( i = size - 1; i >= 0; i-- )
                    {
                        g.drawLine( j, mid - i, j, mid + i );
                        j++;
                    }
                    break;
            }
            g.translate( -x, -y );
            g.setColor( oldColor );
        }

        /**
         * @param g
         * @param x
         * @param y
         * @param size
         * @param direction
         * @param isEnabled
         */
        private void paintScaledTriangle( Graphics g, double x, double y,
            double size, int direction, boolean isEnabled )
        {
            size = Math.max( size, 2 );
            Path2D.Double path = new Path2D.Double();
            path.moveTo( -size, size / 2 );
            path.lineTo( size, size / 2 );
            path.lineTo( 0, -size / 2 );
            path.closePath();
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate( Math.PI * ( direction - 1 ) / 4 );
            path.transform( affineTransform );

            Graphics2D g2d = ( Graphics2D )g;
            double tx = x + size / 2;
            double ty = y + size / 2;
            g2d.translate( tx, ty );
            Color oldColor = g.getColor();
            if( !isEnabled )
            {
                g2d.translate( 1, 0 );
                g2d.setColor( highlight );
                g2d.fill( path );
                g2d.translate( -1, 0 );
            }
            g2d.setColor( isEnabled ? highlight : shadow );
            g2d.fill( path );
            g2d.translate( -tx, -ty );
            g2d.setColor( oldColor );
        }
    }
}
