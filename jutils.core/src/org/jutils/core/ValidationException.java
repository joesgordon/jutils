package org.jutils.core;

/*******************************************************************************
 * Defines an exception for validation errors.
 ******************************************************************************/
public class ValidationException extends Exception
{
    /**  */
    private static final long serialVersionUID = 7317348765245800006L;

    /***************************************************************************
     * Creates a new exception with the provided message.
     * @param message the reason for the error.
     **************************************************************************/
    public ValidationException( String message )
    {
        super( message );
    }

    /***************************************************************************
     * Creates a new exception with the provided message and cause.
     * @param message the reason for the error.
     * @param cause the exception to be encapsulated.
     **************************************************************************/
    public ValidationException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
