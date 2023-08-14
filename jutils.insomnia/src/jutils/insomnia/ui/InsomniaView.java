package jutils.insomnia.ui;

import javax.swing.JComponent;

import jutils.core.ui.ComponentView;
import jutils.core.ui.model.IView;
import jutils.insomnia.InsomniaController;
import jutils.insomnia.data.InsomniaConfig;
import jutils.insomnia.data.InsomniaStatus;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final InsomniaConfigView configView;
    /**  */
    private final InsomniaStatusView statusView;
    /**  */
    private final ComponentView mainView;

    /**  */
    private final InsomniaController controller;

    /***************************************************************************
     * 
     **************************************************************************/
    public InsomniaView()
    {
        this.configView = new InsomniaConfigView();
        this.statusView = new InsomniaStatusView();
        this.mainView = new ComponentView();
        this.controller = new InsomniaController();

        mainView.setComponent( configView.getView() );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        return mainView.getView();
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
     * 
     **************************************************************************/
    public void start()
    {
        InsomniaConfig config = configView.getData();
        InsomniaStatus status = new InsomniaStatus();

        statusView.setData( status );

        mainView.setComponent( statusView.getView() );

        controller.start( config, status );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        controller.stop();

        mainView.setComponent( configView.getView() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refreshStatus()
    {
        statusView.setData( statusView.getData() );

        statusView.getView().repaint();
    }
}
