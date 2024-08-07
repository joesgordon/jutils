package jutils.telemetry.ui.ch10;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import jutils.core.ui.ComponentView;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch10.CompGen1Body;
import jutils.telemetry.data.ch10.DataBody;
import jutils.telemetry.data.ch10.IPacketBody;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketBodyView implements IDataView<IPacketBody>
{
    /**  */
    private final ComponentView view;
    /**  */
    private final JScrollPane scrollpane;
    /**  */
    private final Map<Class<?>, IPacketBodyView<?>> bodyViews;

    /**  */
    private IPacketBody body;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketBodyView()
    {
        this.view = new ComponentView();
        this.scrollpane = new JScrollPane();
        this.bodyViews = new HashMap<>();

        scrollpane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        bodyViews.put( DataBody.class, new DataBodyView() );
        bodyViews.put( CompGen1Body.class, new CompGen1BodyView() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IPacketBody getData()
    {
        return this.body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( IPacketBody data )
    {
        this.body = data;

        IPacketBodyView<?> bodyView = bodyViews.get( data.getClass() );
        @SuppressWarnings( "unchecked")
        IPacketBodyView<
            IPacketBody> ipbView = ( IPacketBodyView<IPacketBody> )bodyView;

        ipbView.setData( data );

        if( ipbView.hasScrollbar() )
        {
            view.setComponent( ipbView.getView() );
        }
        else
        {
            scrollpane.setViewportView( ipbView.getView() );
            view.setComponent( scrollpane );
        }
    }
}
