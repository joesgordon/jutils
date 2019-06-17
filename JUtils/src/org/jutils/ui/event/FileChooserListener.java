package org.jutils.ui.event;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/*******************************************************************************
 * Generic {@link ActionListener} for prompting a user for a file. If file
 * extensions are added, the file returned via the listener is ensured to have
 * the file extension selected.
 ******************************************************************************/
public class FileChooserListener implements ActionListener
{
    /** The parent component of the dialog to be displayed. */
    private final Component parent;
    /** The listener called when a file is selected. Never null. */
    private final IFileSelected fileSelected;
    /** The listener called when files are selected. Never null. */
    private final IFilesSelected filesSelected;
    /** The callback to get the previous file selected. Never null. */
    private final ILastFile lastFile;
    /** The callback to get the previous files selected. Never null. */
    private final ILastFiles lastFiles;
    /**
     * Indicates that a save dialog will be shown if {@code true}, an open
     * dialog will be shown otherwise.
     */
    private final boolean isSave;
    /** {@code true} if the user can select multiple files. */
    private final boolean selectMultiple;

    /** The file chooser to be used to generate a dialog. */
    private final DataFileChooser chooser;

    /** The directory last view regardless of user accept/cancel. */
    private File lastDir;

    /***************************************************************************
     * Creates a listener able to select a single file with the following
     * parameters:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param isSave a save dialog will be shown if {@code true}, an open dialog
     * will be shown otherwise.
     * @param fileSelected the listener called when the directory is selected.
     **************************************************************************/
    public FileChooserListener( Component parent, String title, boolean isSave,
        IFileSelected fileSelected )
    {
        this( parent, title, isSave, fileSelected, null );
    }

    /***************************************************************************
     * Creates a listener able to select a single file with the following
     * parameters:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param isSave a save dialog will be shown if {@code true}, an open dialog
     * will be shown otherwise.
     * @param fileSelected the listener called when the file is selected.
     * @param lastFile the callback to get the previous file chosen (may be
     * {@code null}).
     **************************************************************************/
    public FileChooserListener( Component parent, String title, boolean isSave,
        IFileSelected fileSelected, ILastFile lastFile )
    {
        this( parent, title, isSave, false, fileSelected, null, lastFile,
            null );
    }

    /***************************************************************************
     * Creates a listener able to select a multiple files with the following
     * parameters:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param isSave a save dialog will be shown if {@code true}, an open dialog
     * will be shown otherwise.
     * @param filesSelected the listener called when the files are selected.
     **************************************************************************/
    public FileChooserListener( Component parent, String title, boolean isSave,
        IFilesSelected filesSelected )
    {
        this( parent, title, isSave, filesSelected, null );
    }

    /***************************************************************************
     * Creates a listener able to select a multiple files with the following
     * parameters:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param isSave a save dialog will be shown if {@code true}, an open dialog
     * will be shown otherwise.
     * @param filesSelected the listener called when the files are selected.
     * @param lastFiles the callback to get the previous files chosen (may be
     * {@code null}).
     **************************************************************************/
    public FileChooserListener( Component parent, String title, boolean isSave,
        IFilesSelected filesSelected, ILastFiles lastFiles )
    {
        this( parent, title, isSave, true, null, filesSelected, null,
            lastFiles );
    }

    /***************************************************************************
     * Creates a listener with the following parameters:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param isSave a save dialog will be shown if {@code true}, an open dialog
     * will be shown otherwise.
     * @param selectMultiple option to select multiple files (only valid if
     * {@code isSave} is {@code false}.
     * @param fileSelected the listener called when the file is selected.
     * @param filesSelected the listener called when the files are selected.
     * @param lastFile the callback to get the previous file chosen (may be
     * {@code null}).
     * @param lastFiles the callback to get the previous files chosen (may be
     * {@code null}).
     **************************************************************************/
    private FileChooserListener( Component parent, String title, boolean isSave,
        boolean selectMultiple, IFileSelected fileSelected,
        IFilesSelected filesSelected, ILastFile lastFile, ILastFiles lastFiles )
    {
        this.parent = parent;
        this.isSave = isSave;
        this.selectMultiple = selectMultiple;
        this.fileSelected = fileSelected == null ? IFileSelected.nullSelector()
            : fileSelected;
        this.filesSelected = filesSelected == null
            ? IFilesSelected.nullSelector()
            : filesSelected;
        this.lastFile = lastFile == null ? ILastFile.nullSelector() : lastFile;
        this.lastFiles = lastFiles == null ? ILastFiles.nullSelector()
            : lastFiles;

        if( selectMultiple && isSave )
        {
            throw new IllegalArgumentException(
                "Cannot select multiple if saving." );
        }

        this.chooser = new DataFileChooser();

        chooser.setDialogTitle( title );
        chooser.setMultiSelectionEnabled( false );
        chooser.setAcceptAllFileFilterUsed( false );
        chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
    }

