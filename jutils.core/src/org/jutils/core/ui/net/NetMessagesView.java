package org.jutils.core.ui.net;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.FileStream;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.IStream;
import org.jutils.core.io.IStringWriter;
import org.jutils.core.net.NetMessage;
import org.jutils.core.net.NetMessageSerializer;
import org.jutils.core.ui.PaginatedTableView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.IView;
import org.jutils.core.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;

/*******************************************************************************
 * Defines UI that displays {@link NetMessage}s.
 ******************************************************************************/
public class NetMessagesView implements IView<JPanel>
{
    /**  */
    private final PaginatedTableView<NetMessage> table;
    /**  */
    private final NetMessagesTableConfig tableCfg;

    /**  */
    private final JButton openButton;
    /**  */
    private final JButton hexTextButton;

    /**  */
    private boolean isHex;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessagesView()
    {
        this( null, null, false );
    }

    /***************************************************************************
     * @param fields
     * @param msgWriter
     **************************************************************************/
    public NetMessagesView( IMessageFields fields,
        IStringWriter<NetMessage> msgWriter )
    {
        this( fields, createMsgWriterView( msgWriter ), true );
    }

    /***************************************************************************
     * @param fields
     * @param msgView
     * @param addScrollPane
     **************************************************************************/
    public NetMessagesView( IMessageFields fields,
        IDataView<NetMessage> msgView, boolean addScrollPane )
    {
        this.tableCfg = new NetMessagesTableConfig( fields );
        this.table = new PaginatedTableView<>( tableCfg,
            new NetMessageSerializer(),
            new NetMessageView( msgView, addScrollPane ) );

        table.setDefaultRenderer( LocalDateTime.class,
            new LocalDateTimeDecorator() );

        table.setCellRenderer( tableCfg.getColumnNames().length - 1,
            new FontLabelTableCellRenderer( SwingUtils.getFixedFont( 12 ) ) );

        table.addToToolbar( createSaveAction() );
        table.addToToolbar( createSaveAsAction() );
        this.openButton = table.addToToolbar( createOpenAction() );

        table.addToToolbar();

        this.hexTextButton = table.addToToolbar( createTextHexAction() );
        hexTextButton.setText( "" );

        table.addToToolbar();

        setOpenVisible( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        IFileSelected ifs = ( f ) -> saveFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose Net Messages File", true, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );

        listener.addExtension( "Net Messages", "netmsgs" );

        return new ActionAdapter( listener, "Save Msgs w/ Metadata", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAsAction()
    {
        IFileSelected ifs = ( f ) -> saveBinFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose Messages File", true, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_AS_16 );

        listener.addExtension( "Message Payloads", "msgs" );

        return new ActionAdapter( listener, "Save Msg Payloads", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        IFileSelected ifs = ( f ) -> openFile( f );
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
    private void saveFile( File file )
    {
        byte [] buf = new byte[IOUtils.DEFAULT_BUF_SIZE];

        synchronized( table.itemsStream )
        {
            try( FileStream stream = new FileStream( file ) )
            {
                @SuppressWarnings( "resource")
                IStream input = table.itemsStream.getItemsStream();

                input.seek( 0L );

                long length = input.getLength();
                long written = 0;

                while( written < length )
                {
                    int count = input.read( buf );

                    stream.write( buf, 0, count );

                    written += count;
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
    public void saveBinFile( File file )
    {
        synchronized( table.itemsStream )
        {
            try( FileStream stream = new FileStream( file ) )
            {
                for( long i = 0L; i < table.itemsStream.getCount(); i++ )
                {
                    NetMessage msg = table.itemsStream.read( i );

                    stream.write( msg.contents );
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
            catch( ValidationException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void openFile( File file )
    {
        table.openFile( file );
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
    public void addMessage( NetMessage msg )
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
    public Iterator<NetMessage> getMsgIterator()
    {
        return table.itemsStream.getIterator();
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
     * @param msgWriter
     * @return
     **************************************************************************/
    private static StringWriterView<NetMessage> createMsgWriterView(
        IStringWriter<NetMessage> msgWriter )
    {
        return msgWriter == null ? null : new StringWriterView<>( msgWriter );
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void showMessage( long index )
    {
        table.showItem( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IMessageFields
    {
        /**
         * @return
         */
        public int getFieldCount();

        /**
         * @param index
         * @return
         */
        public String getFieldName( int index );

        /**
         * @param message
         * @param index
         * @return
         */
        public String getFieldValue( NetMessage message, int index );
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
            this.dtf = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss.SSS" );
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
