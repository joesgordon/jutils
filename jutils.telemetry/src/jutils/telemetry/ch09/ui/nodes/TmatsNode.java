package jutils.telemetry.ch09.ui.nodes;

import javax.swing.Icon;

import jutils.core.ui.AbstractDataNode;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.TmatsFile;
import jutils.telemetry.ch09.ui.TmatsFileView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsNode extends AbstractDataNode<TmatsFile>
{
    /***************************************************************************
     * @param parent
     * @param data
     **************************************************************************/
    public TmatsNode( TmatsFile data )
    {
        super( null, data );

        createChildren();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return data.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void createChildren()
    {
        children.add( new GeneralInfoNode( this, data.tmats.general ) );
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
    public IDataView<TmatsFile> createView()
    {
        return new TmatsFileView();
    }
}
