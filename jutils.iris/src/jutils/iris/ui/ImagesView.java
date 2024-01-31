package jutils.iris.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.PositionIndicator;
import jutils.core.ui.model.IView;
import jutils.iris.data.IRasterAlbum;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImagesView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final PositionIndicator positionView;
    /**  */
    private final ImageView imgView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImagesView()
    {
        this.positionView = new PositionIndicator();
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
     * @param images
     **************************************************************************/
    public void setImages( IRasterAlbum images )
    {
        int imgCount = images.getRasterCount();

        positionView.setLength( imgCount );
        positionView.setPosition( 0 );

        if( imgCount > 0 )
        {
            imgView.setRaster( images.getRaster( 0 ), images.getColorizer() );
        }
    }
}
