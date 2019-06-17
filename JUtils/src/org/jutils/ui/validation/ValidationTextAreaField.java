package org.jutils.ui.validation;

import javax.swing.JTextArea;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ValidationTextAreaField
    extends ValidationTextComponentField<JTextArea>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public ValidationTextAreaField()
    {
        super( new JTextArea() );
    }

    /***************************************************************************
     * @param columns
     **************************************************************************/
    public void setColumns( int columns )
    {
        super.getView().setColumns( columns );
    }
}
