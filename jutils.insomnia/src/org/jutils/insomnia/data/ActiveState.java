package org.jutils.insomnia.data;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ActiveState implements INamedItem
{
    /**  */
    ACTIVE( "Active" ),
    /**  */
    INACTIVE( "Inactive" ),
    /**  */
    IDLE( "Idle" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private ActiveState( String name )
    {
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
}
