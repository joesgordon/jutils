package jutils.iris.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum IndexingType implements INamedValue
{
    /** Indexing proceeds a row of data at a time. */
    ROW_MAJOR( 0, "Row-Major" ),
    /** Indexing proceeds a column of data at a time. */
    COLUMN_MAJOR( 1, "Column-Major" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private IndexingType( int value, String name )
    {
        this.value = value;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return this.name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return this.value;
    }
}
