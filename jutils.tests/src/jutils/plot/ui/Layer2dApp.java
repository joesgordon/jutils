package jutils.plot.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.Utils;
import jutils.core.io.LogUtils;
import jutils.core.ui.JGoodiesToolBar;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.event.ActionAdapter;
import jutils.plot.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Layer2dApp implements IFrameApp
{
    /**  */
    private JLabel label;
    /**  */
    private Container content;

    public static void main( String [] args )
    {
        String laf = null;

        // laf = javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName();
        // laf = javax.swing.plaf.metal.MetalLookAndFeel.class.getName();

        AppRunner.invokeLater( new Layer2dApp(), true, laf );
    }

    public static void main2( @SuppressWarnings( "unused") String [] args )
    {
        BufferedImage img = new BufferedImage( 200, 200,
            BufferedImage.TYPE_INT_RGB );
        Dimension size = new Dimension( img.getWidth(), img.getHeight() );
        Graphics2D graphics;

        img = Utils.createTransparentImage( size.width, size.height );
        graphics = img.createGraphics();

        // testImage( img, graphics );

        Point p = new Point( size.width - 1, 0 );

        Color existing = new Color( img.getRGB( p.x, p.y ), true );
        Color expected = Color.orange;
        Color actual;

        graphics.setColor( Color.orange );
        graphics.drawLine( p.x, p.y, p.x, p.y );
        actual = new Color( img.getRGB( p.x, p.y ), true );

        BufferedImage img2 = new BufferedImage( size.width, size.height,
            BufferedImage.TYPE_INT_RGB );
        Graphics2D graphics2 = img2.createGraphics();

        graphics2.drawImage( img, 0, 0, null );
        Color actual2 = new Color( img2.getRGB( p.x, p.y ), true );

        LogUtils.printDebug(
            "Pixel @ %d, %d of [%d,%d]: existing = %s, expected = %s, actual = %s, actual2 = %s",
            p.x, p.y, size.width, size.height, existing, expected, actual,
            actual2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        this.label = new JLabel();

        label.setMinimumSize( new Dimension( 300, 300 ) );
        label.setPreferredSize( new Dimension( 300, 300 ) );

        frameView.setToolbar( createToolbar() );
        frameView.setContent( createContent() );

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 331, 550 );

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createZoomAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createZoomAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.ANALYZE_16 );

        return new ActionAdapter( ( e ) -> createSnapshot(), "Take Snapshot",
            icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createSnapshot()
    {
        Point p = content.getLocationOnScreen();
        Dimension size = content.getSize();

        // size.width += 10;
        // size.height += 10;

        Icon icon = takeSnapshot( p, size );

        if( icon == null )
        {
            return;
        }

        label.setIcon( icon );
    }

    /***************************************************************************
     * @param p
     * @param size
     * @return
     **************************************************************************/
    private static Icon takeSnapshot( Point p, Dimension size )
    {
        Robot bot;

        try
        {
            bot = new Robot();
        }
        catch( AWTException e )
        {
            return null;
        }

        Rectangle sr = new Rectangle( p.x, p.y, size.width, size.height );
        BufferedImage capImg = bot.createScreenCapture( sr );

        BufferedImage cornerImg = getCorners( capImg );

        return new ImageIcon( resizeImage( cornerImg, 10 ) );
    }

    /***************************************************************************
     * @param img
     * @return
     **************************************************************************/
    private static BufferedImage getCorners( BufferedImage img )
    {
        Dimension imgSize = new Dimension( img.getWidth(), img.getHeight() );
        Dimension regionSize = new Dimension( 15, 15 );
        BufferedImage cornerImg = new BufferedImage( 2 * regionSize.width,
            3 * regionSize.height, BufferedImage.TYPE_INT_RGB );
        int x1 = 0;
        int x2 = imgSize.width - regionSize.width;
        int y1 = 0;
        int y2 = ( int )( imgSize.height / 2.0 - regionSize.height / 2.0 ) + 2;
        int y3 = imgSize.height - regionSize.height;
        Point srcPnt;
        Point dstPnt;

        // ---------------------------------------------------------------------
        // Row 1
        // ---------------------------------------------------------------------
        srcPnt = new Point( x1, y1 );
        dstPnt = new Point( 0, 0 );
        copyRegion( img, cornerImg, srcPnt, regionSize, dstPnt );

        srcPnt = new Point( x2, y1 );
        dstPnt = new Point( regionSize.width, 0 );
        copyRegion( img, cornerImg, srcPnt, regionSize, dstPnt );

        // ---------------------------------------------------------------------
        // Row 2
        // ---------------------------------------------------------------------
        srcPnt = new Point( x1, y2 );
        dstPnt = new Point( 0, regionSize.height );
        copyRegion( img, cornerImg, srcPnt, regionSize, dstPnt );

        srcPnt = new Point( x2, y2 );
        dstPnt = new Point( regionSize.width, regionSize.height );
        copyRegion( img, cornerImg, srcPnt, regionSize, dstPnt );

        // ---------------------------------------------------------------------
        // Row 3
        // ---------------------------------------------------------------------
        srcPnt = new Point( x1, y3 );
        dstPnt = new Point( 0, regionSize.height * 2 );
        copyRegion( img, cornerImg, srcPnt, regionSize, dstPnt );

        srcPnt = new Point( x2, y3 );
        dstPnt = new Point( regionSize.width, regionSize.height * 2 );
        copyRegion( img, cornerImg, srcPnt, regionSize, dstPnt );

        return cornerImg;
    }

    /***************************************************************************
     * @param srcImg
     * @param dstImg
     * @param srcPnt
     * @param size
     * @param dstPnt
     **************************************************************************/
    private static void copyRegion( BufferedImage srcImg, BufferedImage dstImg,
        Point srcPnt, Dimension size, Point dstPnt )
    {
        BufferedImage subImg = srcImg.getSubimage( srcPnt.x, srcPnt.y,
            size.width, size.height );
        dstImg.getGraphics().drawImage( subImg, dstPnt.x, dstPnt.y, null );
    }

    /***************************************************************************
     * @param img
     * @param factor
     * @return
     **************************************************************************/
    private static BufferedImage resizeImage( BufferedImage img, int factor )
    {
        Dimension size = new Dimension( img.getWidth(), img.getHeight() );
        Dimension resize = new Dimension( size.width * factor,
            size.height * factor );
        BufferedImage resizedImage = new BufferedImage( resize.width,
            resize.height, BufferedImage.TYPE_INT_RGB );
        Graphics2D g2 = resizedImage.createGraphics();

        g2.drawImage( img, 0, 0, resize.width, resize.height, null );

        return resizedImage;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        this.content = createItems();

        panel.add( content, BorderLayout.CENTER );
        panel.add( label, BorderLayout.EAST );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JPanel createItems()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        Component directComp = new TestComp( false );
        Component layerComp = new TestComp( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.5,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 1, 1, 1, 1 ), 0, 0 );
        panel.add( directComp, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.5,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 1, 1, 1, 1 ), 0, 0 );
        panel.add( layerComp, constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TestComp extends JPanel
    {
        private static final long serialVersionUID = 1L;
        private final Layer2d layer;

        public TestComp( boolean layered )
        {
            layer = layered ? new Layer2d() : null;

            super.setBorder( new LineBorder( Color.green, 1 ) );
            super.setBackground( Color.white );
            super.setOpaque( true );
        }

        /***********************************************************************
         * 
         **********************************************************************/
        @Override
        public void paintComponent( Graphics g )
        {
            super.paintComponent( g );

            Graphics2D graphics = ( Graphics2D )g;
            Dimension size = getSize();

            // size.width--;
            // size.height--;

            Insets borderSize = super.getInsets();
            int x = borderSize.left;
            int y = borderSize.top;

            size.width = size.width - borderSize.left - borderSize.right;
            size.height = size.height - borderSize.top - borderSize.bottom;

            size.width = Math.max( size.width, 10 );
            size.height = Math.max( size.height, 10 );

            if( layer != null )
            {
                Dimension s = new Dimension( size.width, size.height );
                layer.setSize( s );
                Graphics2D lg = layer.getGraphics();

                drawGraphics( lg, size, 0, 0 );

                testImage( layer.img, lg );

                layer.paint( graphics, x, y );
            }
            else
            {
                drawGraphics( graphics, size, x, y );
            }
        }

        private static void drawGraphics( Graphics2D graphics, Dimension size,
            int x, int y )
        {
            // graphics.setStroke( new BasicStroke( 2, BasicStroke.CAP_ROUND,
            // BasicStroke.JOIN_ROUND ) );
            // Shape s = new Rectangle2D.Float( x - 0.51f, y - 0.51f,
            // size.width,
            // size.height );
            graphics.setColor( Color.black );
            graphics.drawRect( x, y, size.width - 1, size.height - 1 );
            // graphics.draw( s );

            graphics.setColor( Color.red );
            graphics.drawRect( x + 1, x + 1, size.width - 3, size.height - 3 );

            graphics.setColor( Color.cyan );
            graphics.drawLine( x + 1, y + 5, x + size.width - 1, y + 5 );

            graphics.setColor( Color.magenta );
            graphics.drawLine( x + size.width, y, x + size.width, y );
        }
    }

    private static void testImage( BufferedImage img, Graphics2D graphics )
    {
        Dimension size = new Dimension( img.getWidth(), img.getHeight() );
        Point p = new Point( size.width - 1, 0 );

        Color expected = Color.orange;
        // Color existing = new Color( img.getRGB( p.x, p.y ), true );
        // Color actual;

        graphics.setColor( expected );

        for( int i = 0; i < size.getWidth(); i++ )
        {
            int evenOne = ( i % 2 == 0 ? 0 : 1 );
            int x = p.x - i;
            int y = p.y + evenOne;

            if( x < 0 || y >= size.height )
            {
                break;
            }

            graphics.drawLine( x, y, x, y );
        }

        // actual = new Color( img.getRGB( p.x, p.y ), true );
        // LogUtils.printDebug(
        // "Pixel @ %d, %d of [%d,%d]: existing = %s, expected = %s, actual =
        // %s",
        // p.x, p.y, size.width, size.height, existing, expected, actual );
    }
}
