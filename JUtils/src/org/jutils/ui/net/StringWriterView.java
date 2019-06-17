package org.jutils.ui.net;

import java.awt.Component;

import javax.swing.JEditorPane;

import org.jutils.SwingUtils;
import org.jutils.io.IStringWriter;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public final class StringWriterView<T> implements IDataView<T>
{
    /** The field for the string representation of the message content. */
    private final JEditorPane editor;
    /**  */
    private final IStringWriter<T> writer;

    /**  */
    private T msg;

    /**
     * @param writer
     */
    public StringWriterView( IStringWriter<T> writer )
    {
        this.writer = writer;
        this.editor = new JEditorPane();

        editor.setEditable( false );
        editor.setFont( SwingUtils.getFixedFont( 12 ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getView()
    {
        return editor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getData()
    {
        return msg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData( T msg )
    {
        this.msg = msg;

        editor.setText( writer.toString( msg ) );
        editor.setCaretPosition( 0 );
    }
}
