package jutils.colorific;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import jutils.core.ui.ShadowBorder;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ZoomView implements IView<JLabel>
{
    /**
     * The number of pixels of a side of a zoom area including the center pixel.
     */
    private static final int CAP_LEN = 8 + 1 + 8;
    /**  */
    private static final int PIXEL_SIZE = 9;
    /**  */
    public static final int IMG_LEN = PIXEL_SIZE * CAP_LEN;
    /**  */
    private static final int LINE_W = 3;
    /**  */
    private static final int CAP_HALF = PIXEL_SIZE / 2;
    /**  */
    private static final int CH_MIN = CAP_LEN * CAP_HALF - LINE_W / 2;
    /**  */
    private static final int CH_MAX = CAP_LEN * CAP_HALF - LINE_W / 2 + CAP_LEN;

    /**  */
    private final Stroke solidStroke;
    /**  */
    private final JLabel zoomLabel;

    /***************************************************************************
     * 
     **************************************************************************/
    public ZoomView()
    {
        this.solidStroke = new BasicStroke( LINE_W, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_ROUND );
        this.zoomLabel = new JLabel();

        zoomLabel.setPreferredSize( new Dimension( IMG_LEN, IMG_LEN ) );
        zoomLabel.setBorder( new ShadowBorder() );
    }

    /***************************************************************************
     * @param p
     * @param bot
     * @param swatch
     * @param colorLabel
     **************************************************************************/
    public void copyArea( Point p, Robot bot, SwatchView swatch,
        JLabel colorLabel )
    {
        Color c = bot.getPixelColor( p.x, p.y );

        swatch.setColor( c );
        colorLabel.setText( String.format( "#%06X", c.getRGB() & 0x00FFFFFF ) );

        Rectangle sr = new Rectangle( p.x - CAP_HALF, p.y - CAP_HALF,
            PIXEL_SIZE, PIXEL_SIZE );
        BufferedImage image = bot.createScreenCapture( sr );
        BufferedImage resizedImage = new BufferedImage( IMG_LEN, IMG_LEN,
            BufferedImage.TYPE_INT_RGB );
        Graphics2D g2 = resizedImage.createGraphics();

        g2.drawImage( image, 0, 0, IMG_LEN, IMG_LEN, null );

        g2.setStroke( solidStroke );
        g2.setColor( new Color( 255 - c.getRed(), 255 - c.getGreen(),
            255 - c.getBlue() ) );

        g2.drawLine( CH_MIN, CH_MIN, CH_MIN, CH_MAX );
        g2.drawLine( CH_MIN, CH_MIN, CH_MAX, CH_MIN );
        g2.drawLine( CH_MAX, CH_MIN, CH_MAX, CH_MAX );
        g2.drawLine( CH_MIN, CH_MAX, CH_MAX, CH_MAX );

        g2.dispose();

        ImageIcon icon = new ImageIcon( resizedImage );
        zoomLabel.setIcon( icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        zoomLabel.setIcon( null );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public JLabel getView()
    {
        return zoomLabel;
    }
}
