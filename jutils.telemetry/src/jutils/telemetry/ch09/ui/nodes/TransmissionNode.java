package jutils.telemetry.ch09.ui.nodes;

import javax.swing.Icon;

import jutils.core.ui.AbstractDataNode;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.Transmission;
import jutils.telemetry.ch09.ui.TransmissionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransmissionNode extends AbstractDataNode<Transmission>
{
    /***************************************************************************
     * @param parent
     * @param data
     **************************************************************************/
    public TransmissionNode( AbstractDataNode<?> parent, Transmission data )
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
        return "Transmission";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void createChildren()
    {
        // TODO Auto-generated method stub
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
    public IDataView<Transmission> createView()
    {
        return new TransmissionView();
    }
}
