package org.jutils.ui.explorer;

import java.awt.Component;
import java.util.List;

import org.jutils.ui.ItemListView;
import org.jutils.ui.ListView;
import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.explorer.data.ApplicationConfig;
import org.jutils.ui.model.IDataView;

public class ApplicationsView implements IDataView<List<ApplicationConfig>>
{
    /**  */
    private final ItemListView<ApplicationConfig> appsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ApplicationsView()
    {
        this.appsView = new ItemListView<>( new ApplicationView(),
            new ExtensionItemListModel() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return appsView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<ApplicationConfig> getData()
    {
        return appsView.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<ApplicationConfig> data )
    {
        appsView.setData( data );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        appsView.setEnabled( enabled );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ExtensionItemListModel
        implements IItemListModel<ApplicationConfig>
    {
        @Override
        public String getTitle( ApplicationConfig item )
        {
            return item.name;
        }

        @Override
        public ApplicationConfig promptForNew(
            ListView<ApplicationConfig> view )
        {
            ApplicationConfig item = null;
            String name = view.promptForName( "extension" );

            if( name != null )
            {
                item = new ApplicationConfig();
                item.name = name;
            }

            return item;
        }
    }
}
