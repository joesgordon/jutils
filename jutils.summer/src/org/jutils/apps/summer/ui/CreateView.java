package org.jutils.apps.summer.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.apps.summer.data.SumFile;
import org.jutils.apps.summer.data.SumFilePathComparator;
import org.jutils.apps.summer.data.SumFileSizeComparator;
import org.jutils.apps.summer.io.ChecksumFileSerializer;
import org.jutils.apps.summer.tasks.CreationTasksManager;
import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.cksum.ChecksumType;
import org.jutils.core.task.MultiTaskView;
import org.jutils.core.task.TaskMetrics;
import org.jutils.core.time.TimeUtils;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.TitleView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.DirectoryChooserListener;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileDropTarget;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.event.FileChooserListener.IFilesSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFiles;
import org.jutils.core.ui.event.FileDropTarget.DropActionType;
import org.jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.core.ui.fields.ComboFormField;
import org.jutils.core.ui.fields.NamedItemDescriptor;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.ITableItemsConfig;
import org.jutils.core.ui.model.ItemsTableModel;
import org.jutils.core.ui.model.LabelTableCellRenderer;
import org.jutils.core.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;
import org.jutils.core.ui.validation.IValidationField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;
import org.jutils.core.ui.validation.ValidityListenerList;

/*******************************************************************************
 * 
 *******************************************************************************/
public class CreateView implements IDataView<ChecksumResult>, IValidationField
{
    /**  */
    private final JPanel view;
    /**  */
    private final ComboFormField<ChecksumType> checksumTypeField;
    /**  */
    private final PathView commonField;
    /**  */
    private final JTextField fileCountField;
    /**  */
    private final JTextField totalSizeField;
    /**  */
    private final ItemsTableModel<SumFile> pathsModel;
    /**  */
    private final JTable table;

    /**  */
    private final ValidityListenerList validityListeners;

    /**  */
    private ChecksumResult input;

    /***************************************************************************
     * 
     **************************************************************************/
    public CreateView()
    {
        this.checksumTypeField = new ComboFormField<>( "Checksum Method",
            ChecksumType.values(), new NamedItemDescriptor<>() );
        this.commonField = new PathView();
        this.fileCountField = new JTextField();
        this.totalSizeField = new JTextField();
        this.pathsModel = new ItemsTableModel<>( new PathModel() );
        this.table = new JTable( pathsModel );

        this.view = createView();

        this.validityListeners = new ValidityListenerList();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        TitleView titleView = new TitleView();
        GridBagConstraints constraints;

        titleView.setTitle( "Files" );
        titleView.setComponent( createFilesComponent( panel ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 8, 8, 8 ), 0, 0 );
        panel.add( titleView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param parent
     * @return
     **************************************************************************/
    private Component createFilesComponent( JPanel parent )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "List", createFilesListComponent() );
        tabs.addTab( "Tree", createFilesTreeComponent() );

        panel.add( createFilesToolbar( parent ), BorderLayout.NORTH );
        panel.add( tabs, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @param parent
     * @return
     **************************************************************************/
    private Component createFilesToolbar( JPanel parent )
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar,
            createAddFileListener( parent ) );
        SwingUtils.addActionToToolbar( toolbar,
            createAddDirListener( parent ) );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createClearAllListener() );

