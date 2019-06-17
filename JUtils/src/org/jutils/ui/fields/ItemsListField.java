package org.jutils.ui.fields;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.ListUpdater;
import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ItemsListField<T> implements IDataFormField<T>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final CollectionListModel<T> itemsListModel;
    /** The component to display the list. */
    private final JList<T> itemsList;
    /**  */
    private final ItemCellDecorator<T> decorator;
    /**  */
    private final ValidityListenerList listenerList;

    /**  */
    private IUpdater<T> updater;

    /***************************************************************************
     * @param name
     * @param config
     **************************************************************************/
    public ItemsListField( String name, List<T> choices )
    {
        this( name, choices, null );
    }

    /***************************************************************************
     * @param name
     * @param choices
     * @param descriptor
     **************************************************************************/
    public ItemsListField( String name, List<T> choices,
        IDescriptor<T> descriptor )
    {
        descriptor = descriptor == null ? new DefaultItemDescriptor<>()
            : descriptor;

        this.itemsListModel = new CollectionListModel<>();
        this.itemsList = new JList<>( itemsListModel );
        this.decorator = new ItemCellDecorator<>( descriptor );
        this.listenerList = new ValidityListenerList();

        this.view = createView();

        this.updater = null;

        itemsListModel.addAll( choices );

        itemsList.setName( name );
        itemsList.addListSelectionListener(
            new ListUpdater<T>( ( d ) -> fireUpdaters( d ) ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JScrollPane pane = new JScrollPane( itemsList );
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        itemsList.setCellRenderer( new LabelListCellRenderer<>( decorator ) );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );
        pane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( pane, constraints );

        return panel;
    }

    /***************************************************************************
     * @param selected
     **************************************************************************/
    private void fireUpdaters( List<T> selected )
    {
        if( updater != null )
        {
            updater.update( selected.get( 0 ) );
        }
    }

    /***************************************************************************
     * @param decorator
     **************************************************************************/
    public void setDecorator( IListCellLabelDecorator<T> decorator )
    {
        this.decorator.setDecorator( decorator );
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
    public T getValue()
    {
        return itemsList.getSelectedValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        itemsList.setSelectedValue( value, true );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<T> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        itemsList.setEnabled( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return itemsList.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        listenerList.addListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        listenerList.removeListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return listenerList.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ItemCellDecorator<T>
        implements IListCellLabelDecorator<T>
    {
        /**  */
        private final IDescriptor<T> descriptor;

        /**  */
        private IListCellLabelDecorator<T> additionalDecorator;

        /**
         * 
         */
        public ItemCellDecorator( IDescriptor<T> descriptor )
        {
            this.descriptor = descriptor;
            this.additionalDecorator = null;
        }

        /**
         * @param decorator
         */
        public void setDecorator( IListCellLabelDecorator<T> decorator )
        {
            this.additionalDecorator = decorator;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JList<? extends T> list, T value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            label.setText( descriptor.getDescription( value ) );

            if( additionalDecorator != null )
            {
                additionalDecorator.decorate( label, list, value, index,
                    isSelected, cellHasFocus );
            }
        }
    }
}
