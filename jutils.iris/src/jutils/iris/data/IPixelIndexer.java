package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IPixelIndexer
{
    /***************************************************************************
     * @param width
     * @param height
     * @param row
     * @param column
     * @return
     **************************************************************************/
    public int getIndex( int width, int height, int row, int column );

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
        return ( w, h, r, c ) -> r * w + c;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IPixelIndexer createColumnMajorIndexer()
    {
        return ( w, h, r, c ) -> c * h + r;
    }
}
