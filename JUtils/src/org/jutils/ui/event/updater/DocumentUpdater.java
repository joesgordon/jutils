package org.jutils.ui.event.updater;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

/*******************************************************************************
 * This class acts as a generic document undo listener. Any time an edit happens
 * that can be undone, the edit is added to an {@link UndoManager}. In addition,
 * an {@link IUpdater} can be specified in order to keep a text component's
 * underlying data in sync with edits.
 ******************************************************************************/
public class DocumentUpdater implements DocumentListener
{
    private final IUpdater<String> fupdater;
    private final JTextComponent field;

    public DocumentUpdater( JTextComponent field, IUpdater<String> fupdater )
    {
        this.fupdater = fupdater;
        this.field = field;
    }

    public static interface IFieldUpdater
    {
        public void update( String text );
    }

    @Override
    public void insertUpdate( DocumentEvent e )
    {
        fupdater.update( field.getText() );
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
        fupdater.update( field.getText() );
    }

    @Override
    public void changedUpdate( DocumentEvent e )
    {
        fupdater.update( field.getText() );
    }
}
