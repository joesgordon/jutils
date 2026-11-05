package jutils.plot.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;

import jutils.core.ui.ItemsListView;
import jutils.core.ui.ItemsListView.IListViewModel;
import jutils.core.ui.model.IDataView;
import jutils.plot.model.Series;

/***************************************************************************
 * 
 **************************************************************************/
public class PlotsPropertiesView implements IDataView<List<Series>>
{
    /**  */
    private final ItemsListView<Series> listView;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlotsPropertiesView()
    {
        this.listView = new ItemsListView<Series>( new SeriesItemListModel() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return new JPanel();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Series> getData()
    {
        return listView.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<Series> data )
    {
        listView.setData( data );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SeriesItemListModel implements IListViewModel<Series>
    {
        @Override
        public String getName( Series item )
        {
            return item.name;
        }

        @Override
        public Series promptForNew( ItemsListView<Series> view )
        {
            return null;
        }
    }
}
