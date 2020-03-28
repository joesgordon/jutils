package org.mc.ui.net;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JComponent;

import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.net.*;
import org.jutils.core.ui.net.TcpInputsView;
import org.mc.MulticonMain;
import org.mc.MulticonOptions;
import org.mc.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpClientView implements IConnectionView
{
    /**  */
    public static final String NAME = "TCP Client";
    /**  */
    private final TcpInputsView inputsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpClientView()
    {
        this.inputsView = new TcpInputsView( false );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData(
            new TcpInputs( userio.getOptions().tcpClientInputs ) );
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
    public String getTitle()
    {
        return NAME;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IConnection createConnection() throws SocketException, IOException
    {
        TcpInputs inputs = inputsView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();
        options.tcpClientInputs = new TcpInputs( inputs );
        userio.write( options );

        return new TcpConnection( inputs );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        inputsView.setEnabled( editable );
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public void setInputs( TcpInputs inputs )
    {
        inputsView.setData( inputs );
    }
}
