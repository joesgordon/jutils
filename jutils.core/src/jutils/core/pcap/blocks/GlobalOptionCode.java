package jutils.core.pcap.blocks;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum GlobalOptionCode implements INamedValue
{
    /**  */
    END_OF_OPTIONS( 0, "End of Options" ),
    /**  */
    COMMENT( 1, "Comment" ),
    /**  */
    CUSTOM_2988( 2988, "Custom 2988" ),
    /**  */
    CUSTOM_2989( 2989, "Custom 2989" ),
    /**  */
    CUSTOM_19372( 19372, "Custom 19372" ),
    /**  */
    CUSTOM_19373( 19373, "Custom 19373" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private GlobalOptionCode( int value, String name )
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
     * @param value
     * @return
     **************************************************************************/
    public static GlobalOptionCode fromValue( short value )
    {
        return fromValue( value & 0xFF );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static GlobalOptionCode fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }
}
