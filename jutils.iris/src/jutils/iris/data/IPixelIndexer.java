package jutils.iris.data;

import java.awt.Point;

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
     * @param index
     * @param location
     **************************************************************************/
    public void getLocation( int width, int height, int index, Point location );

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
        return new RowMajorIndexer();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IPixelIndexer createColumnMajorIndexer()
    {
        return new ColumnMajorIndexer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class RowMajorIndexer implements IPixelIndexer
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndex( int w, int h, int x, int y )
        {
            return y * w + x;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void getLocation( int w, int h, int index, Point location )
        {
            location.x = index % w;
            location.y = index / w;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class ColumnMajorIndexer implements IPixelIndexer
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndex( int w, int h, int x, int y )
        {
            return x * h + y;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void getLocation( int w, int h, int index, Point location )
        {
            location.x = index / h;
            location.y = index % h;
        }
    }
}
