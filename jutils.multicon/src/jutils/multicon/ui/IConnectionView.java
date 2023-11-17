package jutils.multicon.ui;

import java.io.IOException;
import java.net.SocketException;

import jutils.core.net.IConnection;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that can create connections.
 * @param <T>
 ******************************************************************************/
public interface IConnectionView<T> extends IDataView<T>
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
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable );
}
