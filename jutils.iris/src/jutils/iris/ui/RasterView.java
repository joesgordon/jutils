package jutils.iris.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import jutils.core.ui.PaintingComponent;
import jutils.core.ui.model.IView;
import jutils.iris.colors.IColorizer;
import jutils.iris.data.DirectImage;
import jutils.iris.data.RasterImage;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterView implements IView<JComponent>
{
    /**  */
    private final PaintingComponent view;

    /**  */
    private final RasterImage image;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterView()
    {
        this.image = new RasterImage( 256, 256 );
        this.view = new PaintingComponent( ( c, g ) -> paint( c, g ) );
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private void paint( JComponent c, Graphics2D g )
    {
        drawCheckerboard( c, g );

        image.paint( c, g );

        // AbstractBorder bdr = new LineBorder( Color.BLACK, 2 );
        // bdr.paintBorder( c, g, 5, 5, image.width, image.height );
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private static void drawCheckerboard( JComponent c, Graphics2D g )
    {
        int s = 20;
        Color lcbbg = new Color( 0xCCCCCC );
        Color dcbbg = new Color( 0x888888 );

        for( int x = 0; x < c.getWidth(); x += s )
        {
            for( int y = 0; y < c.getHeight(); y += s )
            {
                int sum = x + y;
                boolean isLight = ( sum % ( 2 * s ) ) == 0;
                Color bg = isLight ? lcbbg : dcbbg;

                g.setColor( bg );
                g.fillRect( x, y, s, s );
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
    public void reset()
    {
        image.reset();
        view.repaint();
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void set( IRaster raster )
    {
        image.set( raster );
        view.repaint();
    }

    /***************************************************************************
     * @param raster
     * @param colorModel
     **************************************************************************/
    public void set( IRaster raster, IColorizer colorModel )
    {
        this.image.set( raster, colorModel );

        view.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IRaster getRaster()
    {
        return image.getRaster();
    }

    /**
     * @return
     */
    public DirectImage getImage()
    {
        return image.getImage();
    }
}
