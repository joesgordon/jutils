package org.jutils.ui.validators;

import org.jutils.ValidationException;

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
