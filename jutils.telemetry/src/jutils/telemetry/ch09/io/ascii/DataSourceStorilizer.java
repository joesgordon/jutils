package jutils.telemetry.ch09.io.ascii;

import jutils.telemetry.ch09.DataSource;
import jutils.telemetry.ch09.DataSourceType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataSourceStorilizer implements IStorilizer<DataSource>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( DataSource ds, AsciiStore store )
    {
        ds.id = store.getString( "DSI" );
        ds.type = DataSourceType.fromKey( store.getString( "DST" ) );
        ds.classification = store.getString( "DSC" );
    }
}
