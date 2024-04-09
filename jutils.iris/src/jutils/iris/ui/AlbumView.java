package jutils.iris.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.PositionIndicator;
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
    private IRasterAlbum album;

    /***************************************************************************
     * 
     **************************************************************************/
    public AlbumView()
    {
        this.positionView = new PositionIndicator(
            ( v ) -> Long.toString( v + 1 ) + "/" + album.getRasterCount() );
        this.imgView = new ImageView();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( positionView.getView(), BorderLayout.NORTH );
        panel.add( imgView.getView(), BorderLayout.CENTER );

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
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resetImages()
    {
        imgView.resetRaster();
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setInfoVisible( boolean visible )
    {
        imgView.setInfoVisible( visible );
    }
}
