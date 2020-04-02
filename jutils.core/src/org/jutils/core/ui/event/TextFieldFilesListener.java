package org.jutils.core.ui.event;

import java.io.File;
import java.util.List;

import javax.swing.JTextField;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.parsers.ExistenceType;
import org.jutils.core.ui.event.FileDropTarget.IFileDropEvent;

/*******************************************************************************
 * Generic implementation of an {@link ItemActionListener} for use in a
 * {@link FileDropTarget} for a {@link JTextField} when a file/directory is
 * dragged from the native UI to the field.
 ******************************************************************************/
public class TextFieldFilesListener
    implements ItemActionListener<IFileDropEvent>
{
    /** The text field to populate with a path when a file is dropped. */
    private final JTextField field;
    /** The existence to be verified prior to field population. */
    private final ExistenceType existence;

    /***************************************************************************
     * Creates a new listener that populates the provided field when either a
     * file or directory is dropped.
     * @param field the text field to populate.
     **************************************************************************/
    public TextFieldFilesListener( JTextField field )
    {
        this( field, ExistenceType.FILE_OR_DIRECTORY );
    }

    /***************************************************************************
     * Creates a new listener that populates the provided field when a path of
     * the provided existence type is dropped.
     * @param field the text field to populate.
     * @param existence the existence to be checked.
     **************************************************************************/
    public TextFieldFilesListener( JTextField field, ExistenceType existence )
    {
        this.existence = existence;
        this.field = field;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
    {
        List<File> files = event.getItem().getFiles();
        StringBuilder paths = new StringBuilder();

        for( int i = 0; i < files.size(); i++ )
        {
            File file = files.get( i );

            try
            {
                IOUtils.validateFile( file, existence );
            }
            catch( ValidationException e )
            {
                continue;
            }

            if( paths.length() > 0 )
            {
                paths.append( File.pathSeparator );
            }

            paths.append( file.getAbsolutePath() );
        }

        field.setText( paths.toString() );
    }
}
