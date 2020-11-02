package org.jutils.insomnia.ui;

import java.awt.Component;

import javax.swing.JComponent;

import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.model.IDataView;
import org.jutils.insomnia.data.InsomniaConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaConfigView implements IDataView<InsomniaConfig>
{
    /**  */
    private final JComponent view;

    /**  */
    private final KiloField inactiveDelayField;
    /**  */
    private final KiloField inactiveDurationField;
    /**  */
    private final KiloField resetPeriodField;

    /**  */
    private InsomniaConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public InsomniaConfigView()
    {
        this.inactiveDelayField = new KiloField( "Inactive Delay", "s" );
        this.inactiveDurationField = new KiloField( "Inactive Duration", "s" );
        this.resetPeriodField = new KiloField( "Reset Period", "s" );

        this.view = createView();

        setData( new InsomniaConfig() );

        inactiveDelayField.setUpdater( ( d ) -> config.inactiveDelay = d );
        inactiveDurationField.setUpdater(
            ( d ) -> config.inactiveDuration = d );
        resetPeriodField.setUpdater( ( d ) -> config.resetPeriod = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( inactiveDelayField );
        form.addField( inactiveDurationField );
        form.addField( resetPeriodField );

        return form.getView();
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
    public InsomniaConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( InsomniaConfig config )
    {
        this.config = config;

        inactiveDelayField.setValue( config.inactiveDelay );
        inactiveDurationField.setValue( config.inactiveDuration );
        resetPeriodField.setValue( config.resetPeriod );
    }
}
