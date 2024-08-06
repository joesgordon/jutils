package jutils.telemetry.data.ch10;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10Channel
{
    /**  */
    public short id;
    /**  */
    public DataType dataType;
    /**  */
    public long startTime;
    /**  */
    public long endTime;
    /**  */
    public int packetCount;

    /***************************************************************************
     * @param id
     * @param dataType
     **************************************************************************/
    public Ch10Channel( short id, DataType dataType )
    {
        this.id = id;
        this.dataType = dataType;
        this.startTime = 0L;
        this.endTime = 0L;
        this.packetCount = 1;
    }
}