    /***************************************************************************
     * Adds an extension to the dialog list of file types.
     * @param description the description of files with the extension
     * @param extensions the extension to be added sans '.'
     **************************************************************************/
    public void addExtension( String description, String... extensions )
    {
        StringBuilder desc = new StringBuilder();

        desc.append( description );
        desc.append( " " );

        for( int i = 0; i < extensions.length; i++ )
        {
            if( i > 0 )
            {
                desc.append( ", " );
            }
            desc.append( "(*." );
            desc.append( extensions[i] );
            desc.append( ')' );
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            desc.toString(), extensions );
        chooser.addChoosableFileFilter( filter );
    }

    /***************************************************************************
     * Sets a component to show additional options to the right of the standard
     * chooser fields.
     * @param comp the component containing fields for additional options.
     **************************************************************************/
    public void setAdditional( JComponent comp )
    {
        chooser.setAdditional( comp );
    }

    /***************************************************************************
     * Sets whether the dialog will show the "All Files" file filter.
     * @param used shows the "All Files" file filter when {@code true}.
     * @see JFileChooser#setAcceptAllFileFilterUsed(boolean)
     **************************************************************************/
    public void setAcceptAllFileFilterUsed( boolean used )
    {
        chooser.setAcceptAllFileFilterUsed( used );
    }

    /***************************************************************************
     * Shows a {@link JFileChooser} in response to an action.
     * @param event ignored.
     * @see ActionListener#actionPerformed(ActionEvent)
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent event )
    {
        int choice;
        File [] selected = null;
        FileFilter filter;

        // ---------------------------------------------------------------------
        // Set the file filter to the first in the list if any have been added.
        // ---------------------------------------------------------------------
        if( chooser.getChoosableFileFilters().length > 0 )
        {
            chooser.setFileFilter( chooser.getChoosableFileFilters()[0] );
        }

        // ---------------------------------------------------------------------
        // Allow multiple selections as desired if not saving.
        // ---------------------------------------------------------------------
        if( !isSave )
        {
            chooser.setMultiSelectionEnabled( selectMultiple );
        }

        // ---------------------------------------------------------------------
        // Set the last selected.
        // ---------------------------------------------------------------------
        if( selectMultiple )
        {
            File [] defaultFiles = lastFiles.getLastFiles();

            if( defaultFiles != null )
            {
                chooser.setSelectedFiles( defaultFiles );
            }
            else if( lastDir != null )
            {
                chooser.setCurrentDirectory( lastDir );
            }
            else
            {
                chooser.setSelectedFile( new File( "." ) );
            }
        }
        else
        {
            File defaultFile = lastFile.getLastFile();

            if( defaultFile != null )
            {
                chooser.setSelectedFile( defaultFile );

                if( !chooser.getSelectedFile().equals( defaultFile ) ||
                    !chooser.getCurrentDirectory().equals(
                        defaultFile.getParentFile() ) )
                {
                    chooser.setCurrentDirectory( defaultFile );
                }
                // LogUtils.printDebug(
                // "Last selected is " + defaultFile.getAbsolutePath() );
            }
            else if( lastDir != null )
            {
                chooser.setCurrentDirectory( lastDir );
            }
            else
            {
                chooser.setSelectedFile( new File( "." ) );
            }
        }

        // ---------------------------------------------------------------------
        // Show the dialog
        // ---------------------------------------------------------------------
        if( isSave )
        {
            choice = chooser.showSaveDialog( parent );
        }
        else
        {
            choice = chooser.showOpenDialog( parent );
        }

        // ---------------------------------------------------------------------
        // Save the last place chosen in case the user cancelled, they may start
        // from where they left off.
        // ---------------------------------------------------------------------
        lastDir = chooser.getCurrentDirectory();

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            // -----------------------------------------------------------------
            // Get the selected file or files
            // -----------------------------------------------------------------
            if( selectMultiple )
            {
                selected = chooser.getSelectedFiles();
            }
            else
            {
                File f = chooser.getSelectedFile();
                if( f != null )
                {
                    selected = new File[] { f };
                }
            }

            if( selected != null && selected.length > 0 )
            {
                // -------------------------------------------------------------
                // Get the absolute file to remove relative paths.
                // -------------------------------------------------------------
                for( int f = 0; f < selected.length; f++ )
                {
                    selected[f] = selected[f].getAbsoluteFile();
                }

                // -------------------------------------------------------------
                // Ensure the correct extension was added if saving.
                // -------------------------------------------------------------
                if( isSave )
                {
                    filter = chooser.getFileFilter();

                    if( filter != null &&
                        filter instanceof FileNameExtensionFilter )
                    {
                        FileNameExtensionFilter fnef = ( FileNameExtensionFilter )filter;
                        for( int i = 0; i < selected.length; i++ )
                        {
                            selected[i] = ensureExtension( selected[i], fnef );
                        }
                    }
                }

                fileSelected.fileChosen( selected[0] );
                filesSelected.filesChosen( selected );
            }
        }
    }

    /***************************************************************************
     * Ensures that the extension specified by the file filter is suffixed onto
     * the file name of the given file.
     * @param selected The file to be checked.
     * @param fileFilter the filter defining the extension to be used.
     * @return a file with the given extension.
     **************************************************************************/
    private static File ensureExtension( File selected,
        FileNameExtensionFilter fileFilter )
    {
        File file = selected;
        String extension = fileFilter.getExtensions()[0];
        String filename = file.getName();
        int dotIndex = filename.length() - extension.length() - 1;

        if( !( filename.endsWith( extension ) &&
            filename.charAt( dotIndex ) == '.' ) )
        {
            file = new File( file.getParentFile(), filename + "." + extension );
        }

        return file;
    }

