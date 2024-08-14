package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.Information;
import jutils.telemetry.data.ch09.PointOfContact;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InformationStorilizer implements IStorilizer<Information>
{
    /**  */
    private final PocStorilizer pocReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public InformationStorilizer()
    {
        this.pocReader = new PocStorilizer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( Information info, AsciiStore store )
    {
        info.filename = store.getString( "FN" );
        info.revisionLevel = store.getString( "106" );
        info.originationDate = store.getString( "OD" );
        info.revisionNumber = store.getString( "RN" );
        info.revisionDate = store.getString( "RD" );
        info.updateNumber = store.getString( "UN" );
        info.updateDate = store.getString( "UD" );
        info.testNumber = store.getString( "TN" );

        info.pocCount = store.readItems( "POC\\N", info.pocs, pocReader,
            () -> new PointOfContact() );
    }
}
