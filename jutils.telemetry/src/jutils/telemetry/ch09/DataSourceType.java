package jutils.telemetry.ch09;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum DataSourceType implements INamedValue
{
    /**  */
    RF( 0, "Radio Frequency", "RF" ),
    /**  */
    TAPE( 1, "Tape", "TAP" ),
    /**  */
    STORAGE( 2, "Storage", "STO" ),
    /**  */
    REPRODUCER( 3, "Reproducer", "REP" ),
    /**  */
    DIST_SRC( 4, "Distributed Source", "DSS" ),
    /**  */
    DIRECT_SRC( 5, "Direct Source", "DRS" ),
    /**  */
    OTHER( 6, "Other", "OTH" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;
    /**  */
    public final String key;

    /***************************************************************************
     * @param value
     * @param name
     * @param key
     **************************************************************************/
    private DataSourceType( int value, String name, String key )
    {
        this.value = value;
        this.name = name;
        this.key = key;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * @param field
     * @return
     **************************************************************************/
    public static DataSourceType fromValue( int field )
    {
        return INamedValue.fromValue( field, values(), null );
    }

    /***************************************************************************
     * @param field
     * @return
     **************************************************************************/
    public static DataSourceType fromKey( String key )
    {
        DataSourceType item = null;

        for( DataSourceType v : values() )
        {
            if( v.key.equals( key ) )
            {
                item = v;
                break;
            }
        }

        return item;
    }
}
