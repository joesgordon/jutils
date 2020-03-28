package org.jutils.apps.jhex.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.jutils.apps.jhex.JHexIcons;
import org.jutils.apps.jhex.task.DataDistributionTask;
import org.jutils.apps.jhex.task.SearchTask;
import org.jutils.chart.ChartIcons;
import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.datadist.DataDistribution;
import org.jutils.core.io.IStream;
import org.jutils.core.task.TaskView;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.ShadowBorder;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.TitleView;
import org.jutils.core.ui.UnitPositionIndicator;
import org.jutils.core.ui.VerboseMessageView;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.event.RequestFocusListener;
import org.jutils.core.ui.fields.HexBytesFormField;
import org.jutils.core.ui.fields.HexLongFormField;
import org.jutils.core.ui.fields.LongFormField;
import org.jutils.core.ui.hex.BlockBuffer;
import org.jutils.core.ui.hex.ByteBuffer;
import org.jutils.core.ui.hex.HexPanel;
import org.jutils.core.ui.hex.IByteBuffer;
import org.jutils.core.ui.hex.ValueView;
import org.jutils.core.ui.hex.BlockBuffer.DataBlock;
import org.jutils.core.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.validation.AggregateValidityChangedManager;
import org.jutils.core.ui.validation.IValidationField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Displays the contents of a file in a paginated hex table.
 ******************************************************************************/
public class HexFileView implements IDataView<File>
{
    /**  */
    private final JPanel view;

    /**  */
    private final UnitPositionIndicator progressBar;
    /**  */
    private final JLabel offsetLabel;
    /**  */
    private final TitleView fileTitleView;
    /**  */
    private final HexPanel hexView;
    /**  */
    private final TitleView dataTitleView;
    /**
     * The view to show the data in different formats starting at the currently
     * selected byte.
     */
    private final ValueView valuePanel;

    /**  */
    public final Action firstAction;
    /**  */
    public final Action prevAction;
    /**  */
    public final Action nextAction;
    /**  */
    public final Action lastAction;
    /**  */
    public final Action searchAction;
    /**  */
    public final Action gotoAction;
    /**  */
    public final Action analyzeAction;
    /**  */
    public final Action plotAction;

    /**  */
    private final BlockBuffer buffer;

