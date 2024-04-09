package jutils.iris.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( positionView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 4, 4, 4 ), 0, 0 );
        panel.add( imgView.getView(), constraints );

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

        positionView.setPosition( index );
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
