package jutils.platform.ui;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

import jutils.core.Utils;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.model.ITableConfig;
import jutils.core.ui.net.NetMessagesTableConfig.EmptyMessageFields;
import jutils.core.ui.net.NetMessagesTableConfig.IMessageFields;
import jutils.platform.data.SerialMessage;

/***************************************************************************
 * 
 **************************************************************************/
public class SerialMessagesTableConfig implements ITableConfig<SerialMessage>
{
    /**  */
    private static final String [] MSG_NAMES = new String[] { "Tx/Rx", "Time",
        "Length", "Contents" };
    /**  */
    private static final Class<?> [] MSG_CLASSES = new Class<?>[] {
        String.class, LocalDateTime.class, Integer.class, String.class };

    /**  */
    private final Charset utf8;
    /**  */
    private final IMessageFields<SerialMessage> fields;
    /**  */
    private final String [] names;
    /**  */
    private final Class<?> [] classes;

    /**  */
    private boolean isHex;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialMessagesTableConfig()
    {
        this( new EmptyMessageFields<>() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialMessagesTableConfig( IMessageFields<SerialMessage> fields )
    {
        this.fields = fields;
        this.utf8 = Charset.forName( "UTF-8" );
        this.names = new String[MSG_NAMES.length + this.fields.getFieldCount()];
        this.classes = new Class<?>[MSG_NAMES.length +
            this.fields.getFieldCount()];

        this.isHex = true;

        for( int i = 0; i < names.length; i++ )
        {
            String name = null;
            Class<?> cls = null;
            if( i < ( MSG_NAMES.length - 1 ) )
            {
                name = MSG_NAMES[i];
                cls = MSG_CLASSES[i];
            }
            else if( i == names.length - 1 )
            {
                name = MSG_NAMES[MSG_NAMES.length - 1];
                cls = MSG_CLASSES[MSG_NAMES.length - 1];
            }
            else
            {
                name = fields.getFieldName( i - MSG_NAMES.length + 1 );
                cls = String.class;
            }
            names[i] = name;
            classes[i] = cls;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String [] getColumnNames()
    {
        return names;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Class<?> [] getColumnClasses()
    {
        return classes;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getItemData( SerialMessage item, int col )
    {
        int fieldStart = MSG_NAMES.length - 1;
        int contentsCol = names.length - 1;

        if( col < fieldStart )
        {
            switch( col )
            {
                case 0:
                    return item.isTransmitted ? "Tx" : "Rx";

                case 1:
                    return item.time;

                case 2:
                    return item.data.length;

                default:
                    throw new IllegalStateException( "Programmer error" );
            }
        }
        else if( col < contentsCol )
        {
            return fields.getFieldValue( item, col - fieldStart );
        }
        else if( col == contentsCol )
        {
            int cnt = Math.min( item.data.length, isHex ? 32 : 64 );
            byte [] buf = new byte[cnt];
            boolean addDots = cnt != item.data.length;

            Utils.byteArrayCopy( item.data, 0, buf, 0, buf.length );

            String str;

            if( isHex )
            {
                str = HexUtils.toHexString( buf, " " );
            }
            else
            {
                HexUtils.cleanAscii( buf, 0, cnt );
                str = new String( buf, 0, cnt, utf8 );
            }

            str = addDots ? str + " ..." : str;

            return str;
        }

        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setItemData( SerialMessage item, int col, Object data )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( SerialMessage item, int col )
    {
        return false;
    }

    /***************************************************************************
     * @param isHex
     **************************************************************************/
    public void setHexText( boolean isHex )
    {
        this.isHex = isHex;
    }
}
