package jutils.core.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jutils.core.io.ResourceLoader;
import jutils.core.ui.ColorMapView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * Defines a test application for {@link ColorMapView}
 ******************************************************************************/
public class ColorMapMain
{
    /***************************************************************************
     * The application entry point.
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setTitle( "Color Map Test" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 550, 800 );
        frameView.setContent( createView() );

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Container createView()
    {
        ColorMapView mapView = new ColorMapView();

        BufferedImage img = getTestImage();
        mapView.setImage( img );

        int [] pixel = new int[1];
        for( int x = 0; x < img.getRaster().getWidth(); x++ )
        {
            for( int y = 0; y < img.getRaster().getHeight(); y++ )
            {
                img.getRaster().getPixel( x, y, pixel );
            }
        }

        JPanel view = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( mapView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 2, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( Box.createHorizontalStrut( 0 ), constraints );

        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static BufferedImage getTestImage()
    {
        ResourceLoader rl = new ResourceLoader( ColorMapMain.class, "." );

        try( InputStream is = rl.getInputStream( "pollen.bmp" ) )
        {
            return ImageIO.read( is );
        }
        catch( IOException ex )
        {
            throw new IllegalStateException( ex );
        }
    }
}
