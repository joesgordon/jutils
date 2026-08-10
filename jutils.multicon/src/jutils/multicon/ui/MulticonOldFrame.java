package jutils.multicon.ui;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import jutils.core.OptionUtils;
import jutils.core.SwingUtils;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.net.TcpConfig;
import jutils.core.net.TcpServerConfig;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.WindowCloseListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;
import jutils.core.ui.net.TcpInputsView;
import jutils.core.ui.net.TcpServerConfigView;
import jutils.multicon.*;
import jutils.multicon.data.UdpInputs;
import jutils.multicon.ui.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonOldFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final List<IBindableView<?>> views;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonOldFrame()
    {
        this.frameView = new StandardFrameView();
        this.views = new ArrayList<>();

        frameView.getView().setIconImages( MulticonIcons.getMulticonImages() );

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 400, 400 );
        frameView.setTitle( "Multicon" );
        frameView.setContent( createContent() );

        frameView.getView().addWindowListener(
            new WindowCloseListener( () -> handleWindowClosed() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JTabbedPane tabs = new JTabbedPane();
        IUpdater<IBindableView<?>> u = null;
        BindableFavView<?> favView;

        UdpInputsView udpView = new UdpInputsView();
        TcpInputsView tcpClientView = new TcpInputsView( false );
        TcpServerConfigView tcpServerView = new TcpServerConfigView( true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();

        udpView.setData( new UdpInputs( options.udpInputs ) );
        tcpClientView.setData( new TcpConfig( options.tcpClientInputs ) );
        tcpServerView.setData( new TcpServerConfig( options.tcpServerInputs ) );

        u = ( v ) -> {
            options.udpInputs = new UdpInputs( udpView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( UdpView.NAME, udpView,
            () -> new ConnectionBindableView<>( new UdpView() ), u );
        tabs.addTab( favView.name, favView.getView() );

        u = ( v ) -> {
            options.tcpClientInputs = new TcpConfig( tcpClientView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( TcpClientView.NAME, tcpClientView,
            () -> new ConnectionBindableView<>( new TcpClientView() ), u );
        tabs.addTab( favView.name, favView.getView() );

        u = ( v ) -> {
            options.tcpServerInputs = new TcpServerConfig(
                tcpServerView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( TcpServerView.NAME, tcpServerView,
            () -> new TcpServerView(), u );
        tabs.addTab( favView.name, favView.getView() );

        return tabs;
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void showView( IBindableView<?> view )
    {
        views.add( view );

        showBindingFrame( view, getView() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleWindowClosed()
    {
        for( IBindableView<?> view : views )
        {
            closeView( view );
        }
    }

    /***************************************************************************
     * @param view
     * @param parent
     * @return
     **************************************************************************/
    public static BindingFrameView showBindingFrame( IBindableView<?> view,
        Component parent )
    {
        return showBindingFrame( view, parent, true );
    }

    /***************************************************************************
     * @param view
     * @param parent
     * @param bind
     * @return
     **************************************************************************/
    public static BindingFrameView showBindingFrame( IBindableView<?> view,
        Component parent, boolean bind )
    {
        Window window = SwingUtils.getComponentsWindow( parent );
        BindingFrameView frame = new BindingFrameView( view, parent );

        frame.getView().addWindowListener( new WindowCloseListener( () -> {
            try
            {
                view.unbind();
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( parent, ex.getMessage(),
                    "Socket Close Error" );
            }
        } ) );

        frame.getView().setIconImages( window.getIconImages() );
        frame.getView().pack();
        frame.getView().setLocationRelativeTo( parent );
        frame.getView().setVisible( true );

        if( bind )
        {
            frame.bind();
        }

        return frame;
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void closeView( IBindableView<?> view )
    {
        try
        {
            view.unbind();
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "Socket Close Error" );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }
}
