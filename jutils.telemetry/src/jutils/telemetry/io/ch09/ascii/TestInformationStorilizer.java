package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.TestInformation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TestInformationStorilizer
{
    /***************************************************************************
     * @param info
     * @param store
     **************************************************************************/
    public void read( TestInformation info, AsciiStore store )
    {
        info.testDuration = store.getString( "TI1" );
        info.preTestRequirement = store.getString( "TI2" );
        info.postTestRequirement = store.getString( "TI3" );
        info.classification = store.getString( "SC" );
    }
}