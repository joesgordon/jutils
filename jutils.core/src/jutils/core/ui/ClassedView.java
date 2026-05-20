package jutils.core.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import jutils.core.ui.model.IDataView;
import jutils.core.utils.Tuple2;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ClassedView<T> implements IDataView<T>
{
    /**  */
    private final ComponentView view;
    /**  */
    private final JScrollPane scrollpane;
    /**  */
    private final Map<Class<?>,
        Tuple2<Boolean, IClassedView<? extends T>>> dataViews;

    /**  */
    private T data;

    /***************************************************************************
     * 
     **************************************************************************/
    public ClassedView()
    {
        this.view = new ComponentView();
        this.scrollpane = new JScrollPane();
        this.dataViews = new HashMap<>();

        scrollpane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
    }

    /***************************************************************************
     * @param <D>
     * @param clz
     * @param dataView
     * @param needsScrollPane
     **************************************************************************/
    public <D extends T> void put( Class<D> clz, IClassedView<D> dataView,
        boolean needsScrollPane )
    {
        this.dataViews.put( clz, new Tuple2<>( needsScrollPane, dataView ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T getData()
    {
        return this.data;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( T data )
    {
        this.data = data;

        Tuple2<Boolean, IClassedView<
            ? extends T>> viewInfo = dataViews.get( data.getClass() );
        @SuppressWarnings( "unchecked")
        IDataView<T> dataView = ( IDataView<T> )viewInfo.second;

        dataView.setData( data );

        if( viewInfo.first )
        {
            scrollpane.setViewportView( dataView.getView() );
            view.setComponent( scrollpane );
        }
        else
        {
            view.setComponent( dataView.getView() );
        }
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        for( Tuple2<Boolean,
            IClassedView<? extends T>> viewInfo : dataViews.values() )
        {
            viewInfo.second.setEditable( editable );
        }
    }

    /***************************************************************************
     * @param <D>
     **************************************************************************/
    public static interface IClassedView<D> extends IDataView<D>
    {
        /**
         * @param editable
         * @return
         */
        public void setEditable( boolean editable );
    }
}
