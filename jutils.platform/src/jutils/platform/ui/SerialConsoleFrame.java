package jutils.platform.ui;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.ABButton;
import jutils.core.ui.ABButton.IABCallback;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;
import jutils.platform.PlatformIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialConsoleFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final SerialConsoleView console;
    /**  */
    private final SerialConfigView configView;
    /**  */
    private final ABButton connectButton;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialConsoleFrame()
    {
        this.view = new StandardFrameView();
        this.console = new SerialConsoleView();
        this.configView = new SerialConfigView();
        this.connectButton = createConnectButton();

        view.setTitle( "Serial Console" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setToolbar( createToolbar() );
        view.setContent( configView.getView() );
        view.getView().setIconImages( PlatformIcons.getAppImages() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        toolbar.add( connectButton.button );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ABButton createConnectButton()
    {
        ABButton button;

        Icon connectIcon = IconConstants.getIcon( IconConstants.CHECK_16 );
        IABCallback connectCallback = () -> handleConnect();
        Icon disconnectIcon = IconConstants.getIcon( IconConstants.CLOSE_16 );
        IABCallback disconnectCallback = () -> handleDisconnect();

        button = new ABButton( "Connect", connectIcon, connectCallback,
            "Disconnect", disconnectIcon, disconnectCallback );

        return button;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean handleConnect()
    {
        boolean connected = console.connect( configView.getData() );

        if( connected )
        {
            view.setContent( console.getView() );
        }
        else
        {
            view.setContent( configView.getView() );
        }

        return connected;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean handleDisconnect()
    {
        boolean disconnected = console.disconnect();

        if( disconnected )
        {
            view.setContent( configView.getView() );
        }
        else
        {
            view.setContent( console.getView() );
        }

        return disconnected;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
