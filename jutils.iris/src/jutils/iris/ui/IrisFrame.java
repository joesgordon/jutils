package jutils.iris.ui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;
import jutils.iris.IrisIcons;
import jutils.iris.IrisUtils;
import jutils.iris.colors.MonoColorModel;
import jutils.iris.data.IColorModel;
import jutils.iris.data.IRaster;
import jutils.iris.data.RasterAlbumList;
import jutils.iris.rasters.Mono8Raster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final IrisView contentView;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisFrame()
    {
        this.frameView = new StandardFrameView();
        this.contentView = new IrisView();

        frameView.setTitle( "Iris" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 800 );
        frameView.setContent( contentView.getView() );
        frameView.getView().setIconImages( IrisIcons.getAppImages() );

        createMenus( frameView.getMenuBar(), frameView.getFileMenu() );
    }

    /***************************************************************************
     * @param menuBar
     * @param fileMenu
     **************************************************************************/
    private void createMenus( JMenuBar menuBar, JMenu fileMenu )
    {
        menuBar.add( createPatternMenu() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createPatternMenu()
    {
        JMenu menu = new JMenu( "Patterns" );
        JMenuItem item;

        item = menu.add( "Diagonal Gradient" );
        item.addActionListener( ( e ) -> handleDiagonalGradient() );

        item = menu.add( "Julia Fractal" );
        item.addActionListener( ( e ) -> handleJuliaFractal() );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleDiagonalGradient()
    {
        int w = 256;
        int h = 256;
        IRaster r = new Mono8Raster( w, h );

        IrisUtils.setDiagonalGradient( r );

        int bitDepth = r.getConfig().bitDepth;
        IColorModel colors = MonoColorModel.createGrayscaleMap( bitDepth );
        RasterAlbumList rasters = new RasterAlbumList();

        rasters.addRaster( r );
        rasters.setColors( colors );

        contentView.setImages( rasters );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleJuliaFractal()
    {
        int w = 512;
        int h = 512;
        IRaster r = new Mono8Raster( w, h );

        IrisUtils.setJuliaFractal( r );

        int bitDepth = r.getConfig().bitDepth;
        IColorModel colors = MonoColorModel.createMatlabDefault( bitDepth );
        RasterAlbumList rasters = new RasterAlbumList();

        rasters.addRaster( r );
        rasters.setColors( colors );

        contentView.setImages( rasters );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }
}
