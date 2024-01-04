package jutils.iris.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum IndexingType implements INamedValue
{
    /**  */
    ROW_MAJOR( 0, "Row-Major" ),
    /**  */
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
