package org.jutils.ui.hex;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.HexBytesFormField;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.ValidationView;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexBytesField implements IDataFormField<byte []>
{
    /**  */
    private final ComboFormField<HexBytesItem> comboField;
    /**  */
    private final HexBytesFormField bytesField;
    /**  */
    private final ValidationView view;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public HexBytesField( String name )
    {
        this( name, new ArrayList<>() );
    }

    /***************************************************************************
     * @param name
     * @param items
     **************************************************************************/
    public HexBytesField( String name, List<byte []> items )
    {
        this.comboField = new ComboFormField<>( name, createList( items ) );
        this.bytesField = new HexBytesFormField( name );
        this.view = new ValidationView( bytesField, null,
            comboField.getView() );

        comboField.getView().setEditable( true );
        comboField.setEditor( new HexBytesComboEditor( bytesField ) );
        comboField.getView().setRenderer(
            new LabelListCellRenderer<>( new HexBytesCellDecorator() ) );

        Dimension dim;

        dim = comboField.getView().getMinimumSize();
        dim.width = 25;
        comboField.getView().setMinimumSize( dim );

        dim = comboField.getView().getPreferredSize();
        dim.width = 50;
        comboField.getView().setPreferredSize( dim );
    }

    /***************************************************************************
     * @param items
     * @return
     **************************************************************************/
    private List<HexBytesItem> createList( List<byte []> items )
    {
        List<HexBytesItem> hbi = new ArrayList<>();

        for( byte [] b : items )
        {
            hbi.add( new HexBytesItem( b ) );
        }

        return hbi;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return comboField.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getValue()
    {
        HexBytesItem item = ( HexBytesItem )comboField.getValue();
        return item == null ? new byte[] {} : item.bytes;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( byte [] value )
    {
        comboField.setValue( new HexBytesItem( value ) );
        bytesField.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<byte []> updater )
    {
        comboField.setUpdater(
            updater == null ? null : ( d ) -> updater.update( d.bytes ) );
        bytesField.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<byte []> getUpdater()
    {
        return bytesField.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        bytesField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        bytesField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        bytesField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
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
        /**  */
        public final byte [] bytes;

        /**
         * @param bytes
         */
        public HexBytesItem( byte [] bytes )
        {
            this.bytes = bytes;
        }

        /**
         * @{@inheritDoc}
         */
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

        /**
         * @{@inheritDoc}
         */
        @Override
        public int hashCode()
        {
            return Arrays.hashCode( bytes );
        }

        /**
         * @{@inheritDoc}
         */
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
        /**
         * @{@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JList<? extends HexBytesItem> list,
            HexBytesItem value, int index, boolean isSelected,
            boolean cellHasFocus )
        {
            String text = value == null ? "" : value.toString();
            label.setText( text );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexBytesComboEditor implements ComboBoxEditor
    {
        /**  */
        private final HexBytesFormField field;

        /**
         * @param bytesField
         */
        public HexBytesComboEditor( HexBytesFormField bytesField )
        {
            this.field = bytesField;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public Component getEditorComponent()
        {
            return field.getTextField();
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void setItem( Object item )
        {
            HexBytesItem value = ( HexBytesItem )item;

            byte [] b = value == null ? null : value.bytes;

            field.setValue( b );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public HexBytesItem getItem()
        {
            byte [] b = field.getValue();
            return b == null ? null : new HexBytesItem( b );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void selectAll()
        {
            field.getTextField().selectAll();
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void addActionListener( ActionListener l )
        {
            field.getTextField().addActionListener( l );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void removeActionListener( ActionListener l )
        {
            field.getTextField().removeActionListener( l );
        }
    }
}
