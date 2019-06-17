package org.jutils.ui.fields;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.io.parsers.ExistenceType;
import org.jutils.io.parsers.FileParser;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileDropTarget.JTextFieldFilesListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * Defines a field that validates the text as a {@link File}, checking for
 * existence according to constructor parameters.
 ******************************************************************************/
public class FileField implements IDataView<File>, IValidationField
{
    /** The panel that contains all the components in this view. */
    private final JPanel view;
    /** The field used to display the file path and its associated icon. */
    private final IconTextField textField;
    /** The validation field used to display the {@link #textField}. */
    private final ValidationTextComponentField<JTextField> field;
    /** The button used to display an open file dialog. */
    private final JButton button;
    /** Listeners called when the file is updated by the user. */
    private final UpdaterList<File> updateListeners;
    /** The listener called when the user clicks the browse button. */
    private final FileChooserListener fileListener;
    /** The icon shown in {@link #textField}. */
    private final FileIcon icon;

    /***************************************************************************
     * Creates a File view according to the parameters provided:
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.
     * @param isSave if the path is to be be save to (alt. read from).
     * @param showButton denotes whether the browse button should be shown.
     **************************************************************************/
    public FileField( ExistenceType existence, boolean required, boolean isSave,
        boolean showButton )
    {
        this.updateListeners = new UpdaterList<>();

        this.field = new ValidationTextComponentField<>( new JTextField() );
        this.textField = new IconTextField( field.getView() );
        this.button = new JButton();
        this.icon = new FileIcon();

        this.fileListener = createBrowseListener( existence, isSave );
        this.view = createView( existence, required, showButton );

        textField.setIcon( icon );

        // textField.setIcon( icon );

        field.getView().setColumns( 20 );

        field.setText( "" );

        field.addValidityChanged( ( v ) -> {
            if( !v.isValid )
            {
                icon.setErrorIcon();
            }
            else if( !getData().exists() &&
                existence == ExistenceType.DO_NOT_CHECK )
            {
                icon.setCheckIcon();
            }
        } );
    }

    /***************************************************************************
     * Creates the listener for the browse button.
     * @param existence the type of existence to be checked.
     * @param isSave indicates if the file is being saved ({@code false}
     * indicates open).
     * @return the listener for the browse button.
     **************************************************************************/
    private FileChooserListener createBrowseListener( ExistenceType existence,
        boolean isSave )
    {
        FileChooserListener fcl = null;

        if( existence != ExistenceType.DIRECTORY_ONLY )
        {
            IFileSelected ifs = ( f ) -> setData( f );
            fcl = new FileChooserListener( field.getView(), "Choose File",
                isSave, ifs, () -> getDefaultFile() );
        }

        return fcl;
    }

    /***************************************************************************
     * Creates the main panel for this view.
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.s
     * @param showButton denotes whether the browse button should be shown.
     * @return the newly created panel.
     **************************************************************************/
    private JPanel createView( ExistenceType existence, boolean required,
        boolean showButton )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        ActionListener browseListener;

        if( existence == ExistenceType.DIRECTORY_ONLY )
        {
            IFileSelected ifs = ( f ) -> setData( f );
            browseListener = new DirectoryChooserListener( panel,
                "Choose Directory", ifs, () -> getDefaultFile() );
        }
        else
        {
            browseListener = fileListener;
        }

        ITextValidator validator = new DataTextValidator<File>(
            new FileParser( existence, required ),
            ( f ) -> handleFileUpdated( f ) );

        // LogUtils.printDebug( "Setting validator" );
        field.setValidator( validator );
        field.getView().setDropTarget( new FileDropTarget(
            new JTextFieldFilesListener( field.getView(), existence ) ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( textField.getView(), constraints );

        if( showButton )
        {
            button.setIcon(
                IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
            button.addActionListener( browseListener );
            button.setToolTipText( "Browse (Right-click to open path)" );

            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 4, 0, 0 ), 0, 0 );
            panel.add( button, constraints );

            button.addMouseListener( new MenuListener( this, panel ) );
        }

        return panel;
    }

    /***************************************************************************
     * Invoked when the edited value is valid.
     * @param file the new file object.
     **************************************************************************/
    public void handleFileUpdated( File file )
    {
        // LogUtils.printDebug( "File changed to " + file.getAbsolutePath()
        // );
        icon.setFile( file );
        updateListeners.fire( file );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public File getData()
    {
        File file;
        String path = field.getText();

        if( path.isEmpty() )
        {
            file = null;
        }
        else
        {
            file = new File( path ).getAbsoluteFile();
        }

        return file;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( File file )
    {
        String text = "";

        if( file != null )
        {
            text = file.getAbsolutePath();
        }

        // LogUtils.printDebug( "Setting data to: \"" + text + "\"" );

        field.setText( text );
        icon.setFile( file );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * Sets the field as editable according to the provided boolean.
     * @param editable {@code true} if the user can edit the control;
     * {@code false} otherwise.
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        button.setEnabled( editable );
        field.setEditable( editable );
    }

    /***************************************************************************
     * Adds the provided callback to the end of the callbacks invoked when the
     * file is updated.
     * @param updater the listener to be added.
     **************************************************************************/
    public void addUpdater( IUpdater<File> updater )
    {
        updateListeners.add( updater );
    }

    /***************************************************************************
     * Adds the provided extension description and extensions to the file
     * dialog.
     * @param description the description of the file type denoted by the
     * extension list.
     * @param extensions the list of extensions of a file type.
     * @throws IllegalStateException if this field was initialized with
     * {@link ExistenceType#DIRECTORY_ONLY}.
     * @see FileChooserListener#addExtension(String, String...)
     **************************************************************************/
    public void addExtension( String description, String... extensions )
        throws IllegalStateException
    {
        if( fileListener == null )
        {
            throw new IllegalStateException(
                "Cannot add extensions to a chooser that is directory only" );
        }

        fileListener.addExtension( description, extensions );
    }

    /***************************************************************************
     * Returns either the current path or the closest parent path that exists.
     * @return the closest known existing path to the current path.
     **************************************************************************/
    private File getDefaultFile()
    {
        File f = getData();

        while( f != null && !f.exists() )
        {
            f = f.getParentFile();
        }

        return f;
    }

    /***************************************************************************
     * Displays the context menu on the button on right-click.
     **************************************************************************/
    private static class MenuListener extends MouseAdapter
    {
        /** The field on which the menu is displayed. */
        private final FileField field;
        /** The menu to be displayed. */
        private final FileContextMenu menu;

        /**
         * @param field the field on which the menu is displayed.
         * @param parent the component to be used as the parent of the menu.
         */
        public MenuListener( FileField field, Component parent )
        {
            this.field = field;
            this.menu = new FileContextMenu( parent );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            File file = field.getData();

            if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 1 )
            {
                Component c = e.getComponent();
                int x = c.getWidth() / 2; // e.getX();
                int y = c.getHeight() / 2; // e.getY();

                menu.show( file, c, x, y );
            }
        }
    }
}
