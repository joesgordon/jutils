package org.jutils.multicon.ui.net;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JComponent;

import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.net.*;
import org.jutils.core.ui.net.MulticastInputsView;
import org.jutils.multicon.MulticonMain;
import org.jutils.multicon.MulticonOptions;
import org.jutils.multicon.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastView implements IConnectionView<MulticastInputs>
{
    /**  */
    public static final String NAME = "Multicast Connection";
    /**  */
    private final MulticastInputsView inputsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastView()
    {
        this.inputsView = new MulticastInputsView();

        inputsView.setEnabled( true );

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData(
            new MulticastInputs( userio.getOptions().multicastInputs ) );
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
    public MulticastInputs getData()
    {
        return inputsView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( MulticastInputs data )
    {
        inputsView.setData( data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IConnection createConnection() throws SocketException, IOException
    {
        MulticastInputs inputs = inputsView.getData();

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();
        MulticonOptions options = userio.getOptions();
        options.multicastInputs = new MulticastInputs( inputs );
        userio.write( options );

        MulticastConnection connection = new MulticastConnection( inputs );

        return connection;
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return NAME;
    }
}
