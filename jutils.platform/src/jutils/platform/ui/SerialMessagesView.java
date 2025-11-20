package jutils.platform.ui;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.io.CircularItemsStream;
import jutils.core.io.DataStream;
import jutils.core.io.FileStream;
import jutils.core.io.IDataStream;
import jutils.core.io.IItemStream;
import jutils.core.io.IOUtils;
import jutils.core.io.IStringWriter;
import jutils.core.io.ReferenceItemStream;
import jutils.core.io.ReferenceStream;
import jutils.core.time.TimeUtils;
import jutils.core.ui.PaginatedTableView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.event.ItemActionListener;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.model.ITableConfig;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;
import jutils.core.ui.net.NetMessagesTableConfig.EmptyMessageFields;
import jutils.core.ui.net.NetMessagesTableConfig.IMessageFields;
import jutils.platform.data.SerialMessage;
import jutils.platform.data.SerialMessage.SerialMessageSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialMessagesView implements IView<JComponent>
{
    /** The paginated table of {@link SerialMessage}s. */
    private final PaginatedTableView<SerialMessage> table;
    /** The {@link ITableConfig} for {@link SerialMessage}s. */
    private final SerialMessagesTableConfig tableCfg;

    /** The button for loading a file of SerialMessages. */
    private final JButton openButton;
    /**
     * The button that toggles between hexadecimal and text for the message
     * contents column.
     */
    private final JButton hexTextButton;

    /** {@code true} if the contents column is displaying hex. */
    private boolean isHex;

    /**  */
    private ReferenceItemStream<SerialMessage> refStream;
    /**  */
    private CircularItemsStream<SerialMessage> cirStream;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialMessagesView()
    {
        this( new EmptyMessageFields<>(), null );
    }

    /***************************************************************************
     * @param fields
     * @param msgWriter
     **************************************************************************/
    public SerialMessagesView( IMessageFields<SerialMessage> fields,
        IStringWriter<SerialMessage> msgWriter )
    {
        this( fields, PaginatedTableView.createItemWriterView( msgWriter ),
            false );
    }

    /***************************************************************************
     * @param fields
     * @param msgView
     * @param addScrollPane
     **************************************************************************/
    public SerialMessagesView( IMessageFields<SerialMessage> fields,
        IDataView<SerialMessage> msgView, boolean addScrollPane )
    {
        this( fields, msgView, addScrollPane, false );
    }

    /***************************************************************************
     * @param fields
     * @param msgView
     * @param addScrollPane
     * @param isStaticSize
     **************************************************************************/
    private SerialMessagesView( IMessageFields<SerialMessage> fields,
        IDataView<SerialMessage> msgView, boolean addScrollPane,
        boolean isStaticSize )
    {
        SerialMessageSerializer nmSerializer = new SerialMessageSerializer();
        SerialMessageView nmView = new SerialMessageView( msgView,
            addScrollPane );

        IItemStream<SerialMessage> stream = null;

        if( isStaticSize )
        {
            this.cirStream = new CircularItemsStream<>( 512 );
            stream = cirStream;
        }
        else
        {
            ReferenceStream<SerialMessage> rs;
            try
            {
                @SuppressWarnings( "resource")
                ReferenceStream<
                    SerialMessage> refs = new ReferenceStream<>( nmSerializer );
                rs = refs;
            }
            catch( IOException ex )
            {
                throw new RuntimeException( ex );
            }

            this.refStream = new ReferenceItemStream<>( rs );
            stream = refStream;
        }

        this.tableCfg = new SerialMessagesTableConfig( fields );
        this.table = new PaginatedTableView<>( tableCfg, stream, nmView );

        table.setDefaultRenderer( LocalDateTime.class,
            new LocalDateTimeDecorator() );

        table.setCellRenderer( tableCfg.getColumnNames().length - 1,
            new FontLabelTableCellRenderer( SwingUtils.getFixedFont( 12 ) ) );

        table.addToToolbar( createSaveNetMsgsAction() );
        table.addToToolbar( createSaveMsgsAction() );
        this.openButton = table.addToToolbar( createOpenAction() );

        ItemActionListener<IFileDropEvent> ifde = ( e ) -> openNetMsgsFile(
            e.getItem().getFiles().get( 0 ) );
        openButton.setDropTarget( new FileDropTarget( ifde ) );

        table.addToToolbar();

        this.hexTextButton = table.addToToolbar( createTextHexAction() );
        hexTextButton.setText( "" );

        table.addToToolbar();

        setOpenVisible( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveNetMsgsAction()
    {
        IFileSelected ifs = ( f ) -> saveNetMsgsFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose Net Messages File", true, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );

        listener.addExtension( "Net Messages",
            SerialMessageSerializer.SERMSGS_EXT );

        return new ActionAdapter( listener, "Save Msgs w/ Metadata", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveMsgsAction()
    {
        IFileSelected ifs = ( f ) -> saveMsgsFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose Messages File", true, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_AS_16 );

        listener.addExtension( "Message Payloads",
            SerialMessageSerializer.MSGS_EXT );

        return new ActionAdapter( listener, "Save Msg Payloads", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        IFileSelected ifs = ( f ) -> handleOpenFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose File", false, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

        return new ActionAdapter( listener, "Open", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createTextHexAction()
    {
        ActionListener listener = ( e ) -> toggleHexText();
        Icon icon = IconConstants.getIcon( IconConstants.FONT_16 );

        isHex = true;

        return new ActionAdapter( listener, "Show Text Contents", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void toggleHexText()
    {
        isHex = !isHex;

        Icon icon;
        String text;

        if( isHex )
        {
            icon = IconConstants.getIcon( IconConstants.FONT_16 );
            text = "Show Text Contents";
        }
        else
        {
            icon = IconConstants.getIcon( IconConstants.HEX_16 );
            text = "Show Hex Contents";
        }

        hexTextButton.setIcon( icon );
        hexTextButton.setToolTipText( text );
        tableCfg.setHexText( isHex );

        table.updateTable();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void handleOpenFile( File file )
    {
        String ext = IOUtils.getFileExtension( file ).toLowerCase();

        switch( ext )
        {
            // case SerialMessageSerializer.MSGS_EXT:
            // openMsgsFile( file );
            // break;

            case SerialMessageSerializer.SERMSGS_EXT:
                openNetMsgsFile( file );
                break;

            default:
                break;
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void saveNetMsgsFile( File file )
    {
        synchronized( table.itemsStream )
        {
            saveNetMsgsFile( file, refStream );
        }
    }

    /***************************************************************************
     * @param file
     * @param msgs
     **************************************************************************/
    public static void saveNetMsgsFile( File file,
        Iterable<SerialMessage> msgs )
    {
        SerialMessageSerializer netMsgSerializer = new SerialMessageSerializer();

        try( FileStream fs = new FileStream( file );
             IDataStream ds = new DataStream( fs ) )
        {
            for( SerialMessage netMsg : msgs )
            {
                netMsgSerializer.write( netMsg, ds );
            }
        }
        catch( FileNotFoundException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void saveMsgsFile( File file )
    {
        synchronized( table.itemsLock )
        {
            try( FileStream stream = new FileStream( file ) )
            {
                for( SerialMessage msg : table.itemsStream )
                {
                    stream.write( msg.data );
                }
            }
            catch( FileNotFoundException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            catch( IOException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void openNetMsgsFile( File file )
    {
        ReferenceStream<SerialMessage> rs;
        try
        {
            @SuppressWarnings( "resource")
            ReferenceStream<SerialMessage> refs = new ReferenceStream<>(
                new SerialMessageSerializer(), file );
            rs = refs;
        }
        catch( IOException ex )
        {
            throw new RuntimeException(
                "Unable to read file " + file.getAbsolutePath(), ex );
        }

        refStream = new ReferenceItemStream<>( rs );
        table.setItems( refStream );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return table.getView();
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void addMessage( SerialMessage msg )
    {
        table.addItem( msg );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearMessages()
    {
        table.clearItems();
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setOpenVisible( boolean visible )
    {
        openButton.setVisible( visible );
    }

    /***************************************************************************
     * @param a
     * @return
     **************************************************************************/
    public JButton addToToolbar( Action a )
    {
        return table.addToToolbar( a );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Iterator<SerialMessage> getMsgIterator()
    {
        return table.itemsStream.iterator();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getSelectedIndex()
    {
        return table.getSelectedIndex();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getMessageCount()
    {
        return table.itemsStream.getCount();
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void showMessage( long index )
    {
        table.showItem( index );
    }

    /***************************************************************************
     * @param msgsPerPage
     **************************************************************************/
    public void setMsgsPerPage( int msgsPerPage )
    {
        table.setItemsPerPage( msgsPerPage );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class LocalDateTimeDecorator
        implements ITableCellLabelDecorator
    {
        /**  */
        private final DateTimeFormatter dtf;

        /**
         * 
         */
        public LocalDateTimeDecorator()
        {
            this.dtf = TimeUtils.buildDateTimeDisplayFormat();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            String text = "";

            if( value != null )
            {
                LocalDateTime ldt = ( LocalDateTime )value;
                text = ldt.format( dtf );
            }

            label.setText( text );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FontLabelTableCellRenderer
        implements ITableCellLabelDecorator
    {
        /**  */
        private final Font font;

        /**
         * @param font
         */
        public FontLabelTableCellRenderer( Font font )
        {
            this.font = font;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            label.setFont( font );
        }
    }
}
