package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.Recorder;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecorderStorilizer implements IStorilizer<Recorder>
{
    /**  */
    private final RecorderMediaStorilizer mediaStorilizer;

    /***************************************************************************
     * 
     **************************************************************************/
    public RecorderStorilizer()
    {
        this.mediaStorilizer = new RecorderMediaStorilizer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( Recorder r, AsciiStore store )
    {
        r.sourceId = store.getString( "ID" );
        r.id = store.getString( "RID" );
        r.description = store.getString( "R1" );

        mediaStorilizer.read( r.media, store );
    }
}
