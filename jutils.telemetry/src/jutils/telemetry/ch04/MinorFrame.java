package jutils.telemetry.ch04;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MinorFrame
{
    /**  */
    public final int sfid;
    /**  */
    public final byte [] words;

    /***************************************************************************
     * @param sfid
     * @param size
     **************************************************************************/
    public MinorFrame( int sfid, int size )
    {
        this.sfid = sfid;
        this.words = new byte[size];
    }
}
