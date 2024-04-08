package jutils.iris.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jutils.core.ui.IPaintable;
import jutils.core.ui.PaintingComponent;
import jutils.core.ui.event.RightClickListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;
import jutils.iris.IrisUtils;
import jutils.iris.data.DirectImage;
import jutils.iris.data.RasterImage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HoverView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final HoverPaintable hoverPaintable;
    /**  */
    private final PaintingComponent hoverView;
    /**  */
    private final JLabel hoverLabel;

    /**  */
    private DirectImage hoverImage;
    /** Size of the area hovered. */
    private int hoverSize;

    /***************************************************************************
     * @param hoverLabel
     **************************************************************************/
    public HoverView()
    {
        this.hoverPaintable = new HoverPaintable();
        this.hoverView = new PaintingComponent( hoverPaintable );
        this.hoverLabel = new JLabel( "N/A" );
        this.view = createView();

        hoverView.setBorder( new LineBorder( IrisUtils.BORDER_COLOR ) );

        setHover( 9, 32 );

        resetCapture();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        panel.setOpaque( false );

        int row = 0;

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 0, 0 );
        panel.add( hoverView, constraints );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets( 0, 10, 0, 0 ), 0, 0 );
        panel.add( hoverLabel, constraints );

        return panel;
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
     * @param callback
     **************************************************************************/
    public void addRightClick( IUpdater<MouseEvent> callback )
    {
        hoverView.addMouseListener( new RightClickListener( callback ) );
    }

    /***************************************************************************
     * @param hoverSize
     * @param pixelSize
     **************************************************************************/
    public void setHover( int hoverSize, int pixelSize )
    {
        this.hoverSize = hoverSize;

        this.hoverImage = new DirectImage( hoverSize, hoverSize );
        this.hoverPaintable.setImage( hoverImage );

        int hoverSideLen = pixelSize * hoverSize;

        Dimension dim = new Dimension( hoverSideLen, hoverSideLen );

        this.hoverView.setPreferredSize( dim );
        this.hoverView.setMinimumSize( dim );
        this.hoverView.setMaximumSize( dim );
        this.hoverPaintable.setHoverPixelSize( pixelSize );

        hoverView.invalidate();
        hoverView.validate();
        hoverView.repaint();
        view.invalidate();
        view.validate();
        view.repaint();
        if( view.getParent() != null )
        {
            view.getParent().invalidate();
            view.getParent().validate();
        }
        resetCapture();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHoverSize()
    {
        return hoverSize;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getPixelSize()
    {
        return hoverPaintable.hoverPixelSize;
    }

    /***************************************************************************
     * @param x
     * @param y
     * @param rimage
     **************************************************************************/
    public void captureHoverImage( int x, int y, RasterImage rimage )
    {
        DirectImage image = rimage.getImage();

        if( x < 0 || x >= image.width || y < 0 || y >= image.height )
        {
            resetCapture();

            return;
        }

        int offset = ( hoverSize ) / 2;

        int xs = x - offset;
        int ys = y - offset;

        for( int xi = 0; xi < hoverSize; xi++ )
        {
            for( int yi = 0; yi < hoverSize; yi++ )
            {
                int pixel = 0;
                int xp = xs + xi;
                int yp = ys + yi;

                if( xp > -1 && yp > -1 && xp < image.width &&
                    yp < image.height )
                {
                    pixel = image.getPixel( xp, yp );
                }
                else
                {
                    boolean xeven = ( xp % 2 ) == 0;
                    boolean yeven = ( yp % 2 ) == 0; // like omg!
                    if( xeven != yeven )
                    {
                        pixel = IrisUtils.DARK_CHECKER.getRGB();
                    }
                    else
                    {
                        pixel = IrisUtils.DARK_CHECKER.getRGB();
                    }
                }

                hoverImage.setPixel( xi, yi, pixel );
            }
        }

        long px = rimage.getRaster().getPixel( x, y );
        hoverLabel.setText( x + ", " + y + " = " + px );
        hoverView.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void resetCapture()
    {
        int [] pixels = hoverImage.pixels;

        for( int i = 0; i < pixels.length; i++ )
        {
            if( ( i % 2 ) == 0 )
            {
                pixels[i] = IrisUtils.DARK_CHECKER.getRGB();
            }
            else
            {
                pixels[i] = IrisUtils.LIGHT_CHECKER.getRGB();
            }
        }

        hoverView.repaint();
        hoverLabel.setText( "N/A" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HoverPaintable implements IPaintable
    {
        /**  */
        private int hoverPixelSize;
        /**  */
        private DirectImage image;

        /**
         * @param view
         */
        public HoverPaintable()
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g )
        {
            int w = c.getWidth();
            int h = c.getHeight();

            for( int x = 0; x < image.width; x++ )
            {
                for( int y = 0; y < image.height; y++ )
                {
                    int pixel = image.getPixel( x, y );
                    Color clr = new Color( pixel );
                    g.setColor( clr );
                    g.fillRect( x * hoverPixelSize, y * hoverPixelSize,
                        hoverPixelSize, hoverPixelSize );
                }
            }

            int boxx = w / 2 - 1 - hoverPixelSize / 2;
            int boxy = h / 2 - 1 - hoverPixelSize / 2;

            int x;
            int y;

            g.setColor( Color.yellow );

            x = w / 2 - 1;
            y = 0;
            g.fillRect( x, y, 2, boxy );

            x = 0;
            y = h / 2 - 1;
            g.fillRect( x, y, boxx, 2 );

            x = w / 2 - 1;
            y = boxy + hoverPixelSize + 2;
            g.fillRect( x, y, 2, boxy );

            x = boxx + hoverPixelSize + 2;
            y = h / 2 - 1;
            g.fillRect( x, y, boxx, 2 );

            g.setStroke( new BasicStroke( 2, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER ) );
            g.drawRect( boxx, boxy, hoverPixelSize + 2, hoverPixelSize + 2 );
        }

        /**
         * @param hoverPixelSize
         */
        public void setHoverPixelSize( int hoverPixelSize )
        {
            this.hoverPixelSize = hoverPixelSize;
        }

        /**
         * @param image
         */
        public void setImage( DirectImage image )
        {
            this.image = image;
        }
    }
}
