package jutils.telemetry.ui.ch09;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch09.Information;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InformationView implements IDataView<Information>
{
    /**  */
    private final JComponent view;

    /**  */
    private Information general;

    /***************************************************************************
     * 
     **************************************************************************/
    public InformationView()
    {
        this.view = createView();

        setData( new Information() );
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
    public Information getData()
    {
        return general;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Information data )
    {
        this.general = data;

        // TODO Auto-generated method stub
    }
}
