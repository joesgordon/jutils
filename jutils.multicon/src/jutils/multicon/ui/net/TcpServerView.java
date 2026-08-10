package jutils.multicon.ui.net;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import jutils.core.concurrent.*;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.net.*;
import jutils.core.ui.net.TcpServerConfigView;
import jutils.multicon.MulticonMain;
import jutils.multicon.MulticonOptions;
import jutils.multicon.ui.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerView implements IBindableView<TcpServerConfig>
{
    /**  */
    public static final String NAME = "TCP Server";

    /**  */
    private final TcpServerConfigView configView;

    /**  */
    private TaskThread acceptThread;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpServerView()
    {
        this.configView = new TcpServerConfigView( true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        configView.setData(
            new TcpServerConfig( userio.getOptions().tcpServerInputs ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void bind() throws IOException
    {
        TcpServerConfig inputs = configView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getDefault();
        options.tcpServerInputs = new TcpServerConfig( inputs );
        userio.write( options );

        AcceptTask task = new AcceptTask( inputs, this );
        this.acceptThread = new TaskThread( task, "TCP Server Accept" );

        acceptThread.start();

        configView.setEnabled( false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void unbind() throws IOException
    {
        if( acceptThread != null )
        {
            acceptThread.stop();
            acceptThread.interrupt();
            acceptThread.stopAndWait();

            this.acceptThread = null;
        }

        configView.setEnabled( true );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return configView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isBound()
    {
        return acceptThread != null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return NAME;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TcpServerConfig getData()
    {
        return configView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( TcpServerConfig data )
    {
        configView.setData( data );
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    private void handleConnectionAccepted( TcpConnection connection )
    {
        TcpClientView clientView = new TcpClientView( connection );
        ConnectionBindableView<TcpConfig> connectionView;

        connectionView = new ConnectionBindableView<>( clientView );

        TcpConfig inputs = connection.getInputs();

        clientView.setInputs( inputs );

        MulticonOldFrame.showBindingFrame( connectionView, getView(), false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AcceptTask implements ITask
    {
        /**  */
        private final TcpServerConfig config;
        /**  */
        private final TcpServerView view;

        /**
         * @param config
         * @param view
         */
        public AcceptTask( TcpServerConfig config, TcpServerView view )
        {
            this.config = config;
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run( ITaskHandler stopManager )
        {
            try( TcpServer server = new TcpServer() )
            {
                server.open( config );

                while( stopManager.canContinue() )
                {
                    try
                    {
                        @SuppressWarnings( "resource")
                        TcpConnection connection = server.accept();

                        SwingUtilities.invokeLater(
                            () -> view.handleConnectionAccepted( connection ) );
                    }
                    catch( SocketTimeoutException ex )
                    {
                    }
                }
            }
            catch( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally
            {
                view.acceptThread = null;
            }
        }
    }
}
