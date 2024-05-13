package jutils.telemetry.ch10;

import java.util.ArrayList;
import java.util.List;

import jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Chapter10File
{
    /**  */
    public final IDataStream stream;
    /**  */
    public final List<Ch10Channel> channels;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public Chapter10File( IDataStream stream )
    {
        this.stream = stream;
        this.channels = new ArrayList<>();
    }
}
