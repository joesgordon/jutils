package jutils.telemetry.ch09.io.ascii;

import jutils.telemetry.ch09.EthernetPort;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EthernetPortStorilizer implements IStorilizer<EthernetPort>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( EthernetPort poc, AsciiStore store )
    {
        poc.address = store.getString( "EI\\PA" );
        poc.type = store.getString( "EI\\PT" );
    }
}
