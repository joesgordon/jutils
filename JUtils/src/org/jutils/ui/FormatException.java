package org.jutils.ui;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FormatException extends Exception
{
    /**  */
    private static final long serialVersionUID = 1150947911135182282L;

    /***************************************************************************
     * @param message
     **************************************************************************/
    public FormatException( String message )
    {
        super( message );
    }

    /***************************************************************************
     * @param message
     * @param cause
     **************************************************************************/
    public FormatException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
