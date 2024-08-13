package jutils.telemetry.io.ch09;

import jutils.telemetry.data.ch09.Information;
import jutils.telemetry.data.ch09.PointOfContact;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InformationAsciiReader
{
    /**  */
    private final PocAsciiReader pocReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public InformationAsciiReader()
    {
        this.pocReader = new PocAsciiReader();
    }

    /***************************************************************************
     * @param info
     * @param store
     * @param id
     **************************************************************************/
    public void read( Information info, AsciiStore store )
    {
        Integer x;

        info.filename = store.getString( "G\\FN" );
        info.revisionLevel = store.getString( "G\\106" );
        info.originationDate = store.getString( "G\\OD" );
        info.revisionNumber = store.getString( "G\\RN" );
        info.revisionDate = store.getString( "G\\RD" );
        info.updateNumber = store.getString( "G\\UN" );
        info.updateDate = store.getString( "G\\UD" );
        info.testNumber = store.getString( "G\\TN" );

        info.pocs.clear();
        x = store.getInteger( "G\\POC\\N" );
        if( x != null )
        {
            for( int i = 0; i < x; i++ )
            {
                PointOfContact poc = new PointOfContact();

                pocReader.read( poc, store, i + 1 );

                info.pocs.add( poc );
            }
        }
    }
}
