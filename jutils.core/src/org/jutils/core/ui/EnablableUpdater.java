package org.jutils.core.ui;

import org.jutils.core.ui.event.updater.IUpdater;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class EnablableUpdater<T> implements IUpdater<T>
{
    /**  */
    private IUpdater<T> updater;
    /**  */
    private boolean enabled;

    /***************************************************************************
     * 
     **************************************************************************/
    public EnablableUpdater()
    {
        this.updater = null;
        this.enabled = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void update( T data )
    {
        if( updater != null && enabled )
        {
            updater.update( data );
        }
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IUpdater<T> getUpdater()
    {
        return updater;
    }
}
