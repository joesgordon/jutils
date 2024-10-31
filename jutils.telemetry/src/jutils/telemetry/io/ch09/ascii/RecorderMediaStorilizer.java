package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.RecorderMedia;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecorderMediaStorilizer implements IStorilizer<RecorderMedia>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( RecorderMedia media, AsciiStore store )
    {
        media.type = store.getString( "TC1" );
        media.manufacturer = store.getString( "TC2" );
        media.code = store.getString( "TC3" );
        media.location = store.getString( "RML" );
        media.rmmBusSpeed = store.getString( "ERBS" );
        media.tapeWidth = store.getString( "TC4" );
        media.tapeHousing = store.getString( "TC5" );
        media.trackType = store.getString( "TT" );
        media.channelCount = store.getInteger( "N" );
        media.recordSpeed = store.getString( "TC6" );
        media.dataDensity = store.getString( "TC7" );
        media.tapeRewound = store.getString( "TC8" );
        media.numSourceBits = store.getString( "NSB" );
    }
}
