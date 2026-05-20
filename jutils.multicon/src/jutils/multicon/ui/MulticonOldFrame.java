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
import jutils.core.net.TcpInputs;
import jutils.core.net.UdpConfig;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.WindowCloseListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;
import jutils.core.ui.net.TcpInputsView;
import jutils.core.ui.net.UdpConfigView;
import jutils.multicon.*;
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

        frameView.getView().addWindowListener( new WindowCloseListener( () -> {
            for( IBindableView<?> view : views )
            {
                closeView( view );
            }
        } ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JTabbedPane tabs = new JTabbedPane();
        IUpdater<IBindableView<?>> u = null;
        BindableFavView<?> favView;

        UdpConfigView udpView = new UdpConfigView();
        TcpInputsView tcpClientView = new TcpInputsView( false );
        TcpInputsView tcpServerView = new TcpInputsView( true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();

        udpView.setData( new UdpConfig( options.udpInputs ) );
        tcpClientView.setData( new TcpInputs( options.tcpClientInputs ) );
        tcpServerView.setData( new TcpInputs( options.tcpServerInputs ) );

        u = ( v ) -> {
            options.udpInputs = new UdpConfig( udpView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( UdpView.NAME, udpView,
            () -> new ConnectionBindableView<>( new UdpView() ), u );
        tabs.addTab( favView.name, favView.getView() );

        u = ( v ) -> {
            options.tcpClientInputs = new TcpInputs( tcpClientView.getData() );
            userio.write( options );
            showView( v );
        };
        favView = new BindableFavView<>( TcpClientView.NAME, tcpClientView,
            () -> new ConnectionBindableView<>( new TcpClientView() ), u );
        tabs.addTab( favView.name, favView.getView() );

        u = ( v ) -> {
            options.tcpServerInputs = new TcpInputs( tcpServerView.getData() );
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
