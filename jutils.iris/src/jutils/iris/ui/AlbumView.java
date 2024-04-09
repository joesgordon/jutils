package jutils.iris.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.ui.PositionIndicator;
import jutils.core.ui.hex.ByteBuffer;
import jutils.core.ui.hex.HexPanel;
import jutils.core.ui.model.IView;
import jutils.iris.albums.IRasterAlbum;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AlbumView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final PositionIndicator positionView;

    /**  */
    private final ImageView imgView;
    /**  */
    private final PixelsView pixelsView;
    /**  */
    private final PlanesView planesView;
    /**  */
    private final ChannelsView rawPixelView;
    /**  */
    private final HexPanel bufferView;

    /**  */
    private IRasterAlbum album;

    /***************************************************************************
     * 
     **************************************************************************/
    public AlbumView()
    {
        this.positionView = new PositionIndicator(
            ( v ) -> Long.toString( v + 1 ) + "/" + album.getRasterCount() );
        this.imgView = new ImageView();
        this.pixelsView = new PixelsView();
        this.planesView = new PlanesView();
        this.rawPixelView = new ChannelsView();
        this.bufferView = new HexPanel();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Image", imgView.getView() );
        tabs.addTab( "Pixels", pixelsView.getView() );
        tabs.addTab( "Planes", planesView.getView() );
        tabs.addTab( "Raw Pixels", rawPixelView.getView() );
        tabs.addTab( "Raw Bytes", bufferView.getView() );

        panel.add( positionView.getView(), BorderLayout.NORTH );
        panel.add( tabs, BorderLayout.CENTER );

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
     * @param album
     **************************************************************************/
    public void setAlbum( IRasterAlbum album )
    {
        this.album = album;

        int imgCount = album.getRasterCount();

        positionView.setLength( imgCount );
        positionView.setPosition( 0 );

        if( imgCount > 0 )
        {
            setRaster( 0 );
        }
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    private void setRaster( int index )
    {
        IRaster raster = album.getRaster( index );

        imgView.setRaster( raster, album.getColorizer() );
        pixelsView.setRaster( imgView.getImage() );
        rawPixelView.setRaster( raster );
        bufferView.setBuffer( new ByteBuffer( raster.getBufferData() ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resetImages()
    {
        imgView.resetRaster();
    }
}
