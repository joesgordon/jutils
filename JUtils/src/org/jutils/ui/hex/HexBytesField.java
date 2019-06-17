package org.jutils.ui.hex;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.HexBytesFormField;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.model.ItemComboBoxModel;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexBytesField implements IDataFormField<byte []>
{
    /**  */
    private final JComboBox<HexBytesItem> comboField;
    /**  */
    private final HexBytesFormField bytesField;
    /**  */
    private final ValidationView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexBytesField()
    {
        this( new ArrayList<>() );
    }

    /***************************************************************************
     * @param quickList
     **************************************************************************/
    public HexBytesField( List<byte []> quickList )
    {
        this.comboField = new JComboBox<>( createModel( quickList ) );
        this.bytesField = new HexBytesFormField( "" );
        this.view = new ValidationView( bytesField, null, comboField );

        comboField.setRenderer( new LabelListCellRenderer<HexBytesItem>(
            new HexBytesCellDecorator() ) );

        comboField.setEditable( true );
        comboField.setEditor( new HexBytesComboEditor( bytesField ) );

        Dimension dim;

        dim = comboField.getMinimumSize();
        dim.width = 25;
        comboField.setMinimumSize( dim );

        dim = comboField.getPreferredSize();
        dim.width = 50;
        comboField.setPreferredSize( dim );
    }

    /***************************************************************************
     * @param quickList
     * @return
     **************************************************************************/
    private static ItemComboBoxModel<HexBytesItem> createModel(
        List<byte []> quickList )
    {
        List<HexBytesItem> items = new ArrayList<>();

        for( byte [] b : quickList )
        {
            items.add( new HexBytesItem( b ) );
        }

        return new ItemComboBoxModel<>( items, false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "SYNC";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte [] getValue()
    {
        HexBytesItem item = ( HexBytesItem )comboField.getSelectedItem();
        return item == null ? new byte[] {} : item.bytes;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( byte [] value )
    {
        comboField.setSelectedItem( new HexBytesItem( value ) );
        bytesField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<byte []> updater )
    {
        bytesField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<byte []> getUpdater()
    {
        return bytesField.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        bytesField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        bytesField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        bytesField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return bytesField.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexBytesItem
    {
        public final byte [] bytes;

        public HexBytesItem( byte [] bytes )
        {
            this.bytes = bytes;
        }

        @Override
        public boolean equals( Object obj )
        {
            if( obj == null )
            {
                return false;
            }

            if( obj instanceof HexBytesItem )
            {
                HexBytesItem item = ( HexBytesItem )obj;

                if( item.bytes.length != bytes.length )
                {
                    return false;
                }

                for( int i = 0; i < bytes.length; i++ )
                {
                    if( bytes[i] != item.bytes[i] )
                    {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode( bytes );
        }

        @Override
        public String toString()
        {
            return HexUtils.toHexString( bytes );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexBytesCellDecorator
        implements IListCellLabelDecorator<HexBytesItem>
    {
        @Override
        public void decorate( JLabel label, JList<? extends HexBytesItem> list,
            HexBytesItem value, int index, boolean isSelected,
            boolean cellHasFocus )
        {
            label.setText( value.toString() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexBytesComboEditor implements ComboBoxEditor
    {
        private final HexBytesFormField field;

        public HexBytesComboEditor( HexBytesFormField bytesField )
        {
            this.field = bytesField;
        }

        @Override
        public Component getEditorComponent()
        {
            return field.getTextField();
        }

        @Override
        public void setItem( Object item )
        {
            HexBytesItem value = ( HexBytesItem )item;

            byte [] b = value == null ? null : value.bytes;

            field.setValue( b );
        }

        @Override
        public HexBytesItem getItem()
        {
            byte [] b = field.getValue();
            return b == null ? null : new HexBytesItem( b );
        }

        @Override
        public void selectAll()
        {
            field.getTextField().selectAll();
        }

        @Override
        public void addActionListener( ActionListener l )
        {
            field.getTextField().addActionListener( l );
        }

        @Override
        public void removeActionListener( ActionListener l )
        {
            field.getTextField().removeActionListener( l );
        }
    }
}
