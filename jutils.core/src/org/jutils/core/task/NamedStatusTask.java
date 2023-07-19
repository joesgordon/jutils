package org.jutils.core.task;

/***************************************************************************
 * 
 **************************************************************************/
public class NamedStatusTask implements IStatusTask
{
    /**  */
    public final String name;
    /**  */
    public final IStatusRunnable runnable;

    /***************************************************************************
     * @param name
     * @param runnable
     **************************************************************************/
    public NamedStatusTask( String name, IStatusRunnable runnable )
    {
        this.name = name;
        this.runnable = runnable;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        runnable.run( handler );
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
