package jutils.telemetry.ch09.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch09.Transmission;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TransmissionView implements IDataView<Transmission>
{
    /**  */
    private final JComponent view;

    /**  */
    private Transmission data;

    /***************************************************************************
     * 
     **************************************************************************/
    public TransmissionView()
    {
        this.view = createView();

        setData( new Transmission() );
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
    public Transmission getData()
    {
        return data;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Transmission data )
    {
        this.data = data;
        // TODO Auto-generated method stub
    }
}
