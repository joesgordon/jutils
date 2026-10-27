package jutils.telemetry.ch09.ui.nodes;

import java.util.List;

import javax.swing.Icon;

import jutils.core.ui.AbstractDataNode;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.Transmission;
import jutils.telemetry.ch09.ui.TransmissionsView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransmissionsNode extends AbstractDataNode<List<Transmission>>
{
    /***************************************************************************
     * @param parent
     * @param data
     **************************************************************************/
    public TransmissionsNode( AbstractDataNode<?> parent,
        List<Transmission> data )
    {
        super( parent, data );

        createChildren();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Transmissions";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void createChildren()
    {
        for( Transmission t : data )
        {
            children.add( new TransmissionNode( this, t ) );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IDataView<List<Transmission>> createView()
    {
        return new TransmissionsView();
    }
}
