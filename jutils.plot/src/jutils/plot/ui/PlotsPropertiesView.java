package jutils.plot.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;

import jutils.core.ui.ListView;
import jutils.core.ui.ListView.IItemListModel;
import jutils.core.ui.model.IDataView;
import jutils.plot.model.Series;

/***************************************************************************
 * 
 **************************************************************************/
public class PlotsPropertiesView implements IDataView<List<Series>>
{
    /**  */
    private final ListView<Series> listView;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlotsPropertiesView()
    {
        this.listView = new ListView<Series>( new SeriesItemListModel() );
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
    private static class SeriesItemListModel implements IItemListModel<Series>
    {
        @Override
        public String getTitle( Series item )
        {
            return item.name;
        }

        @Override
        public Series promptForNew( ListView<Series> view )
        {
            return null;
        }
    }
}
