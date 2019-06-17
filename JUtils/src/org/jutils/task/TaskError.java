package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskError
{
    /**  */
    public final String name;
    /**  */
    public final String message;
    /**  */
    public final String description;
    /**  */
    public final Throwable exception;

    /***************************************************************************
     * @param name
     * @param message
     **************************************************************************/
    public TaskError( String name, String message )
    {
        this( name, message, null, null );
    }

    /***************************************************************************
     * @param name
     * @param message
     * @param description
     **************************************************************************/
    public TaskError( String name, String message, String description )
    {
        this( name, message, description, null );
    }

    /***************************************************************************
     * @param name
     * @param exception
     **************************************************************************/
    public TaskError( String name, Throwable exception )
    {
        this( name, null, null, exception );
    }

    /***************************************************************************
     * @param name
     * @param message
     * @param exception
     **************************************************************************/
    public TaskError( String name, String message, Throwable exception )
    {
        this( name, message, null, exception );
    }

    /***************************************************************************
     * @param name
     * @param message
     * @param description
     * @param exception
     **************************************************************************/
    public TaskError( String name, String message, String description,
        Throwable exception )
    {
        this.name = name;
        this.message = message;
        this.description = description;
        this.exception = exception;
    }
}
