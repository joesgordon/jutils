package jutils.core.ui.fields;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import jutils.core.IconConstants;
import jutils.core.io.parsers.ExistenceType;
import jutils.core.io.parsers.FileParser;
import jutils.core.io.parsers.FileType;
import jutils.core.ui.FileContextMenu;
import jutils.core.ui.FileIcon;
import jutils.core.ui.IconTextField;
import jutils.core.ui.event.DirectoryChooserListener;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.TextFieldFilesListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines a form field that edits a file path.
 ******************************************************************************/
public class FileFormField implements IDataFormField<File>
{
    /** The panel that contains all the components in this view. */
    private final JPanel view;
    /**  */
    private final JTextField jtf;
    /** The field used to display the file path and its associated icon. */
    private final IconTextField textField;
    /** The validation field used to display the {@link #textField}. */
    private final ParserFormField<File> parserField;
    /** The button used to display an open file dialog. */
    private final JButton button;
    /** The listener called when the user clicks the browse button. */
    private final ActionListener browseListener;
    /** The file listener called when the user clicks the browse button. */
    private final FileChooserListener fileListener;
    /** The icon shown in {@link #textField}. */
    private final FileIcon icon;
    /** The parser to be used for {@link #parserField}. */
    private final FileParser parser;

    /**  */
    private IUpdater<File> updater;

    /***************************************************************************
     * Creates a file form field with the provided name and an
     * {@link ExistenceType} of {@link FileType#FILE} that is required which
     * shows "Save" text and a browse button.
     * @param name the name of this field.
     **************************************************************************/
    public FileFormField( String name )
    {
        this( name, FileType.FILE );
    }

    /***************************************************************************
     * Creates a file form field with the provided name and
     * {@link ExistenceType} that ensures the file exists if the existence type
     * is {@link FileType#FILE} and shows "Save" icon on a browse button.
     * @param name the name of this field.
     * @param fileType type of file to be checked: file/dir/either.
     **************************************************************************/
    public FileFormField( String name, FileType fileType )
    {
        this( name, fileType, ExistenceType.PARENT_EXISTS );
    }

    /***************************************************************************
     * @param name the name of this field.
     * @param fileType type of file to be checked: file/dir/either.
     * @param existence the existence type to be checked.
     **************************************************************************/
    public FileFormField( String name, FileType fileType,
        ExistenceType existence )
    {
        this( name, fileType, existence, false );
    }

    /***************************************************************************
     * Creates a file form field with the provided name and
     * {@link ExistenceType} that ensures the file exists if {@code required}
     * and shows "Save" text and a browse button.
     * @param name the name of this field.
     * @param fileType type of file to be checked: file/dir/either.
     * @param existence the existence type to be checked.
     * @param isBlankAllowed whether a blank field returns null or is invalid.
     **************************************************************************/
    public FileFormField( String name, FileType fileType,
        ExistenceType existence, boolean isBlankAllowed )
    {
        this( name, fileType, existence, isBlankAllowed, true );
    }

