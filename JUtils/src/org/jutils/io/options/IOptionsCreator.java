package org.jutils.io.options;

/*******************************************************************************
 * Interface used to create a default set of options when an error results
 * during a read from file.
 * @param <T> The type of options to be created.
 ******************************************************************************/
public interface IOptionsCreator<T>
{
    /***************************************************************************
     * Creates a default set of options.
     * @return the options created.
     **************************************************************************/
    public T createDefaultOptions();

    /***************************************************************************
     * Called to initialize fields that may have been read as null.
     * @param itemRead
     * @return
     **************************************************************************/
    public T initialize( T itemRead );

    /***************************************************************************
     * Called if there is any issue in reading or writing the file.
     * @param message
     **************************************************************************/
    public void warn( String message );
}
