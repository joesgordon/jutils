package jutils.iris.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProfileView implements IView<JComponent>
{
    /**  */
    private final boolean isHorizontal;
    /**  */
    private final PaintingComponent view;

    /**  */
    private final List<IProfile> profiles;

    /**  */
    private Dimension size;

    /***************************************************************************
     * 
     **************************************************************************/
    public ProfileView()
    {
        this( true );
    }

    /***************************************************************************
     * @param isHorizontal
     **************************************************************************/
    public ProfileView( boolean isHorizontal )
    {
        this.isHorizontal = isHorizontal;
        this.view = new PaintingComponent( ( c, g ) -> paint( c, g ) );
        this.profiles = new ArrayList<>();
        this.size = null;
    }

    /***************************************************************************
     * @param comp
     * @param g
     **************************************************************************/
    private void paint( JComponent comp, Graphics2D g )
    {
        Dimension c = size == null ? comp.getPreferredSize() : size;
        int h = isHorizontal ? c.height : c.width;
        int w = isHorizontal ? c.width : c.height;

        AffineTransform transform = g.getTransform();

        if( !isHorizontal )
        {
            double rx = w / 2.0;
            // double ry = ( c.getHeight() + c.getWidth() + 60 ) / 2.0;
            AffineTransform r = new AffineTransform();

            // r.setTrans6form( new AffineTransform() );

            // r.translate( 11, c.getHeight() + ry - 25 );
            // r.rotate( Math.toRadians( -90 ) );
            // r.rotate( Math.toRadians( -90 ), 291, w - h - 4 );

            // r.rotate( Math.toRadians( -90 ), ry, ry );
            // r.translate( 176, 0 );

            r.rotate( Math.toRadians( -90 ), rx, rx );
            // r.translate( 0, 0 );
            // r.translate( -284, 0 );

            g.setTransform( r );
        }

        g.setColor( Color.white );
        g.fillRect( 0, 0, w, h );

        g.setColor( Color.black );
        g.drawRect( 1, 1, w - 1, h - 1 );

        for( IProfile profile : profiles )
        {
            paintProfile( profile, g, w, h );
        }

        g.setTransform( transform );
    }

    /***************************************************************************
     * @param profile
     * @param g
     * @param w
     * @param h
     **************************************************************************/
    private void paintProfile( IProfile profile, Graphics2D g, int w, int h )
    {
        FontMetrics m = g.getFontMetrics();

        boolean isMajor = true;

        for( int i = 0; i <= profile.getCount(); i++ )
        {
            int screenx = i;
            int n = isHorizontal ? i : imgLen - i;

            if( n == imgLen )
            {
                n--;
            }

            if( isMajor )
            {
                String str = "" + n;
                int sw = m.stringWidth( str );
                // int x = i - Math.round( sw / 2.0f );
                int strx = ( int )Math.ceil( i * cfg.getZoomScale() - sw );

                strx = strx < 0 ? 0 : strx;
                strx = ( strx + sw ) > w ? w - sw : strx;

                g.drawLine( screenx, h - RULER_SIZE + 6, screenx, h - 1 );

                g.drawString( str, strx, PROFILE_SIZE + m.getHeight() - 2 );
            }
            else
            {
                g.drawLine( screenx, h - RULER_SIZE + 10, screenx, h - 1 );
            }

            isMajor = !isMajor;
        }

        if( strip > -1 )
        {
            int range = stripMax - stripMin + 1;
            range = Math.max( 1, range );
            float ys = PROFILE_SIZE / ( float )range;
            float min = ys * stripMin;

            int lastPy = -1;
            for( int i = 0; i < stripPixels.length; i++ )
            {
                int pixel = stripPixels[i];

                pixel = Math.min( maxPixel, pixel );

                int py = PROFILE_SIZE - Math.round( pixel * ys - min );
                // g.drawRect( i, py, 1, 1 );

                if( lastPy > -1 )
                {
                    int x1 = ( int )Math.ceil( ( i - 1 ) * cfg.zoomLevel );
                    int x2 = ( int )Math.ceil( i * cfg.zoomLevel );
                    g.drawLine( x1, lastPy, x2, py );
                }
                lastPy = py;
            }
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public interface IProfile
    {
        /**
         * @return
         */
        public int getMin();

        /**
         * @return
         */
        public int getMax();

        /**
         * @return
         */
        public int getCount();

        /**
         * @param n
         * @return
         */
        public int getValue( int n );

        /**
         * @return
         */
        public Color getColor();
    }
}
