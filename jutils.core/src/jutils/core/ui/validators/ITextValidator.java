package jutils.core.ui.validators;

import jutils.core.ValidationException;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ITextValidator
{
    /***************************************************************************
     * @param text
     * @throws ValidationException
     **************************************************************************/
    public void validateText( String text ) throws ValidationException;
}