        return toolbar;
    }

    /***************************************************************************
     * @param parent
     * @return
     **************************************************************************/
    private Action createAddFileListener( JPanel parent )
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
        LastFilesListener lfl = new LastFilesListener( this );
        ActionListener listener = new FileChooserListener( parent,
            "Choose File", false, lfl, lfl );
        Action action = new ActionAdapter( listener, "Add File", icon );

        return action;
    }

    /***************************************************************************
     * @param parent
     * @return
     **************************************************************************/
    private Action createAddDirListener( JPanel parent )
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        LastFilesListener lfl = new LastFilesListener( this );
        ActionListener listener = new DirectoryChooserListener( parent,
            "Choose Directory", lfl, lfl );
        Action action = new ActionAdapter( listener, "Add Directory", icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createClearAllListener()
    {
        Icon icon = IconConstants.getIcon( IconConstants.CLOSE_16 );
        ActionListener listener = new ClearAllListener( this );
        Action action = new ActionAdapter( listener, "Clear All", icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFilesListComponent()
    {
        JScrollPane pane = new JScrollPane( table );

        Action deleteAction = new ActionAdapter( new DeleteListener( this ),
            "Remove File", null );
        KeyStroke accel = KeyStroke.getKeyStroke( "DELETE" );
        deleteAction.putValue( Action.ACCELERATOR_KEY, accel );

        table.getInputMap().put( accel, "removeNode" );
        table.getActionMap().put( "removeNode", deleteAction );

        table.getTableHeader().setReorderingAllowed( false );
        table.setDefaultRenderer( String.class,
            new LabelTableCellRenderer( new PathRenderer( this ) ) );
        table.getSelectionModel().setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );

        pane.getViewport().setBackground( table.getBackground() );
        pane.setDropTarget(
            new FileDropTarget( new FilesDroppedListener( this ) ) );

        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        return pane;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFilesTreeComponent()
    {
        JScrollPane pane = new JScrollPane();

        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        pane.setDropTarget(
            new FileDropTarget( new FilesDroppedListener( this ) ) );

        return pane;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        checksumTypeField.setUpdater( ( d ) -> input.type = d );
        commonField.addSelectedListener( new CommonDirChanged( this ) );
        fileCountField.setEditable( false );
        totalSizeField.setEditable( false );

        form.addField( checksumTypeField );
        form.addField( "Common Directory", commonField.getView() );
        form.addField( "File Count", fileCountField );
        form.addField( "Total Size", totalSizeField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void validate()
    {
        if( input == null )
        {
            validityListeners.signalValidity( "No input loaded" );
            return;
        }

        if( input.type == null )
        {
            validityListeners.signalValidity( "No checksum type selected" );
            return;
        }

        if( input.files.isEmpty() )
        {
            validityListeners.signalValidity( "No files loaded" );
            return;
        }

        validityListeners.signalValidity();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ChecksumResult getData()
    {
        return input;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( ChecksumResult data )
    {
        this.input = data;

        checksumTypeField.setValue( data.type );
        commonField.setData( data.commonDir );
        pathsModel.setItems( data.files );

        fileCountField.setText( "" + data.files.size() );
        totalSizeField.setText( IOUtils.byteCount( data.calculateSize() ) );

        validate();
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void setFiles( List<File> files )
    {
        ChecksumResult input = getData();

        input.setFiles( files );

        view.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        setData( input );
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void addFiles( List<File> files )
    {
        ChecksumResult input = getData();

        input.addFiles( files );

        view.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        setData( input );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityListeners.addListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityListeners.removeListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityListeners.getValidity();
    }

    /***************************************************************************
     * @param numThreads
     **************************************************************************/
    public void runCreate( int numThreads )
    {
        ChecksumResult input = getData();

        Collections.sort( input.files, new SumFileSizeComparator() );
        TaskMetrics metrics = runCreate( view, input, numThreads );

        if( metrics != null && !metrics.interrupted )
        {
            Collections.sort( input.files, new SumFilePathComparator() );
            displayChecksums( input, metrics );
        }
    }

    /***************************************************************************
     * @param parent
     * @param input
     * @param numThreads
     * @return
     **************************************************************************/
    public static TaskMetrics runCreate( Component parent, ChecksumResult input,
        int numThreads )
    {
        try
        {
            input.validate();
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( parent, ex.getMessage(),
                "Invalid Inputs" );
            return null;
        }

        CreationTasksManager tasker = new CreationTasksManager( input );

        TaskMetrics metrics = MultiTaskView.startAndShow( parent, tasker,
            "Creating checksums for " + input.files.size() + " files",
            numThreads );

        // ChecksumCreationTask task = new ChecksumCreationTask( input );
        //
        // TaskView.startAndShow( parent, task, "Creating Checksums" );

        return metrics;
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    private void addDirs( File [] files )
    {
        view.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

        List<File> fileList = new ArrayList<>();

        for( File file : files )
        {
            if( file.isDirectory() )
            {
                fileList.addAll( IOUtils.getAllFiles( file ) );
            }
            else
            {
                fileList.add( file );
            }
        }

        addFiles( fileList );
    }

    /***************************************************************************
     * @param result
     * @param metrics
     **************************************************************************/
    private void displayChecksums( ChecksumResult result, TaskMetrics metrics )
    {
        TextView textView = new TextView();
        String text = ChecksumFileSerializer.write( result );
        ChecksumType type = checksumTypeField.getValue();
        String ext = type.toString().toLowerCase();
        String desc = type.toString() + " checksum file (*." + ext + ")";
        JFrame frame = SwingUtils.getComponentsJFrame( view );

        textView.setData( text );
        textView.setEditable( false );
        textView.setExtension( desc, ext );

        OkDialogView dialogView = new OkDialogView( frame, textView.getView(),
            ModalityType.DOCUMENT_MODAL );

        JDialog dialog = dialogView.getView();

        dialog.setTitle( "Checksums (" +
            TimeUtils.durationToString( metrics.getDuration() ) + ")" );
        dialog.setSize( 600, 600 );
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.validate();
        dialog.setLocationRelativeTo( frame );
        dialog.setVisible( true );
        dialog.toFront();

        File cksmFile = null;
        if( result.commonDir != null )
        {
            String fileName = result.commonDir.getName() + "." + ext;
            cksmFile = new File( result.commonDir, fileName );
        }

        textView.showSave( cksmFile );
    }

    /***************************************************************************
     * Called when a file has been added or removed.
     **************************************************************************/
    private static class FilesDroppedListener
        implements ItemActionListener<IFileDropEvent>
    {
        /**  */
        private final CreateView view;

        /**
         * @param view
         */
        public FilesDroppedListener( CreateView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent fde = event.getItem();
            List<File> droppedFiles = fde.getFiles();
            List<File> files = new ArrayList<>();

            view.view.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

            // -----------------------------------------------------------------
            // Add existing files first if added files.
            // -----------------------------------------------------------------
            if( fde.getActionType() == DropActionType.COPY )
            {
                for( SumFile sf : view.pathsModel.getItems() )
                {
                    if( !droppedFiles.contains( sf.file ) )
                    {
                        files.add( sf.file );
                    }
                }
            }

            // -----------------------------------------------------------------
            // Add dropped files or contents if a directory.
            // -----------------------------------------------------------------
            for( File file : droppedFiles )
            {
                if( file.isDirectory() )
                {
                    files.addAll( IOUtils.getAllFiles( file ) );
                }
                else
                {
                    files.add( file );
                }
            }

            view.setFiles( files );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PathModel implements ITableItemsConfig<SumFile>
    {
        /**  */
        private static final Class<?> [] CLASSES = { String.class,
            String.class };
        /**  */
        private static final String [] NAMES = { "Path", "Size" };

        /**
         * {@inheritDoc}
         */
        @Override
        public String [] getColumnNames()
        {
            return NAMES;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> [] getColumnClasses()
        {
            return CLASSES;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getItemData( SumFile sf, int col )
        {
            switch( col )
            {
                case 0:
                    return sf.path;

                case 1:
                    return IOUtils.byteCount( sf.length );

                default:
                    throw new IllegalArgumentException(
                        "Invalid column: " + col );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setItemData( SumFile item, int col, Object data )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( SumFile item, int col )
        {
            return false;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PathRenderer implements ITableCellLabelDecorator
    {
        /**  */
        private static final FileSystemView FILE_SYSTEM = FileSystemView.getFileSystemView();

        /**  */
        private final CreateView view;

        /**
         * @param view
         */
        public PathRenderer( CreateView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            Icon icon = null;

            if( col == 0 )
            {
                SumFile sf = view.pathsModel.getItem( row );
                icon = FILE_SYSTEM.getSystemIcon( sf.file );
                label.setHorizontalAlignment( SwingConstants.LEFT );
            }
            else
            {
                label.setHorizontalAlignment( SwingConstants.RIGHT );
            }

            label.setIcon( icon );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DeleteListener implements ActionListener
    {
        /**  */
        private CreateView view;

        /**
         * @param view
         */
        public DeleteListener( CreateView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int row = view.table.getSelectedRow();

            if( row > -1 )
            {
                view.input.files.remove( row );

                view.setFiles( view.input.buildFileList() );

                if( row >= view.input.files.size() )
                {
                    row--;
                }

                if( row > -1 )
                {
                    view.table.getSelectionModel().setSelectionInterval( row,
                        row );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CommonDirChanged implements ItemActionListener<File>
    {
        /**  */
        private final CreateView view;

        /**
         * @param view
         */
        private CommonDirChanged( CreateView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            ChecksumResult input = view.input;

            input.setPaths( event.getItem() );

            view.checksumTypeField.setValue( input.type );
            view.pathsModel.setItems( input.files );

            view.fileCountField.setText( "" + input.files.size() );
            view.totalSizeField.setText(
                IOUtils.byteCount( input.calculateSize() ) );

            view.validate();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class LastFilesListener implements IFilesSelected, ILastFiles
    {
        /**  */
        private final CreateView view;
        /**  */
        private File [] lastFiles = null;

        /**
         * @param view
         */
        public LastFilesListener( CreateView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public File [] getLastFiles()
        {
            return lastFiles;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void filesChosen( File [] files )
        {
            this.lastFiles = files;

            view.addDirs( files );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ClearAllListener implements ActionListener
    {
        /**  */
        private final CreateView view;

        /**
         * @param view
         */
        private ClearAllListener( CreateView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent event )
        {
            view.pathsModel.clearItems();
            view.input.files.clear();
            view.validate();
        }
    }
}
