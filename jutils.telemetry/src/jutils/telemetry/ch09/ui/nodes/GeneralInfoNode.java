package jutils.telemetry.ch09.ui.nodes;

import javax.swing.Icon;

import jutils.core.ui.AbstractDataNode;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.GeneralInformation;
import jutils.telemetry.ch09.ui.GeneralInformationView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GeneralInfoNode extends AbstractDataNode<GeneralInformation>
{
    /***************************************************************************
     * @param parent
     * @param data
     **************************************************************************/
    public GeneralInfoNode( AbstractDataNode<?> parent,
        GeneralInformation data )
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
        return "General Information";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void createChildren()
    {
        children.add( new InfoNode( this, data.information ) );
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
    public IDataView<GeneralInformation> createView()
    {
        return new GeneralInformationView();
    }
}
