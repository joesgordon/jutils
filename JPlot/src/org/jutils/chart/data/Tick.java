package org.jutils.chart.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Tick
{
    /**  */
    public final double value;
    /**  */
    public final String label;
    /**  */
    public int offset;

    /***************************************************************************
     * @param offset
     * @param value
     * @param label
     **************************************************************************/
    public Tick( double value, String label )
    {
        this.value = value;
        this.label = label;
        this.offset = 0;
    }
}
