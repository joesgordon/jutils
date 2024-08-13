package jutils.telemetry.io.ch09;

import jutils.telemetry.data.ch09.TestInformation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TestInformationAsciiReader
{
    /***************************************************************************
     * @param info
     * @param store
     **************************************************************************/
    public void read( TestInformation info, AsciiStore store )
    {
        info.testDuration = store.getString( "G\\TI1" );
        info.preTestRequirement = store.getString( "G\\TI2" );
        info.postTestRequirement = store.getString( "G\\TI3" );
        info.classification = store.getString( "G\\SC" );
    }
}
