package jutils.platform.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.IDataView;
import jutils.platform.data.SerialConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialConfigView implements IDataView<SerialConfig>
{
    /**  */
    private final JComponent view;

    /**  */
    private final ComPortField portField;
    /**  */
    private final SerialParamsView paramsView;

    private SerialConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialConfigView()
    {
        this.portField = new ComPortField( "COM Port" );
        this.paramsView = new SerialParamsView();

        this.view = createView();

        setData( new SerialConfig() );

        portField.setUpdater( ( d ) -> this.config.comPort = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( paramsView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( portField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SerialConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( SerialConfig data )
    {
        this.config = data;

        portField.setValue( config.comPort );
        paramsView.setData( config.params );
    }
}
