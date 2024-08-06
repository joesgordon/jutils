package jutils.telemetry.ui.ch10;

import java.awt.Component;

import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch09.Tmats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsView implements IDataView<Tmats>
{
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsView()
    {
        this.view = new JPanel();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Tmats getData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Tmats data )
    {
        // TODO Auto-generated method stub

    }

}
