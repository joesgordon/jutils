package jutils.telemetry.ch09.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.Transmission;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransmissionsView implements IDataView<List<Transmission>>
{
    /**  */
    private final JComponent view;

    /**  */
    private List<Transmission> data;

    /***************************************************************************
     * 
     **************************************************************************/
    public TransmissionsView()
    {
        this.view = createView();

        setData( new ArrayList<>() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel();
        // TODO Auto-generated method stub
        return panel;
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
    public List<Transmission> getData()
    {
        return data;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( List<Transmission> data )
    {
        this.data = data;
        // TODO Auto-generated method stub
    }
}
