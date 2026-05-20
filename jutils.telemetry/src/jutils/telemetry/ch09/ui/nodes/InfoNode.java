package jutils.telemetry.ch09.ui.nodes;

import javax.swing.Icon;

import jutils.core.ui.AbstractDataNode;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.Information;
import jutils.telemetry.ch09.ui.InformationView;

public class InfoNode extends AbstractDataNode<Information>
{
    /***************************************************************************
     * @param parent
     * @param data
     **************************************************************************/
    public InfoNode( AbstractDataNode<?> parent, Information data )
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
        return "Information";
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
    public IDataView<Information> createView()
    {
        return new InformationView();
    }
}
