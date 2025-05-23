package jutils.telemetry.ch09.io.ascii;

import jutils.telemetry.ch09.HwModule;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HwModuleStorilizer implements IStorilizer<HwModule>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( HwModule item, AsciiStore store )
    {
        item.id = store.getString( "RIMI" );
        item.serialNumber = store.getString( "RIMS" );
        item.firmwareRevision = store.getString( "RIMF" );
    }
}
