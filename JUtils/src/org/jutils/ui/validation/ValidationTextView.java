package org.jutils.ui.validation;

import javax.swing.JPanel;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * Wrapper class that adds a {@link ValidationTextField} to a
 * {@link ValidationView}.
 ******************************************************************************/
public class ValidationTextView implements IView<JPanel>
{
    /** The default number of columns for the text field. */
    public static final int DEFAULT_COLUMN_COUNT = 30;

    /**  */
    private final ValidationTextField field;
    /**  */
    private final ValidationView view;

    /***************************************************************************
     * Creates a view with no units and {@link #DEFAULT_COLUMN_COUNT} columns.
     **************************************************************************/
    public ValidationTextView()
    {
        this( null );
    }

    /***************************************************************************
     * @param units
     **************************************************************************/
    public ValidationTextView( String units )
    {
        this( units, DEFAULT_COLUMN_COUNT );
    }

    /***************************************************************************
     * @param units
     * @param columns
     **************************************************************************/
    public ValidationTextView( String units, int columns )
    {
        this.field = new ValidationTextField();

        field.setColumns( columns );
        field.getView().setMinimumSize( field.getView().getPreferredSize() );

        this.view = new ValidationView( field, units );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        view.setEditable( editable );
        field.setEditable( editable );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        field.setText( text );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getText()
    {
        return field.getText();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ValidationTextField getField()
    {
        return field;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view.getView();
    }
}
