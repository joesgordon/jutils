package org.jutils.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*******************************************************************************
 *
 ******************************************************************************/
public class FocusPolicyList extends FocusTraversalPolicy
{
    /**  */
    private final List<Component> order;

    /***************************************************************************
     * @param order
     **************************************************************************/
    public FocusPolicyList( List<? extends Component> order )
    {
        this.order = new ArrayList<Component>( order.size() );
        this.order.addAll( order );
    }

    /***************************************************************************
     * @param order
     **************************************************************************/
    public FocusPolicyList( Component... order )
    {
        this( Arrays.asList( order ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getComponentAfter( Container focusCycleRoot,
        Component aComponent )
    {
        int idx = ( order.indexOf( aComponent ) + 1 ) % order.size();
        return order.get( idx );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getComponentBefore( Container focusCycleRoot,
        Component aComponent )
    {
        int idx = order.indexOf( aComponent ) - 1;
        if( idx < 0 )
        {
            idx = order.size() - 1;
        }
        return order.get( idx );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getDefaultComponent( Container focusCycleRoot )
    {
        return order.get( 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getLastComponent( Container focusCycleRoot )
    {
        return order.get( order.size() - 1 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getFirstComponent( Container focusCycleRoot )
    {
        return order.get( 0 );
    }
}
