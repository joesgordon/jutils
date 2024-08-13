package jutils.telemetry.io.ch09;

import jutils.telemetry.data.ch09.DataSource;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataSourceAsciiReader
{
    /***************************************************************************
     * @param ds
     * @param store
     * @param id
     **************************************************************************/
    public void read( DataSource ds, AsciiStore store, int id )
    {
        ds.id = store.getString( "G\\DSI-" + id );
        ds.type = store.getString( "G\\DST-" + id );
        ds.classification = store.getString( "G\\DSC-" + id );
    }
}
