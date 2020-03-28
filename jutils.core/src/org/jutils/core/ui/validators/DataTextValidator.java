package org.jutils.core.ui.validators;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.ui.event.updater.IUpdater;

/*******************************************************************************
 * Validator that updates an object with the latest data when the data is valid.
 * @param <T> the type of data to be validated/updated.
 ******************************************************************************/
public class DataTextValidator<T> implements ITextValidator
{
    /** The validator to be used. */
    private final IParser<T> parser;
    /** The updater to be called when data is valid. */
    private final IUpdater<T> updater;

    /***************************************************************************
     * Creates a new validator with the provided data:
     * @param parser the parser to be used to ensure the format of the data.
     * @param updater the updater to be called when the data is valid.
     **************************************************************************/
    public DataTextValidator( IParser<T> parser, IUpdater<T> updater )
    {
        this.parser = parser;
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void validateText( String text ) throws ValidationException
    {
        T data = parser.parse( text );

        updater.update( data );
    }
}
