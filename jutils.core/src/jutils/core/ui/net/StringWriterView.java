package jutils.core.ui.net;

import java.awt.Component;

import javax.swing.JEditorPane;

import jutils.core.SwingUtils;
import jutils.core.io.IStringWriter;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public final class StringWriterView<T> implements IDataView<T>
{
    /** The field for the string representation of the message content. */
    public final JEditorPane editor;
    /**  */
    private final IStringWriter<T> writer;

    /**  */
    private T msg;

    /***************************************************************************
     * @param writer
     **************************************************************************/
    public StringWriterView( IStringWriter<T> writer )
    {
        this.writer = writer;
        this.editor = new JEditorPane();

        editor.setEditable( false );
        editor.setFont( SwingUtils.getFixedFont( 12 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return editor;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T getData()
    {
        return msg;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( T msg )
    {
        this.msg = msg;

        editor.setText( writer.toString( msg ) );
        editor.setCaretPosition( 0 );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        editor.setEditable( editable );
    }
}