    /***************************************************************************
     * Removes all file extensions.
     **************************************************************************/
    public void removeAllExtensions()
    {
        FileFilter [] filters = chooser.getChoosableFileFilters();

        for( FileFilter f : filters )
        {
            chooser.removeChoosableFileFilter( f );
        }
    }

    /***************************************************************************
     * A callback that notifies when a file has been selected.
     **************************************************************************/
    public static interface IFileSelected
    {
        /**
         * Called when a file has been selected.
         * @param file the file selected by the user.
         */
        void fileChosen( File file );

        /**
         * Creates a file selector that does nothing.
         * @return the file selector.
         */
        static IFileSelected nullSelector()
        {
            return ( f ) -> {
            };
        }
    }

    /***************************************************************************
     * This callback asks for the last file chosen (usually backed by user
     * data).
     **************************************************************************/
    public static interface ILastFile
    {
        /**
         * Returns the last file the user chose.
         * @return the last file chosen.
         */
        File getLastFile();

        /**
         * Creates a last file selector that returns {@code null}.
         * @return the last file selector.
         */
        static ILastFile nullSelector()
        {
            return () -> {
                return null;
            };
        }
    }

    /***************************************************************************
     * A callback that notifies when files have been selected.
     **************************************************************************/
    public static interface IFilesSelected
    {
        /**
         * Called when files have been selected.
         * @param files the files selected by the user.
         */
        void filesChosen( File [] files );

        /**
         * Creates a file selector that does nothing.
         * @return the file selector.
         */
        static IFilesSelected nullSelector()
        {
            return ( fs ) -> {
            };
        }
    }

    /***************************************************************************
     * This callback asks for the last files chosen (usually backed by user
     * data).
     **************************************************************************/
    public static interface ILastFiles
    {
        /**
         * Returns the last set of files the user chose.
         * @return the last files chosen.
         */
        File [] getLastFiles();

        /**
         * Creates a last file selector that returns an empty array.
         * @return the last file selector.
         */
        static ILastFiles nullSelector()
        {
            return () -> {
                return new File[0];
            };
        }
    }

    /***************************************************************************
     * Defines a {@link JFileChooser} that supports an additional component on
     * the right hand side.
     **************************************************************************/
    private static final class DataFileChooser extends JFileChooser
    {
        /** Generated UID. */
        private static final long serialVersionUID = 407583092267479562L;

        /**
         * Creates the chooser with default options.
         */
        public DataFileChooser()
        {
            super();

            // Color [] colors = { Color.red, Color.green, Color.blue,
            // Color.black };
            //
            // for( int i = 0; i < super.getComponentCount(); i++ )
            // {
            // JComponent comp = ( JComponent )super.getComponent( i );
            //
            // comp.setBorder( new LineBorder( colors[i] ) );
            //
            // LogUtils.printDebug( "Child %d is a %s", i, comp.getClass() );
            // }
        }

        /**
         * Sets a component to show additional options to the right of the
         * standard chooser fields.
         * @param comp the component containing fields for additional options.
         */
        public void setAdditional( JComponent comp )
        {
            BorderLayout layout = ( BorderLayout )super.getLayout();
            BorderLayout newLayout = new BorderLayout();
            JPanel panel = new JPanel( newLayout );

            newLayout.setHgap( layout.getHgap() );
            newLayout.setVgap( layout.getVgap() );

            while( super.getComponentCount() > 0 )
            {
                Component c = super.getComponent( 0 );
                Object constraints = layout.getConstraints( c );

                // LogUtils.printDebug( "Contraints: " + constraints.toString()
                // );

                super.remove( c );
                panel.add( c, constraints );
            }

            Border border = comp.getBorder();

            if( border == null )
            {
                border = new EtchedBorder();
            }

            // int rightBorder = onTop ? 0 : 8;
            // int bottomBorder = onTop ? 8 : 0;

            comp.setBorder(
                new CompoundBorder( new EmptyBorder( 0, 8, 0, 0 ), border ) );

            // String compLoc = onTop ? BorderLayout.NORTH : BorderLayout.WEST;
            // String existingLoc = onTop ? BorderLayout.SOUTH :
            // BorderLayout.EAST;

            super.add( panel, BorderLayout.CENTER );
            super.add( comp, BorderLayout.EAST );

            super.doLayout();
        }
    }
}
