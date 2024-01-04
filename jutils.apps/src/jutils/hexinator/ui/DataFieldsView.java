package jutils.hexinator.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import jutils.core.ui.model.IDataView;
import jutils.core.utils.ByteOrdering;
import jutils.hexinator.fields.BinaryBytesDataField;
import jutils.hexinator.fields.BinaryLongDataField;
import jutils.hexinator.fields.DecimalBytesDataField;
import jutils.hexinator.fields.DoubleDataField;
import jutils.hexinator.fields.FloatsDataField;
import jutils.hexinator.fields.HeaderDataField;
import jutils.hexinator.fields.HexBytesDataField;
import jutils.hexinator.fields.HexLongDataField;
import jutils.hexinator.fields.IDataField;
import jutils.hexinator.fields.IntsDataField;
import jutils.hexinator.fields.LongDataField;
import jutils.hexinator.fields.ShortsDataField;

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
    private final BinaryLongDataField binaryField;
    /**  */
    private final LongDataField longField;
    /**  */
    private final HexLongDataField hexField;

    /**  */
    private final IntsDataField intsField;
    /**  */
    private final ShortsDataField shortsField;

    /**  */
    private final DoubleDataField doubleField;
    /**  */
    private final FloatsDataField floatsField;

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

        this.binaryField = new BinaryLongDataField();
        this.longField = new LongDataField();
        this.hexField = new HexLongDataField();

        this.intsField = new IntsDataField();
        this.shortsField = new ShortsDataField();

        this.doubleField = new DoubleDataField();
        this.floatsField = new FloatsDataField();

        this.fields = new IDataField[] { headerField, binBytesField,
            decBytesField, hexBytesField, binaryField, longField, hexField,
            intsField, shortsField, doubleField, floatsField };

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

        binaryField.addTo( "Long (0b)", panel, r++ );
        longField.addTo( "Long", panel, r++ );
        hexField.addTo( "Long (0x)", panel, r++ );

        intsField.addTo( "Integer", panel, r++ );
        shortsField.addTo( "Short", panel, r++ );

        doubleField.addTo( "Double", panel, r++ );
        floatsField.addTo( "Float", panel, r++ );

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
