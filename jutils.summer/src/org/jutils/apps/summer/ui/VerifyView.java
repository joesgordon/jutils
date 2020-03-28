package org.jutils.apps.summer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileSystemView;

import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.apps.summer.data.InvalidChecksum;
import org.jutils.apps.summer.data.SumFile;
import org.jutils.apps.summer.io.ChecksumFileSerializer;
import org.jutils.apps.summer.tasks.VerificationTasksManager;
import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.data.UIProperty;
import org.jutils.core.io.parsers.ExistenceType;
import org.jutils.core.task.MultiTaskView;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.TitleView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileDropTarget;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.core.ui.fields.FileFormField;
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
public class VerifyView implements IDataView<ChecksumResult>, IValidationField
{
    /**  */
    private final FileFormField commonDirField;
    /**  */
    private final ItemsTableModel<SumFile> tableModel;
    /**  */
    private final JTable table;
    /**  */
    private final JPanel view;

    /**  */
    private final ValidityListenerList validityListeners;

    /**  */
    private ChecksumResult input;

    /***************************************************************************
     * 
     **************************************************************************/
    public VerifyView()
    {
        this.commonDirField = new FileFormField( "Common Directory",
            ExistenceType.DIRECTORY_ONLY );
        this.tableModel = new ItemsTableModel<>( new ChecksumsTableModel() );
        this.table = new JTable( tableModel );

        this.view = createView();

        this.validityListeners = new ValidityListenerList();

        setData( new ChecksumResult() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 8, 8, 8 ), 0, 0 );
        panel.add( createTablePanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createForm()
    {
        StandardFormView form = new StandardFormView();

        commonDirField.setUpdater( ( f ) -> setCommonFile( f ) );

        form.addField( commonDirField );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createTablePanel()
    {
        TitleView view = new TitleView();
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane pane = new JScrollPane( table );

        table.getTableHeader().setReorderingAllowed( false );
        table.setDefaultRenderer( String.class,
            new LabelTableCellRenderer( new ChecksumRenderer( this ) ) );
        table.getSelectionModel().setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION );

        pane.getViewport().setBackground( table.getBackground() );
        pane.setDropTarget(
            new FileDropTarget( new OpenChecksumFileDropTarget( this ) ) );
        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( pane, BorderLayout.CENTER );

        view.setTitle( "Checksums" );
        view.setComponent( panel );

        return view.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();
        Action action;
        Icon icon;

        SwingUtils.setToolbarDefaults( toolbar );

        toolbar.setBorderPainted( true );
        toolbar.setBorder( new MatteBorder( 0, 0, 1, 0, Color.gray ) );

        FileChooserListener openListener = new FileChooserListener( view,
            "Choose Checksum File", false,
            new OpenChecksumFileListener( this ) );

        openListener.addExtension( "MD5 Checksum File", "md5" );
        openListener.addExtension( "CRC Checksum File", "crc" );

        icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        action = new ActionAdapter( openListener, "Open Checksum File", icon );
        JButton button = SwingUtils.addActionToToolbar( toolbar, action );

        button.setDropTarget(
            new FileDropTarget( new OpenChecksumFileDropTarget( this ) ) );

        return toolbar;
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
            validityListeners.signalValidity( "No file loaded" );
            return;
        }

        if( input.files.isEmpty() )
        {
            validityListeners.signalValidity( "No files in checksum file" );
            return;
        }

        for( SumFile sf : input.files )
        {
            if( !sf.file.isFile() )
            {
                validityListeners.signalValidity(
                    "File does not exist: " + sf.file.getAbsolutePath() );
                return;
            }
        }

        validityListeners.signalValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChecksumResult getData()
    {
        return input;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ChecksumResult input )
    {
        this.input = input;

        commonDirField.setValue( input.commonDir );
        tableModel.setItems( input.files );

        validate();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityListeners.removeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityListeners.getValidity();
    }

    /**
     * @param numThreads
     * *************************************************************************
     **************************************************************************/
    public void runVerify( int numThreads )
    {
        ChecksumResult input = getData();

        if( !getValidity().isValid )
        {
            OptionUtils.showErrorMessage( view, getValidity().reason,
                "Invalid Configuration" );
            return;
        }

        runVerify( view, input, numThreads );
    }

    /***************************************************************************
     * @param parent
     * @param input
     * @param numThreads
     **************************************************************************/
    public static void runVerify( Component parent, ChecksumResult input,
        int numThreads )
    {
        if( input.type == null )
        {
            OptionUtils.showErrorMessage( parent, "Checksum file not loaded",
                "File Not Loaded" );
            return;
        }

        List<InvalidChecksum> invalidSums;

        invalidSums = new ArrayList<>();
        VerificationTasksManager tasker = new VerificationTasksManager( input,
            invalidSums );
        MultiTaskView.startAndShow( parent, tasker,
            "Verifying checksums for " + input.files.size() + " files",
            numThreads );

        // ChecksumVerificationTask task = new ChecksumVerificationTask( input
        // );
        // TaskView.startAndShow( parent, task, "Verifing Checksums" );
        // invalidSums = task.invalidSums;

        if( invalidSums.isEmpty() )
        {
            OptionUtils.showInfoMessage( parent, "All checksums were valid",
                "Checksums Valid" );
        }
        else
        {
            InvalidChecksumsView invalidView = new InvalidChecksumsView();

            invalidView.setData( invalidSums );

            OptionUtils.showErrorMessage( parent, invalidView.getView(),
                "Invalid Checksums" );
        }

        return;
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void readFile( File file )
    {
        ChecksumFileSerializer cfs = new ChecksumFileSerializer();

        try
        {
            ChecksumResult input = cfs.read( file );

            setData( input );

            for( SumFile sf : input.files )
            {
                if( sf.file.isFile() )
                {
                    sf.length = sf.file.length();
                }
            }
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( view,
                "Error reading file " + file.getAbsolutePath() +
                    Utils.NEW_LINE + ex.getLocalizedMessage(),
                "Read Error" );
        }
        catch( FileNotFoundException ex )
        {
            OptionUtils.showErrorMessage( view,
                "Cannot open file " + file.getAbsolutePath(),
                "File Read Error" );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( view, "I/O error reading file:" +
                ex.getMessage() + Utils.NEW_LINE + file.getAbsolutePath(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void setCommonFile( File file )
    {
        if( input != null )
        {
            for( SumFile sf : input.files )
            {
                if( sf.file.isFile() )
                {
                    sf.length = sf.file.length();
                }
            }
        }

        input.setFiles( file );
        table.repaint();
        validate();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChecksumsTableModel
        implements ITableItemsConfig<SumFile>
    {
        private static final Class<?> [] CLASSES = { String.class,
            String.class };
        private static final String [] NAMES = { "Checksum", "File" };

        @Override
        public String [] getColumnNames()
        {
            return NAMES;
        }

        @Override
        public Class<?> [] getColumnClasses()
        {
            return CLASSES;
        }

        @Override
        public Object getItemData( SumFile checksum, int col )
        {
            switch( col )
            {
                case 0:
                    return checksum.checksum;

                case 1:
                    return checksum.path;

                default:
                    throw new IllegalArgumentException(
                        "Invalid column: " + col );
            }
        }

        @Override
        public void setItemData( SumFile item, int col, Object data )
        {
        }

        @Override
        public boolean isCellEditable( SumFile item, int col )
        {
            return false;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenChecksumFileListener implements IFileSelected
    {
        private final VerifyView view;

        public OpenChecksumFileListener( VerifyView view )
        {
            this.view = view;
        }

        @Override
        public void fileChosen( File file )
        {

            if( file == null || !file.isFile() )
            {
                return;
            }

            view.readFile( file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenChecksumFileDropTarget
        implements ItemActionListener<IFileDropEvent>
    {
        private VerifyView view;

        public OpenChecksumFileDropTarget( VerifyView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            view.readFile( event.getItem().getFiles().get( 0 ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChecksumRenderer implements ITableCellLabelDecorator
    {
        private static final FileSystemView FILE_SYSTEM = FileSystemView.getFileSystemView();

        private final VerifyView view;
        private final Font defaultFont;
        private final Font fixedFont;
        private final Color defaultBackground;

        public ChecksumRenderer( VerifyView view )
        {
            super();

            this.view = view;

            this.defaultFont = UIProperty.LABEL_FONT.getFont();
            this.fixedFont = new Font( Font.MONOSPACED, Font.PLAIN, 12 );
            this.defaultBackground = UIProperty.LABEL_BACKGROUND.getColor();
        }

        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {

            SumFile sum = view.tableModel.getItem( row );
            File commonPath = view.commonDirField.getValue();

            Icon icon = null;

            if( col == 0 )
            {
                label.setFont( fixedFont );
            }
            else
            {
                icon = FILE_SYSTEM.getSystemIcon( sum.file );
                label.setFont( defaultFont );
            }

            label.setIcon( icon );

            if( !isSelected )
            {
                Color bg = defaultBackground;
                if( commonPath == null )
                {
                    bg = Color.red;
                }
                else
                {
                    if( !sum.file.isFile() )
                    {
                        bg = Color.red;
                    }
                }

                label.setBackground( bg );
            }
        }
    }
}
