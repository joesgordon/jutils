package jutils.multicon.data;

import java.util.*;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum LinkType implements INamedValue
{
    /**  */
    UDP( 0, "UDP" ),
    /**  */
    TCP_SERVER( 1, "TCP Listen" ),
    /**  */
    TCP_CONNECT( 2, "TCP Connect" ),
    /**  */
    SERIAL( 3, "Serial" ),
    /**  */
    BRIDGE( 4, "Bridge" ),
    /**  */
    TRANSFER( 5, "Transfer" ),
    /**  */
    NTP( 6, "NTP" );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private LinkType( int value, String name )
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
     * @return
     **************************************************************************/
    public static List<LinkType> getSortedTypes()
    {
        ArrayList<LinkType> types = new ArrayList<>(
            Arrays.asList( LinkType.values() ) );

        Collections.sort( types, ( this1, that1 ) -> {
            return Integer.compare( this1.value, that1.value );
        } );

        return types;
    }
}
