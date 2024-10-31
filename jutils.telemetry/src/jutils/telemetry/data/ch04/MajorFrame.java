package jutils.telemetry.data.ch04;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MajorFrame
{
    /**  */
    public final MinorFrame [] minors;

    /***************************************************************************
     * @param minorCount
     * @param minorSize
     **************************************************************************/
    public MajorFrame( int minorCount, int minorSize )
    {
        this.minors = new MinorFrame[minorCount];

        for( int i = 0; i < minors.length; i++ )
        {
            minors[i] = new MinorFrame( i, minorSize );
        }
    }
}
