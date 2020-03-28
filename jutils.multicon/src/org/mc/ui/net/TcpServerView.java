package org.mc.ui.net;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jutils.core.concurrent.*;
import org.jutils.core.io.LogUtils;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.net.*;
import org.jutils.core.ui.net.TcpInputsView;
import org.mc.MulticonMain;
import org.mc.MulticonOptions;
import org.mc.ui.BindingFrameView.IBindableView;
import org.mc.ui.ConnectionBindableView;
import org.mc.ui.MulticonFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerView implements IBindableView
{
    /**  */
    public static final String NAME = "TCP Server";

    /**  */
    private final TcpInputsView inputsView;

    /**  */
    private TaskThread acceptThread;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpServerView()
    {
        this.inputsView = new TcpInputsView( true, true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData(
            new TcpInputs( userio.getOptions().tcpServerInputs ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void bind() throws IOException
    {
        TcpInputs inputs = inputsView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getDefault();
        options.tcpServerInputs = new TcpInputs( inputs );
        userio.write( options );

        AcceptTask task = new AcceptTask( inputs, this );
        this.acceptThread = new TaskThread( task, "TCP Server Accept" );

        acceptThread.start();

        inputsView.setEnabled( false );
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
            LogUtils.printDebug( "Waiting for" );
            acceptThread.stopAndWait();
            LogUtils.printDebug( ">Waited for" );

            this.acceptThread = null;
        }

        inputsView.setEnabled( true );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return inputsView.getView();
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
     * @param connection
     **************************************************************************/
    private void handleConnectionAccepted( TcpConnection connection )
    {
        TcpClientView clientView = new TcpClientView();
        ConnectionBindableView connectionView = new ConnectionBindableView(
            new TcpClientView() );

        TcpInputs inputs = connection.getInputs();

        clientView.setInputs( inputs );

        connectionView.setConnection( connection );

        MulticonFrame.showBindingFrame( connectionView, getView() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AcceptTask implements ITask
    {
        /**  */
        private final TcpInputs inputs;
        /**  */
        private final TcpServerView view;

        /**
         * @param inputs
         * @param view
         */
        public AcceptTask( TcpInputs inputs, TcpServerView view )
        {
            this.inputs = inputs;
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run( ITaskHandler stopManager )
        {
            try( TcpServer server = new TcpServer( inputs ) )
            {
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
