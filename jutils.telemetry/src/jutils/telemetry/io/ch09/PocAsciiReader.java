package jutils.telemetry.io.ch09;

import jutils.telemetry.data.ch09.PointOfContact;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PocAsciiReader
{
    /***************************************************************************
     * @param poc
     * @param store
     * @param pocId
     **************************************************************************/
    public void read( PointOfContact poc, AsciiStore store, int pocId )
    {
        poc.name = store.getString( "G\\POC1-" + pocId );
        poc.agency = store.getString( "G\\POC2-" + pocId );
        poc.address = store.getString( "G\\POC3-" + pocId );
        poc.telephone = store.getString( "G\\POC4-" + pocId );
    }
}
