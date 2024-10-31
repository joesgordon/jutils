package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.PointOfContact;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PocStorilizer implements IStorilizer<PointOfContact>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( PointOfContact poc, AsciiStore store )
    {
        poc.name = store.getString( "POC1" );
        poc.agency = store.getString( "POC2" );
        poc.address = store.getString( "POC3" );
        poc.telephone = store.getString( "POC4" );
    }
}
