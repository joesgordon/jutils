package jutils.telemetry.data.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TmatsFormat implements INamedValue
{
    /**  */
    ASCII( 0, "ASCII" ),
    /**  */
    XML( 1, "XML" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private TmatsFormat( int value, String name )
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
     * @param field
     * @return
     **************************************************************************/
    public static TmatsFormat fromValue( int field )
    {
        switch( field )
        {
            case 0:
                return ASCII;

            case 1:
                return XML;
        }

        return null;
    }
}