    /***************************************************************************
     * @param name the name of this field.
     * @param fileType the type of file to be check for.
     * @param existence the existence type to be checked.
     * @param isBlankAllowed whether a blank field returns null or is invalid.
     * @param showButton denotes whether the browse button should be shown.
     **************************************************************************/
    public FileFormField( String name, FileType fileType,
        ExistenceType existence, boolean isBlankAllowed, boolean showButton )
    {
        boolean isSave = existence == ExistenceType.PARENT_EXISTS;

        this.jtf = new JTextField( 20 );
        this.parser = new FileParser( fileType, existence, isBlankAllowed );

        this.parserField = new ParserFormField<>( name, parser, jtf,
            ( f ) -> f == null ? "" : f.getAbsolutePath() );
        this.textField = new IconTextField( jtf );
        this.button = new JButton();
        this.icon = new FileIcon();

        this.fileListener = createFileListener( isSave );
        this.browseListener = createBrowseListener( fileType );
        this.view = createView( showButton, isSave );

        this.textField.setIcon( icon );

        // textField.setIcon( icon );

        jtf.setText( "" );

        parserField.addValidityChanged( ( v ) -> handleValidityChanged( v ) );
        parserField.setUpdater( ( d ) -> handleFileUpdated( d ) );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void handleFileSelected( File file )
    {
        file = file.getAbsoluteFile();
        setValue( file );
        handleFileUpdated( file );
    }

    /***************************************************************************
     * Invoked when the edited value is valid.
     * @param file the new file object.
     **************************************************************************/
    private void handleFileUpdated( File file )
    {
        // LogUtils.printDebug( "File updated to " + file.getAbsolutePath() );
        //
        // if( file.exists() )
        // {
        // icon.setFile( file );
        // }
        // else
        // {
        // icon.setCheckIcon();
        // }

        if( updater != null )
        {
            updater.update( file );
        }
    }

    /***************************************************************************
     * Invoked when the validity changes on the {@link #parserField}.
     * @param v
     **************************************************************************/
    private void handleValidityChanged( Validity v )
    {
        File f = getValue();

        // LogUtils.printDebug( "File validity to " + v + " for " +
        // parserField.getTextField().getText() );

        if( v.isValid )
        {
            if( f == null || !f.exists() )
            {
                icon.setCheckIcon();
            }
            else
            {
                icon.setFile( f );
            }
        }
        else
        {
            icon.setErrorIcon();
        }

        textField.getView().invalidate();
        textField.getView().validate();
        textField.getView().repaint();
    }

    /***************************************************************************
     * @param isSave
     * @return
     **************************************************************************/
    private FileChooserListener createFileListener( boolean isSave )
    {
        IFileSelected ifs = ( f ) -> handleFileSelected( f );
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Choose File", isSave, ifs, () -> getDefaultFile() );

        return fcl;
    }

    /***************************************************************************
     * Creates the listener for the browse button.
     * @param fileType the type of existence to be checked.
     * @param isSave indicates if the file is being saved ({@code false}
     * indicates open).
     * @return the listener for the browse button.
     **************************************************************************/
    private ActionListener createBrowseListener( FileType fileType )
    {
        ActionListener fcl = null;

        if( fileType != FileType.DIRECTORY )
        {
            fcl = this.fileListener;
        }
        else
        {
            IFileSelected ifs = ( f ) -> handleFileSelected( f );
            fcl = new DirectoryChooserListener( getView(), "Choose Directory",
                ifs, () -> getDefaultFile() );
        }

        return fcl;
    }

    /***************************************************************************
     * Creates the main panel for this view.
     * @param showButton denotes whether the browse button should be shown.
     * @param isSave
     * @return the newly created panel.
     **************************************************************************/
    private JPanel createView( boolean showButton, boolean isSave )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        ActionListener browseListener = this.browseListener;

        parserField.getTextField().setDropTarget( new FileDropTarget(
            new TextFieldFilesListener( jtf, parser.fileType ) ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( textField.getView(), constraints );

        if( showButton )
        {
            Icon icon = isSave ? IconConstants.getIcon( IconConstants.SAVE_16 )
                : IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

            button.setIcon( icon );
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
    public File getValue()
    {
        return parserField.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( File file )
    {
        // LogUtils.printDebug( "Setting data to: \"" + text + "\"" );

        parserField.setValue( file );
        icon.setFile( file );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        parserField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        parserField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return parserField.getValidity();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        button.setEnabled( editable );
        parserField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return parserField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<File> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<File> getUpdater()
    {
        return this.updater;
    }

    /***************************************************************************
     * Adds the provided extension description and extensions to the file
     * dialog.
     * @param description the description of the file type denoted by the
     * extension list.
     * @param extensions the list of extensions of a file type (do not include
     * the '.').
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
        File f = getValue();

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
        private final FileFormField field;
        /** The menu to be displayed. */
        private final FileContextMenu menu;

        /**
         * @param field the field on which the menu is displayed.
         * @param parent the component to be used as the parent of the menu.
         */
        public MenuListener( FileFormField field, Component parent )
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
            File file = field.getValue();

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
