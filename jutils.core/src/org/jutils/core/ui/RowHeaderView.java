package org.jutils.core.ui;

import java.awt.Component;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JTable;

import org.jutils.core.data.UIProperty;
import org.jutils.core.ui.event.RunnableList;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RowHeaderView
{
    /**  */
    private final RowListModel model;
    /**  */
    private final JList<String> list;

    /***************************************************************************
     * @param table
     **************************************************************************/
    public RowHeaderView( JTable table )
    {
        this.model = new RowListModel( this );
        this.list = new JList<>( model );

        list.setFocusable( false );
        list.setCellRenderer( new RowHeaderRenderer( table ) );
        list.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
        list.setFixedCellHeight( table.getRowHeight() );
        list.setFixedCellWidth( 15 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Component getView()
    {
        return list;
    }

    /***************************************************************************
     * @param height
     **************************************************************************/
    public void setRowHeight( int height )
    {
        list.setFixedCellHeight( height );
    }

    /***************************************************************************
     * @param model
     **************************************************************************/
    public void setModel( IRowHeaderModel model )
    {
        this.model.setModel( model );

        updateView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IRowListModel getListModel()
    {
        return model;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateView()
    {
        list.setFixedCellWidth( -1 );
        list.setFixedCellWidth( list.getPreferredSize().width + 16 );
        list.repaint();
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static interface IRowHeaderModel
    {
        /**
         * @param row
         * @return
         */
        public String getHeader( int row );

        /**
         * Returns the number of rows in this model.
         * @return
         */
        public int getCount();

        /**
         * @param callback
         */
        public void addUpdatedListener( Runnable callback );
    }

    public static interface IRowListModel
    {
        void fireRowsDeleted( int rowStart, int rowEnd );

        void fireRowsInserted( int rowStart, int rowEnd );

        void fireRowsUpdated( int rowStart, int rowEnd );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RowListModel extends AbstractListModel<String>
        implements IRowListModel
    {
        /**  */
        private static final long serialVersionUID = 404977197801943790L;

        /**  */
        private final RowHeaderView view;

        /**  */
        private IRowHeaderModel headerModel;

        /**
         * @param view
         */
        public RowListModel( RowHeaderView view )
        {
            this.view = view;
            this.headerModel = new PaginatedNumberRowHeaderModel();
        }

        /**
         * @param headerModel
         */
        public void setModel( IRowHeaderModel headerModel )
        {
            this.headerModel = headerModel;

            headerModel.addUpdatedListener( () -> view.updateView() );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getSize()
        {
            return headerModel.getCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getElementAt( int index )
        {
            return headerModel.getHeader( index );
        }

        @Override
        public void fireRowsDeleted( int rowStart, int rowEnd )
        {
            super.fireIntervalRemoved( this, rowStart, rowEnd );
        }

        @Override
        public void fireRowsInserted( int rowStart, int rowEnd )
        {
            super.fireIntervalAdded( this, rowStart, rowEnd );
        }

        @Override
        public void fireRowsUpdated( int rowStart, int rowEnd )
        {
            super.fireContentsChanged( this, rowStart, rowEnd );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class PaginatedNumberRowHeaderModel
        implements IRowHeaderModel
    {
        /**  */
        private long rowStart;
        /**  */
        private int rowCount;
        /**  */
        private final RunnableList callbacks;

        /**
         * 
         */
        public PaginatedNumberRowHeaderModel()
        {
            this.rowStart = 0L;
            this.rowCount = 0;
            this.callbacks = new RunnableList();
        }

        /**
         * @param start
         * @param count
         */
        public void setData( long start, int count )
        {
            this.rowStart = start;
            this.rowCount = count;
            callbacks.fireListeners();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getHeader( int row )
        {
            return Long.toString( rowStart + row );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount()
        {
            return rowCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addUpdatedListener( Runnable callback )
        {
            callbacks.addListener( callback );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IPaginatedHeaderModel
    {
        /**
         * @param index
         * @return
         */
        public String getHeader( long index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class PaginatedRowHeaderModel implements IRowHeaderModel
    {
        /**  */
        private final IPaginatedHeaderModel model;

        /**  */
        private long rowStart;
        /**  */
        private int rowCount;
        /**  */
        private final RunnableList callbacks;

        /**
         * @param model
         */
        public PaginatedRowHeaderModel( IPaginatedHeaderModel model )
        {
            this.model = model;
            this.rowStart = 0;
            this.rowCount = 0;
            this.callbacks = new RunnableList();
        }

        /**
         * @param start
         * @param count
         */
        public void setData( long start, int count )
        {
            this.rowStart = start;
            this.rowCount = count;
            callbacks.fireListeners();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getHeader( int row )
        {
            return model.getHeader( rowStart + row );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount()
        {
            return rowCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addUpdatedListener( Runnable callback )
        {
            this.callbacks.addListener( callback );
        }
    }
}
