package jutils.telemetry.ui.ch09;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch09.GeneralInformation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GeneralInformationView implements IDataView<GeneralInformation>
{
    /**  */
    private final JComponent view;

    /**  */
    private GeneralInformation general;

    /***************************************************************************
     * 
     **************************************************************************/
    public GeneralInformationView()
    {
        this.view = createView();

        setData( new GeneralInformation() );
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
    public GeneralInformation getData()
    {
        return general;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( GeneralInformation data )
    {
        this.general = data;

        // TODO Auto-generated method stub
    }
}
