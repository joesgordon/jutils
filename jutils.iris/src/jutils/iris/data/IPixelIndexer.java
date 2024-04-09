package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IPixelIndexer
{
    /***************************************************************************
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public int getIndex( int width, int height, int x, int y );

    /***************************************************************************
     * @param t
     * @return
     **************************************************************************/
    public static IPixelIndexer createIndexer( IndexingType t )
    {
        switch( t )
        {
            case ROW_MAJOR:
                return createRowMajorIndexer();

            case COLUMN_MAJOR:
                return createColumnMajorIndexer();
        }

        throw new IllegalStateException(
            "Indexing type not handled: " + t.getDescription() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IPixelIndexer createRowMajorIndexer()
    {
        return ( w, h, x, y ) -> y * w + x;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IPixelIndexer createColumnMajorIndexer()
    {
        return ( w, h, x, y ) -> x * h + y;
    }
}
