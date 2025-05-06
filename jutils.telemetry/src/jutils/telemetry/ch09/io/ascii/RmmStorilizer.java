package jutils.telemetry.ch09.io.ascii;

import jutils.telemetry.ch09.HwModule;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RmmStorilizer implements IStorilizer<HwModule>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( HwModule item, AsciiStore store )
    {
        item.id = store.getString( "RMMID" );
        item.serialNumber = store.getString( "RMMS" );
        item.firmwareRevision = store.getString( "RMMF" );
    }
}
