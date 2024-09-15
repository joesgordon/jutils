package jutils.core.ui.net;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

import jutils.core.Utils;
import jutils.core.net.NetMessage;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.model.ITableConfig;
import jutils.core.ui.net.NetMessagesView.IMessageFields;

/***************************************************************************
 * 
 **************************************************************************/
public class NetMessagesTableConfig implements ITableConfig<NetMessage>
{
    /**  */
    private static final String [] NAMES = new String[] { "Tx/Rx", "Time",
        "Local", "Remote", "Length", "Contents" };
    /**  */
    private static final Class<?> [] CLASSES = new Class<?>[] { String.class,
        LocalDateTime.class, String.class, String.class, Integer.class,
        String.class };

    /**  */
    private final Charset utf8;
    /**  */
    private final IMessageFields fields;
    /**  */
    private final String [] names;
    /**  */
    private final Class<?> [] classes;

    /**  */
    private boolean isHex;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessagesTableConfig()
    {
        this( null );
    }

    /***************************************************************************
     * @param fields
     **************************************************************************/
    public NetMessagesTableConfig( IMessageFields fields )
    {
        fields = fields == null ? new EmptyMessageFields() : fields;

        this.fields = fields;
        this.utf8 = Charset.forName( "UTF-8" );
        this.names = new String[NAMES.length + this.fields.getFieldCount()];
        this.classes = new Class<?>[NAMES.length + this.fields.getFieldCount()];

        this.isHex = true;

        for( int i = 0; i < names.length; i++ )
        {
            String name = null;
            Class<?> cls = null;
            if( i < ( NAMES.length - 1 ) )
            {
                name = NAMES[i];
                cls = CLASSES[i];
            }
            else if( i == names.length - 1 )
            {
                name = NAMES[NAMES.length - 1];
                cls = CLASSES[NAMES.length - 1];
            }
            else
            {
                name = fields.getFieldName( i - NAMES.length + 1 );
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
    public Object getItemData( NetMessage item, int col )
    {
        int fieldStart = NAMES.length - 1;
        int contentsCol = names.length - 1;

        if( col < fieldStart )
        {
            switch( col )
            {
                case 0:
                    return item.received ? "Rx" : "Tx";

                case 1:
                    return item.time;

                case 2:
                    return item.local.toString();

                case 3:
                    return item.remote.toString();

                case 4:
                    return item.contents.length;

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
            int cnt = Math.min( item.contents.length, isHex ? 32 : 64 );
            byte [] buf = new byte[cnt];
            boolean addDots = cnt != item.contents.length;

            Utils.byteArrayCopy( item.contents, 0, buf, 0, buf.length );

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
    public void setItemData( NetMessage item, int col, Object data )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( NetMessage item, int col )
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class EmptyMessageFields implements IMessageFields
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int getFieldCount()
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getFieldName( int index )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getFieldValue( NetMessage message, int index )
        {
            return null;
        }
    }
}
