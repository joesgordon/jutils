package jutils.core.ui;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jutils.core.ui.fields.StringAreaFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringView implements IDataView<String>
{
    /**  */
    private final JScrollPane pane;
    /**  */
    private final StringAreaFormField field;

    /**  */
    private String text;

    /***************************************************************************
     * 
     **************************************************************************/
    public StringView()
    {
        this.field = new StringAreaFormField( "" );

        JTextArea area = field.getTextArea();
        this.pane = new JScrollPane( area );

        area.setFont( area.getFont().deriveFont( 14.f ) );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return pane;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getData()
    {
        return this.text;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( String data )
    {
        this.text = data;

        field.setValue( text );

        field.getTextArea().setCaretPosition( 0 );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }
}
