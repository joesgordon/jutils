package jutils.math;

import java.util.Arrays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DimensionalIndexer
{
    /**  */
    private final int [] dimSizes;
    /** The coefficients for accessing a value. */
    private final int [] offsets;
    /**  */
    private final int size;

    /***************************************************************************
     * @param dims
     **************************************************************************/
    public DimensionalIndexer( int... dims )
    {
        this.dimSizes = dims;
        this.offsets = new int[dims.length];

        setOffsets( dimSizes, offsets );

        this.size = dims.length > 0 ? offsets[0] * dims[0] : 0;
    }

    /***************************************************************************
     * @param indexes
     * @return
     **************************************************************************/
    public int toIndex( int... indexes )
    {
        int index = 0;

        for( int d = 0; d < dimSizes.length; d++ )
        {
            index += indexes[d] * offsets[d];
        }

        return index;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public int [] toIndexes( int index )
    {
        int [] indexes = new int[dimSizes.length];

        int r = index;

        for( int i = 0; i < indexes.length; i++ )
        {
            int v = r / offsets[i];

            r -= v * offsets[i];

            indexes[i] = v;
        }

        return indexes;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSize()
    {
        return size;
    }

    /***************************************************************************
     * @param dimensionIndex
     * @return
     **************************************************************************/
    public int getSize( int dimensionIndex )
    {
        return dimSizes[dimensionIndex];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void transpose()
    {
        int [] oldDims = Arrays.copyOf( dimSizes, dimSizes.length );

        for( int to = 0, fm = dimSizes.length -
            1; to < dimSizes.length; to++, fm-- )
        {
            dimSizes[to] = oldDims[fm];
        }

        setOffsets( dimSizes, offsets );
    }

    /***************************************************************************
     * @param dimSizes
     * @param offsets
     **************************************************************************/
    private static void setOffsets( int [] dimSizes, int [] offsets )
    {
        for( int o = offsets.length - 1; o > -1; o-- )
        {
            boolean isLast = o == ( offsets.length - 1 );

            offsets[o] = isLast ? 1 : dimSizes[o + 1] * offsets[o + 1];
        }
    }
}