    /**  */
    private byte [] lastSearch;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexFileView()
    {
        this.firstAction = new ActionAdapter( ( e ) -> jumpFirst(),
            "First Data Block",
            IconConstants.getIcon( IconConstants.NAV_FIRST_16 ) );
        this.prevAction = new ActionAdapter( ( e ) -> jumpPrevious(),
            "Previous Data Block",
            IconConstants.getIcon( IconConstants.NAV_PREVIOUS_16 ) );
        this.nextAction = new ActionAdapter( ( e ) -> jumpForward(),
            "Next Data Block",
            IconConstants.getIcon( IconConstants.NAV_NEXT_16 ) );
        this.lastAction = new ActionAdapter( ( e ) -> jumpLast(),
            "Next Data Block",
            IconConstants.getIcon( IconConstants.NAV_LAST_16 ) );

        this.searchAction = new ActionAdapter( ( e ) -> showSearchDialog(),
            "Search", IconConstants.getIcon( IconConstants.FIND_16 ) );
        this.gotoAction = new ActionAdapter( ( e ) -> showGotoDialog(),
            "Go To Byte", JHexIcons.loader.getIcon( JHexIcons.GOTO ) );

        this.analyzeAction = new ActionAdapter( ( e ) -> showAnalyzer(),
            "Analyze", IconConstants.getIcon( IconConstants.ANALYZE_16 ) );
        this.plotAction = new ActionAdapter( ( e ) -> showPlot(), "Plot",
            ChartIcons.getIcon( ChartIcons.CHART_016 ) );

        this.progressBar = new UnitPositionIndicator();
        this.offsetLabel = new JLabel( "" );
        this.hexView = new HexPanel();
        this.fileTitleView = new TitleView( "No File Loaded", createContent() );
        this.valuePanel = new ValueView();
        this.dataTitleView = new TitleView( "Value Selected",
            valuePanel.getView() );

        this.view = createView();

        this.buffer = new BlockBuffer();
        this.lastSearch = null;

        KeyStroke key;
        Action action;
        InputMap inMap = view.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap acMap = view.getActionMap();

        key = KeyStroke.getKeyStroke( "control F" );
        searchAction.putValue( Action.ACCELERATOR_KEY, key );
        searchAction.putValue( Action.MNEMONIC_KEY, ( int )'F' );

        action = new ActionAdapter( new FindAgainListener( this ), "Find Next",
            null );
        key = KeyStroke.getKeyStroke( "F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findNextAction" );
        acMap.put( "findNextAction", action );

        action = new ActionAdapter( new FindAgainListener( this, false ),
            "Find Previous", null );
        key = KeyStroke.getKeyStroke( "shift F3" );
        action.putValue( Action.ACCELERATOR_KEY, key );
        inMap.put( key, "findPrevAction" );
        acMap.put( "findPrevAction", action );

        firstAction.setEnabled( false );
        prevAction.setEnabled( false );
        nextAction.setEnabled( false );
        lastAction.setEnabled( false );
        searchAction.setEnabled( false );
        gotoAction.setEnabled( false );
        analyzeAction.setEnabled( false );
        plotAction.setEnabled( false );

        valuePanel.addSizeSelectedListener( new SizeSelectedListener( this ) );
        addRangeSelectedListener( new SelectionListener( this ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        fileTitleView.getView().setBorder( new ShadowBorder() );
        fileTitleView.getView().setMinimumSize( new Dimension( 500, 100 ) );

        dataTitleView.getView().setBorder( new ShadowBorder() );
        dataTitleView.getView().setMinimumSize(
            dataTitleView.getView().getPreferredSize() );

        panel.add( fileTitleView.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        panel.add( dataTitleView.getView(),
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createContent()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        // ---------------------------------------------------------------------
        // Setup progress bar.
        // ---------------------------------------------------------------------
        progressBar.getView().setLayout( new GridBagLayout() );
        progressBar.getView().add( offsetLabel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 2, 10, 2, 10 ), 0, 0 ) );

        progressBar.setLength( 0 );
        progressBar.setPosition( 0 );
        progressBar.getView().setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        progressBar.addPositionListener( ( d ) -> updatePosition( d ) );

        // setAlternateRowBG( true );
        // setShowGrid( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createToolbar(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( hexView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( progressBar.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, firstAction );
        SwingUtils.addActionToToolbar( toolbar, prevAction );
        SwingUtils.addActionToToolbar( toolbar, nextAction );
        SwingUtils.addActionToToolbar( toolbar, lastAction );

        toolbar.addSeparator();

        JToggleButton jtb = new JToggleButton();
        jtb.setIcon( IconConstants.getIcon( IconConstants.SHOW_DATA ) );
        jtb.setToolTipText( "Show Data" );
        jtb.setFocusable( false );
        jtb.setSelected( true );
        jtb.addActionListener( new ShowDataListener( this, jtb ) );
        toolbar.add( jtb );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, searchAction );
        SwingUtils.addActionToToolbar( toolbar, gotoAction );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, analyzeAction );
        SwingUtils.addActionToToolbar( toolbar, plotAction );

        return toolbar;
    }

