package jutils.hexulator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import jutils.core.ui.model.IDataView;
import jutils.core.utils.ByteOrdering;
import jutils.hexulator.fields.BinaryBytesDataField;
import jutils.hexulator.fields.BinaryLongDataField;
import jutils.hexulator.fields.DecimalBytesDataField;
import jutils.hexulator.fields.DoubleDataField;
import jutils.hexulator.fields.FloatsDataField;
import jutils.hexulator.fields.HeaderDataField;
import jutils.hexulator.fields.HexBytesDataField;
import jutils.hexulator.fields.HexIntsDataField;
import jutils.hexulator.fields.HexLongDataField;
import jutils.hexulator.fields.IDataField;
import jutils.hexulator.fields.IntsDataField;
import jutils.hexulator.fields.Ip4AddressesDataField;
import jutils.hexulator.fields.LongDataField;
import jutils.hexulator.fields.ShortsDataField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataFieldsView implements IDataView<Long>
{
    /**  */
    private final JPanel view;

    /**  */
    private final HeaderDataField headerField;

    /**  */
    private final BinaryBytesDataField binBytesField;
    /**  */
    private final DecimalBytesDataField decBytesField;
    /**  */
    private final HexBytesDataField hexBytesField;

    /**  */
    private final BinaryLongDataField binaryLongField;
    /**  */
    private final LongDataField longField;
    /**  */
    private final HexLongDataField hexLongField;

    /**  */
    private final IntsDataField intsField;
    /**  */
    private final HexIntsDataField hexIntsField;

    /**  */
    private final ShortsDataField shortsField;

    /**  */
    private final DoubleDataField doubleField;
    /**  */
    private final FloatsDataField floatsField;

    /**  */
    private final Ip4AddressesDataField ipsField;
    /**  */
    private final IDataField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataFieldsView()
    {
        this.headerField = new HeaderDataField();

        this.binBytesField = new BinaryBytesDataField();
        this.decBytesField = new DecimalBytesDataField();
        this.hexBytesField = new HexBytesDataField();

        this.binaryLongField = new BinaryLongDataField();
        this.longField = new LongDataField();
        this.hexLongField = new HexLongDataField();

        this.intsField = new IntsDataField();
        this.hexIntsField = new HexIntsDataField();
        this.shortsField = new ShortsDataField();

        this.doubleField = new DoubleDataField();
        this.floatsField = new FloatsDataField();

        this.ipsField = new Ip4AddressesDataField();

        this.fields = new IDataField[] { headerField, binBytesField,
            decBytesField, hexBytesField, binaryLongField, longField,
            hexLongField, intsField, hexIntsField, shortsField, doubleField,
            floatsField, ipsField };

        this.view = createView();

        setData( 0L );

        view.setMinimumSize( view.getPreferredSize() );

        for( IDataField field : fields )
        {
            field.setUpdater( d -> setData( d, field ) );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int r = 0;

        headerField.addTo( "", panel, r++ );

        binBytesField.addTo( "Bytes (0b)", panel, r++ );
        decBytesField.addTo( "Bytes (0d)", panel, r++ );
        hexBytesField.addTo( "Bytes (0x)", panel, r++ );

        constraints = new GridBagConstraints( 1, r++, 8, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 0, 2, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        binaryLongField.addTo( "Long (0b)", panel, r++ );
        longField.addTo( "Long (0d)", panel, r++ );
        hexLongField.addTo( "Long (0x)", panel, r++ );

        intsField.addTo( "Integer (0d)", panel, r++ );
        hexIntsField.addTo( "Integer (0x)", panel, r++ );

        shortsField.addTo( "Short", panel, r++ );

        doubleField.addTo( "Double", panel, r++ );
        floatsField.addTo( "Float", panel, r++ );

        ipsField.addTo( "IP4s", panel, r );

        // GridBagConstraints constraints = new GridBagConstraints( 0, r++, 10,
        // 1,
        // 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
        // new Insets( 0, 0, 0, 0 ), 0, 0 );
        // panel.add( Box.createHorizontalStrut( 0 ), constraints );

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
    public void setData( Long data )
    {
        long value = data;

        byte [] bytes = new byte[8];

        bytes[0] = ( byte )( value & 0xFF );
        bytes[1] = ( byte )( ( value >> 8 ) & 0xFF );
        bytes[2] = ( byte )( ( value >> 16 ) & 0xFF );
        bytes[3] = ( byte )( ( value >> 24 ) & 0xFF );
        bytes[4] = ( byte )( ( value >> 32 ) & 0xFF );
        bytes[5] = ( byte )( ( value >> 40 ) & 0xFF );
        bytes[6] = ( byte )( ( value >> 48 ) & 0xFF );
        bytes[7] = ( byte )( ( value >> 54 ) & 0xFF );

        setData( bytes );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Long getData()
    {
        return longField.getValue();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void setData( byte [] data )
    {
        setData( data, null );
    }

    /***************************************************************************
     * @param data
     * @param fieldException
     **************************************************************************/
    private void setData( byte [] data, IDataField fieldException )
    {
        // LogUtils.printDebug( "Setting data to %s",
        // HexUtils.toHexString( data ) );

        for( IDataField field : fields )
        {
            if( field != fieldException )
            {
                field.setData( data );
            }
        }
    }

    /***************************************************************************
     * @param order
     **************************************************************************/
    public void setOrder( ByteOrdering order )
    {
        for( IDataField field : fields )
        {
            field.setOrder( order );
        }
    }
}
