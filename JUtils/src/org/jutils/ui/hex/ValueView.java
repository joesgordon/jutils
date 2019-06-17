package org.jutils.ui.hex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ValueView implements IView<JPanel>
{
    /**  */
    private static final int DEFAULT_COLS = 17;

    /**  */
    private final JPanel view;

    /**  */
    private final ButtonGroup highlightGroup;

    /**  */
    private final JTextField sint08Field;
    /**  */
    private final JTextField uint08Field;

    /**  */
    private final JTextField sint16Field;
    /**  */
    private final JTextField uint16Field;

    /**  */
    private final JTextField sint32Field;
    /**  */
    private final JTextField uint32Field;

    /**  */
    private final JTextField sint64Field;
    /**  */
    private final JTextField uint64Field;

    /**  */
    private final JTextField floatField;
    /**  */
    private final JTextField doubleField;

    /**  */
    private final List<JTextField> fields;
    /**  */
    private final List<ButtonByteSize> buttonSizes;
    /**  */
    private final ItemActionList<Integer> selectionListeners;

    /**  */
    private ByteOrder dataOrder;
    /**  */
    private byte [] bytes;
    /**  */
    private int offset;

    /***************************************************************************
     * 
     **************************************************************************/
    public ValueView()
    {
        this.highlightGroup = new ButtonGroup();

        this.sint08Field = new JTextField( DEFAULT_COLS );
        this.uint08Field = new JTextField( DEFAULT_COLS );

        this.sint16Field = new JTextField( DEFAULT_COLS );
        this.uint16Field = new JTextField( DEFAULT_COLS );

        this.sint32Field = new JTextField( DEFAULT_COLS );
        this.uint32Field = new JTextField( DEFAULT_COLS );

        this.sint64Field = new JTextField( DEFAULT_COLS );
        this.uint64Field = new JTextField( DEFAULT_COLS );

        this.floatField = new JTextField( DEFAULT_COLS );
        this.doubleField = new JTextField( DEFAULT_COLS );

        this.buttonSizes = new ArrayList<ButtonByteSize>();
        this.fields = new ArrayList<JTextField>();
        this.selectionListeners = new ItemActionList<Integer>();

        this.view = createView();

        setDefaultText();

        this.dataOrder = ByteOrder.BIG_ENDIAN;
        this.bytes = null;
        this.offset = -1;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JRadioButton rbutton;
        ButtonByteSize bsize;

        int row = 1;

        addEndiannessPanel( panel );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 1 );
        buttonSizes.add( bsize );
        rbutton.setSelected( true );
        addField( panel, "Signed Byte:", row++, rbutton, sint08Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 1 );
        buttonSizes.add( bsize );
        addField( panel, "Unsigned Byte:", row++, rbutton, uint08Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 2 );
        buttonSizes.add( bsize );
        addField( panel, "Signed Short:", row++, rbutton, sint16Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 2 );
        buttonSizes.add( bsize );
        addField( panel, "Unsigned Short:", row++, rbutton, uint16Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 4 );
        buttonSizes.add( bsize );
        addField( panel, "Signed Int:", row++, rbutton, sint32Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 4 );
        buttonSizes.add( bsize );
        addField( panel, "Unsigned Int:", row++, rbutton, uint32Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 8 );
        buttonSizes.add( bsize );
        addField( panel, "Signed Long:", row++, rbutton, sint64Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 8 );
        buttonSizes.add( bsize );
        addField( panel, "Unsigned Long:", row++, rbutton, uint64Field );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 4 );
        buttonSizes.add( bsize );
        addField( panel, "Float:", row++, rbutton, floatField );
        highlightGroup.add( rbutton );

        rbutton = new JRadioButton();
        bsize = new ButtonByteSize( rbutton, 8 );
        buttonSizes.add( bsize );
        addField( panel, "Double:", row++, rbutton, doubleField );
        highlightGroup.add( rbutton );

        return panel;
    }

    /***************************************************************************
     * @param panel
     **************************************************************************/
    private void addEndiannessPanel( JPanel panel )
    {
        JPanel ep = new JPanel( new GridBagLayout() );
        ButtonGroup group = new ButtonGroup();
        GridBagConstraints constraints;
        JRadioButton button;

        button = new JRadioButton( "Most Significant Byte First" );
        button.setToolTipText( "Big Endian/Network Order" );
        button.addActionListener(
            new EndiannessListener( this, ByteOrder.BIG_ENDIAN ) );
        button.setSelected( true );
        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 4, 0 ), 0, 0 );
        ep.add( button, constraints );
        group.add( button );

        button = new JRadioButton( "Least Significant Byte First" );
        button.setToolTipText( "Little Endian/Intel Order" );
        button.addActionListener(
            new EndiannessListener( this, ByteOrder.LITTLE_ENDIAN ) );
        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        ep.add( button, constraints );
        group.add( button );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( ep, constraints );
    }

    /***************************************************************************
     * @param panel
     * @param fieldName
     * @param row
     * @param rbutton
     * @param field
     **************************************************************************/
    private void addField( JPanel panel, String fieldName, int row,
        JRadioButton rbutton, JTextField field )
    {
        JLabel label = new JLabel( fieldName );
        GridBagConstraints constraints;

        fields.add( field );

        rbutton.addActionListener( new RadioListener( this ) );
        rbutton.setMinimumSize( rbutton.getPreferredSize() );
        field.setMinimumSize( field.getPreferredSize() );
        field.setEditable( false );

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 2 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 1, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 2, 4, 2 ), 0, 0 );
        panel.add( field, constraints );

        constraints = new GridBagConstraints( 2, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 2 ), 0, 0 );
        panel.add( rbutton, constraints );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSizeSelectedListener( ItemActionListener<Integer> l )
    {
        selectionListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedSize()
    {
        ButtonModel model = highlightGroup.getSelection();

        for( int i = 0; i < buttonSizes.size(); i++ )
        {
            if( model == buttonSizes.get( i ).button.getModel() )
            {
                return buttonSizes.get( i ).size;
            }
        }

        return -1;
    }

    /***************************************************************************
     * @param bytes
     * @param offset
     **************************************************************************/
    public void setBytes( byte [] bytes, int offset )
    {
        this.bytes = bytes;
        this.offset = offset;

        setDefaultText();

        int remaining = bytes.length - offset;
        ByteBuffer buf = ByteBuffer.wrap( bytes, offset,
            remaining > 8 ? 8 : remaining );

        buf.order( dataOrder );

        sint08Field.setText( "" + bytes[offset] );
        uint08Field.setText( toUint08( bytes[offset] ) );

        if( remaining > 1 )
        {
            short s = buf.getShort();
            sint16Field.setText( "" + s );
            uint16Field.setText( toUint16( s ) );
            buf.position( offset );
        }

        if( remaining > 3 )
        {
            int i = buf.getInt();
            sint32Field.setText( "" + i );
            uint32Field.setText( toUint32( i ) );
            buf.position( offset );
            floatField.setText( "" + buf.getFloat() );
            buf.position( offset );
        }

        if( remaining > 7 )
        {
            long l = buf.getLong();
            sint64Field.setText( "" + l );
            uint64Field.setText( toUint64( l ) );
            buf.position( offset );
            doubleField.setText( "" + buf.getDouble() );
            buf.position( offset );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setDefaultText()
    {
        for( JTextField field : fields )
        {
            field.setText( "N/A" );
        }
    }

    /***************************************************************************
     * @param b
     * @return
     **************************************************************************/
    private static String toUint08( byte b )
    {
        int i = b;

        if( i < 0 )
        {
            i += 512;
        }

        return "" + i;
    }

    /***************************************************************************
     * @param s
     * @return
     **************************************************************************/
    private static String toUint16( short s )
    {
        int i = s;

        if( i < 0 )
        {
            i += 0x10000;
        }

        return "" + i;
    }

    /***************************************************************************
     * @param i
     * @return
     **************************************************************************/
    private static String toUint32( int i )
    {
        long l = i;

        if( l < 0 )
        {
            l += 0x100000000L;
        }

        return "" + l;
    }

    /***************************************************************************
     * @param l
     * @return
     **************************************************************************/
    private static String toUint64( long l )
    {
        ByteBuffer buf;
        BigInteger unsigned;

        buf = ByteBuffer.allocate( Long.SIZE / Byte.SIZE );
        buf.putLong( l );
        unsigned = new BigInteger( 1, buf.array() );

        return unsigned.toString();
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
    private static class ButtonByteSize
    {
        public final JRadioButton button;
        public final int size;

        public ButtonByteSize( JRadioButton rbutton, int size )
        {
            this.button = rbutton;
            this.size = size;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RadioListener implements ActionListener
    {
        private final ValueView view;

        public RadioListener( ValueView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int size = view.getSelectedSize();
            Integer selected = size > -1 ? size : null;

            view.selectionListeners.fireListeners( view, selected );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EndiannessListener implements ActionListener
    {
        private ValueView view;
        private ByteOrder order;

        public EndiannessListener( ValueView view, ByteOrder order )
        {
            this.view = view;
            this.order = order;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.dataOrder = order;
            if( view.bytes != null )
            {
                view.setBytes( view.bytes, view.offset );
            }
        }
    }
}
