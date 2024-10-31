package jutils.telemetry.ui.ch10;

import java.awt.Component;

import jutils.core.ui.ClassedView;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch10.CompGen1Body;
import jutils.telemetry.data.ch10.DataBody;
import jutils.telemetry.data.ch10.IPacketBody;
import jutils.telemetry.data.ch10.Pcm1Body;
import jutils.telemetry.data.ch10.Time1Body;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketBodyView implements IDataView<IPacketBody>
{
    /**  */
    private final ClassedView<IPacketBody> view;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketBodyView()
    {
        this.view = new ClassedView<>();

        view.put( DataBody.class, new DataBodyView(), false );
        view.put( CompGen1Body.class, new CompGen1BodyView(), false );
        view.put( Time1Body.class, new Time1BodyView(), true );
        view.put( Pcm1Body.class, new Pcm1BodyView(), false );
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
        return view.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( IPacketBody data )
    {
        view.setData( data );
    }
}
