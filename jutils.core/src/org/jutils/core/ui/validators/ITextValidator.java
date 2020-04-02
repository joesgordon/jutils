package org.jutils.core.ui.validators;

import org.jutils.core.ValidationException;

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
