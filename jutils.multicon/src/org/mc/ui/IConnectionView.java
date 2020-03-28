package org.mc.ui;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JComponent;

import org.jutils.core.net.IConnection;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines a view that can create connections.
 ******************************************************************************/
public interface IConnectionView extends IView<JComponent>
{
    /***************************************************************************
     * @return the string describing the connection this view represents.
     **************************************************************************/
    public String getTitle();

    /***************************************************************************
     * @return
     * @throws SocketException
     * @throws IOException
     **************************************************************************/
    public IConnection createConnection() throws SocketException, IOException;

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEditable( boolean editable );
}