    /***************************************************************************
     * @param position
     **************************************************************************/
    private void updatePosition( long position )
    {
        long pos = buffer.getBufferStart( position );

        try
        {
            loadBuffer( pos );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(), "ERROR" );
        }
    }

    /***************************************************************************
     * @param position
     * @throws IOException
     **************************************************************************/
    private void loadBuffer( long position ) throws IOException
    {
        if( position < 0 || position > buffer.getLength() ||
            buffer.isLoaded( position ) )
        {
            return;
        }

        DataBlock block = buffer.loadBufferAt( position );

        hexView.setStartingAddress( position );
        hexView.setBuffer( new ByteBuffer( block.buffer ) );
        long nextOffset = position + block.buffer.length;

        offsetLabel.setText(
            String.format( "Showing 0x%016X - 0x%016X of 0x%016X", position,
                nextOffset - 1, buffer.getLength() ) );

        firstAction.setEnabled( position > 0 );
        prevAction.setEnabled( firstAction.isEnabled() );
        nextAction.setEnabled( position < buffer.getLastPosition() );
        lastAction.setEnabled( nextAction.isEnabled() );
        progressBar.setPosition( position );
        // progressBar.setUnitLength( block.buffer.length );
    }

    /***************************************************************************
     * Displays the dialog that allows the user to go to a particular offset
     * into the file.
     **************************************************************************/
    private void showGotoDialog()
    {
        HexDecLongView offsetView = new HexDecLongView( "Offset" );

        Long ans = OptionUtils.showQuestionView( getView(), "Enter offset:",
            "Enter Offset", offsetView );

        if( ans != null )
        {
            Validity validity = offsetView.getValidity();

            if( validity.isValid )
            {
                long offset = ans;
                highlightOffset( offset, 1 );
            }
            else
            {
                OptionUtils.showErrorMessage( getView(), validity.reason,
                    "Input Error" );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showPlot()
    {
        try( IStream stream = openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            Window w = SwingUtils.getComponentsWindow( getView() );
            DataPlotView plotView = new DataPlotView( stream );
            OkDialogView dialogView = new OkDialogView( w, plotView.getView(),
                ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

            dialogView.setOkButtonText( "Close" );

            dialogView.show( "Data Plot", JHexIcons.getAppImages(),
                new Dimension( 640, 480 ) );
        }
        catch( FileNotFoundException ex )
        {
            OptionUtils.showErrorMessage(
                getView(), "Unable to open file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage(
                getView(), "Unable to read file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showAnalyzer()
    {
        try( IStream stream = openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            DataDistributionTask ddt = new DataDistributionTask( stream );

            TaskView.startAndShow( getView(), ddt, "Analyzing Data" );

            DataDistribution dist = ddt.getDistribution();

            if( dist != null )
            {
                VerboseMessageView msgView = new VerboseMessageView();

                msgView.setMessages( "Finished Analyzing",
                    dist.getDescription() );

                msgView.show( getView(), "Finished Analyzing" );
            }
        }
        catch( FileNotFoundException ex )
        {
            OptionUtils.showErrorMessage(
                getView(), "Unable to open file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage(
                getView(), "Unable to read file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * Displays the dialog that allows the user to enter bytes to be found.
     **************************************************************************/
    private void showSearchDialog()
    {
        if( !isOpen() )
        {
            return;
        }

        HexBytesFormField hexField = new HexBytesFormField( "Hex Bytes" );
        StandardFormView form = new StandardFormView( true );

        form.addField( hexField );

        hexField.getTextField().addAncestorListener(
            new RequestFocusListener() );

        byte [] bytes = OptionUtils.showQuestionField( getView(),
            "Enter Hexadecimal String", "Search", hexField );

        if( bytes != null )
        {
            if( !hexField.getValidity().isValid )
            {
                OptionUtils.showErrorMessage( getView(),
                    hexField.getValidity().reason,
                    "Invalid Hexadecimal Entry" );
                return;
            }

            long fromOffset = getSelectedOffset();

            fromOffset = fromOffset > -1 ? fromOffset : 0;

            search( bytes, fromOffset );
        }
        // else
        // {
        // LogUtils.printDebug( "cancelled" );
        // }
    }

    /***************************************************************************
     * @param bytes
     * @param fromOffset
     **************************************************************************/
    private void search( byte [] bytes, long fromOffset )
    {
        search( bytes, fromOffset, true );
    }

    /***************************************************************************
     * @param bytes
     * @param fromOffset
     * @param isForward
     **************************************************************************/
    private void search( byte [] bytes, long fromOffset, boolean isForward )
    {
        this.lastSearch = bytes;

        // LogUtils.printDebug( "Searching for: " + HexUtils.toHexString( bytes
        // ) +
        // " @ " + fromOffset + " " + ( isForward ? "Forward" : "Backward" ) );

        try( IStream stream = openStreamCopy() )
        {
            if( stream == null )
            {
                return;
            }

            SearchTask task = new SearchTask( bytes, stream, fromOffset,
                isForward );

            TaskView.startAndShow( getView(), task, "Byte Search" );

            long foundOffset = task.foundOffset;

            if( foundOffset > -1 )
            {
                highlightOffset( foundOffset, bytes.length );
            }
        }
        catch( FileNotFoundException ex )
        {
            OptionUtils.showErrorMessage(
                getView(), "Unable to open file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage(
                getView(), "Unable to read file: " +
                    getData().getAbsolutePath() + " because " + ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addRangeSelectedListener( IRangeSelectedListener l )
    {
        hexView.addRangeSelectedListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IByteBuffer getBuffer()
    {
        return hexView.getBuffer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpFirst()
    {
        jump( 0L );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpPrevious()
    {
        jump( buffer.getPreviousPosition() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpForward()
    {
        jump( buffer.getNextPosition() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void jumpLast()
    {
        jump( buffer.getLastPosition() );
    }

    /***************************************************************************
     * @param position
     **************************************************************************/
    private void jump( long position )
    {
        try
        {
            loadBuffer( position );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( view, ex.getMessage(), "ERROR" );
        }
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    public void showData( boolean show )
    {
        dataTitleView.getView().setVisible( show );

        if( show )
        {
            setHighlightLength( valuePanel.getSelectedSize() );
        }
        else
        {
            setHighlightLength( -1 );
        }
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public void setHightlightColor( Color c )
    {
        hexView.setHightlightColor( c );
    }

    /***************************************************************************
     * @param length
     **************************************************************************/
    public void setHighlightLength( int length )
    {
        hexView.setHighlightLength( length );
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public void setBufferSize( int size )
    {
        long pos = buffer.setBufferSize( size );

        progressBar.setUnitLength( size );

        if( buffer.isOpen() )
        {
            try
            {
                loadBuffer( pos );
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( view, ex.getMessage(), "ERROR" );
            }
        }
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
    public File getData()
    {
        return buffer.getFile();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( File data )
    {
        try
        {
            openFile( data );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void openFile( File file ) throws IOException
    {
        try
        {
            buffer.openFile( file );

            fileTitleView.setTitle( file.getName() );
            loadBuffer( 0 );

            long len = buffer.getLength();
            long size = buffer.getBufferSize();
            long unitLen = size > len ? len : size;

            unitLen = unitLen == 0 ? size : unitLen;

            progressBar.setLength( len );
            progressBar.setUnitLength( unitLen );
        }
        finally
        {
            boolean enabled = isOpen();

            prevAction.setEnabled( false );
            nextAction.setEnabled(
                enabled && !buffer.isLoaded( file.length() - 1 ) );

            searchAction.setEnabled( enabled );
            gotoAction.setEnabled( enabled );

            analyzeAction.setEnabled( enabled );
            plotAction.setEnabled( enabled );
        }
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void closeFile() throws IOException
    {
        buffer.closeFile();

        hexView.setBuffer( null );
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void saveFile( File file ) throws IOException
    {
        // TODO fix saving of files.
        // if( file.compareTo( currentFile ) == 0 )
        // {
        // byteStream.close();
        // }
        //
        // try( FileOutputStream fileStream = new FileOutputStream( file ) )
        // {
        // byte [] buffer = hexView.getBuffer().getBytes();
        // fileStream.write( buffer );
        // fileStream.close();
        // }
        //
        // if( file.compareTo( currentFile ) == 0 )
        // {
        // openFile( file );
        // }
    }

    /***************************************************************************
     * @param offset
     * @param length
     **************************************************************************/
    public void highlightOffset( long offset, int length )
    {
        try
        {
            long blockOffset = buffer.getBlockStart( offset );
            loadBuffer( blockOffset );

            int startIndex = ( int )( offset - blockOffset );
            int endIndex = startIndex + length - 1;

            hexView.setSelected( startIndex, endIndex );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(), "ERROR" );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getSelectedOffset()
    {
        int index = hexView.getSelectedByte();
        long offset = -1;

        if( index > -1 )
        {
            offset = buffer.getPositionAt( index );
        }

        return offset;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isOpen()
    {
        return buffer.isOpen();
    }

    /***************************************************************************
     * @return
     * @throws FileNotFoundException
     **************************************************************************/
    public IStream openStreamCopy() throws FileNotFoundException
    {
        return buffer.openStreamCopy();
    }

    /***************************************************************************
     * Action listener for displaying the data dialog.
     **************************************************************************/
    private static class ShowDataListener implements ActionListener
    {
        /**  */
        private final HexFileView view;
        /**  */
        private final JToggleButton button;

        /**
         * @param view
         * @param jtb
         */
        public ShowDataListener( HexFileView view, JToggleButton jtb )
        {
            this.view = view;
            this.button = jtb;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.showData( button.isSelected() );
        }
    }

    /***************************************************************************
     * Listener to update the buffer size.
     **************************************************************************/
    private static class SizeSelectedListener
        implements ItemActionListener<Integer>
    {
        /**  */
        private final HexFileView view;

        /**
         * @param view
         */
        public SizeSelectedListener( HexFileView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<Integer> event )
        {
            int len = event.getItem() == null ? -1 : event.getItem();

            view.setHighlightLength( len );
        }
    }

    /***************************************************************************
     * Listener to update the data dialog as bytes are selected.
     **************************************************************************/
    private static class SelectionListener implements IRangeSelectedListener
    {
        /**  */
        private final HexFileView view;

        /**
         * @param view
         */
        public SelectionListener( HexFileView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void rangeSelected( int start, int end )
        {
            view.valuePanel.setBytes( view.getBuffer().getBytes(), end );

            // LogUtils.printDebug( "col: " + col + ", row: " + row +
            // ", start: "
            // +
            // start + ", end: " + end );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FindAgainListener implements ActionListener
    {
        /**  */
        private final HexFileView view;
        /**  */
        private final boolean isForward;

        /**
         * @param view
         */
        public FindAgainListener( HexFileView view )
        {
            this( view, true );
        }

        /**
         * @param view
         * @param forward
         */
        public FindAgainListener( HexFileView view, boolean forward )
        {
            this.view = view;
            this.isForward = forward;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized void actionPerformed( ActionEvent e )
        {
            if( view.lastSearch != null )
            {
                long off = view.getSelectedOffset();
                off = off + ( isForward ? 1 : -1 );

                // LogUtils.printDebug( "Searching for: " +
                // HexUtils.toHexString( view.lastSearch ) + " @ " +
                // String.format( "%016X", off ) + " " +
                // ( isForward ? "Forward" : "Backward" ) );

                view.search( view.lastSearch, off, isForward );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexDecLongView
        implements IDataView<Long>, IValidationField
    {
        /**  */
        private final LongFormField decimalField;
        /**  */
        private final HexLongFormField hexField;
        /**  */
        private final JPanel view;

        /**  */
        private final AggregateValidityChangedManager validityManager;

        /**
         * @param name
         */
        public HexDecLongView( String name )
        {
            this.decimalField = new LongFormField( name + " (decimal)" );
            this.hexField = new HexLongFormField( name + " (hex)" );
            this.view = createView();

            this.validityManager = new AggregateValidityChangedManager();

            validityManager.addField( decimalField );
            validityManager.addField( hexField );

            decimalField.setUpdater( ( d ) -> hexField.setValue( d ) );
            hexField.setUpdater( ( d ) -> decimalField.setValue( d ) );

            setData( 0L );
        }

        /**
         * @return
         */
        private JPanel createView()
        {
            StandardFormView form = new StandardFormView();

            form.addField( decimalField );
            form.addField( hexField );

            return form.getView();
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Long getData()
        {
            return decimalField.getValue();
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void setData( Long data )
        {
            decimalField.setValue( data );
            hexField.setValue( data );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void addValidityChanged( IValidityChangedListener l )
        {
            validityManager.addValidityChanged( l );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void removeValidityChanged( IValidityChangedListener l )
        {
            validityManager.removeValidityChanged( l );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Validity getValidity()
        {
            return validityManager.getValidity();
        }
    }
}
