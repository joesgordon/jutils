package jutils.math.rand;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DistProps
{
    /**  */
    public double mean;
    /**  */
    public double standardDeviation;

    /***************************************************************************
     * @param props
     **************************************************************************/
    public DistProps( DistProps props )
    {
        this.mean = props.mean;
        this.standardDeviation = props.standardDeviation;
    }
}
