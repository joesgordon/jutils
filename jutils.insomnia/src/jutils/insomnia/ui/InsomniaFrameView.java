package jutils.insomnia.ui;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.Timer;

import jutils.core.IconConstants;
import jutils.core.ui.ABButton;
import jutils.core.ui.JGoodiesToolBar;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.WindowCloseListener;
import jutils.core.ui.model.IView;
import jutils.insomnia.InsomniaIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final ABButton startStopButton;
    /**  */
    private final InsomniaView mainView;

    /**  */
    private final Timer updateTimer;

    /***************************************************************************
     * 
     **************************************************************************/
    public InsomniaFrameView()
    {
        this.view = new StandardFrameView();
        Icon startIcon = IconConstants.getIcon( IconConstants.NAV_NEXT_16 );
        Icon stopIcon = IconConstants.getIcon( IconConstants.STOP_16 );
        this.startStopButton = new ABButton( "Start", startIcon, () -> start(),
            "Stop", stopIcon, () -> stop() );
        this.mainView = new InsomniaView();
        this.updateTimer = new Timer( 20000,
            ( e ) -> mainView.refreshStatus() );

        updateTimer.stop();
        updateTimer.setInitialDelay( 100 );
        updateTimer.setDelay( 100 );

        view.setToolbar( createToolbar() );
        view.setContent( mainView.getView() );

        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 300, 300 );
        view.setTitle( "Insomnia" );

        view.getView().setIconImages( InsomniaIcons.getAppImages() );
        view.getView().addWindowListener(
            new WindowCloseListener( () -> handleFrameClosing() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        toolbar.add( startStopButton.getView() );

        return toolbar;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean start()
    {
        updateTimer.start();

        mainView.start();

        return true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean stop()
    {
        updateTimer.stop();

        mainView.stop();

        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleFrameClosing()
    {
        mainView.stop();
    }
}
