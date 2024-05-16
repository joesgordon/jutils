package jutils.telemetry.ch10;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10Channel
{
    /**  */
    public short id;
    /**  */
    public DataType dataType;

    /***************************************************************************
     * @param id
     * @param dataType
     **************************************************************************/
    public Ch10Channel( short id, DataType dataType )
    {
        this.id = id;
        this.dataType = dataType;
    }
}
