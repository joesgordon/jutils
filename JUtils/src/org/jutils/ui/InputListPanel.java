package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InputListPanel<T> implements IDataView<List<T>>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTextField textfield;
    /**  */
    private final CollectionListModel<T> itemsListModel;
    /**  */
    private final JList<T> list;

    /**  */
    private final ItemActionList<T> selectionListeners;

    /**  */
    private List<T> items;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public InputListPanel( List<T> data )
    {
        this.textfield = new JTextField( 5 );
        this.itemsListModel = new CollectionListModel<>();
        this.list = new JList<>( itemsListModel );
        this.view = createView();

        this.selectionListeners = new ItemActionList<>();

        setData( data );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JScrollPane scroll = new JScrollPane( list );

        textfield.addActionListener( new EnterListener<T>( this ) );
        // list.setVisibleRowCount( 4 );
        list.addListSelectionListener( new ItemSelected<T>( this ) );
        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        panel.add( textfield,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        panel.add( scroll,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return panel;
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
    @Override
    public List<T> getData()
    {
        return items;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<T> data )
    {
        this.items = data;
        itemsListModel.setData( data );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( T [] data )
    {
        setData( Arrays.asList( data ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setToolTipText( String text )
    {
        view.setToolTipText( text );
        textfield.setToolTipText( text );
        list.setToolTipText( text );
    }

    /***************************************************************************
     * @param sel
     **************************************************************************/
    public void setSelected( T sel )
    {
        list.setSelectedValue( sel, true );
        list.ensureIndexIsVisible( list.getSelectedIndex() );
        textfield.setText( sel.toString() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelected()
    {
        return list.getSelectedValue();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSelectionListener( ItemActionListener<T> l )
    {
        selectionListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EnterListener<T> implements ActionListener
    {
        private final InputListPanel<T> view;

        public EnterListener( InputListPanel<T> view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            ListModel<T> model = view.list.getModel();
            String key = view.textfield.getText().toLowerCase();
            for( int k = 0; k < model.getSize(); k++ )
            {
                T data = model.getElementAt( k );
                String str = data.toString().toLowerCase( Locale.ENGLISH );

                if( str.startsWith( key ) )
                {
                    view.list.setSelectedValue( data, true );
                    break;
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ItemSelected<T> implements ListSelectionListener
    {
        private final InputListPanel<T> view;

        public ItemSelected( InputListPanel<T> view )
        {
            this.view = view;
        }

        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            T obj = view.list.getSelectedValue();

            if( obj != null )
            {
                view.textfield.setText( obj.toString() );
            }

            view.selectionListeners.fireListeners( view, obj );
        }
    }
}
