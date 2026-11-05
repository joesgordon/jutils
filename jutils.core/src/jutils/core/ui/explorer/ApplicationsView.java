package jutils.core.ui.explorer;

import java.awt.Component;
import java.util.List;

import jutils.core.ui.ItemsListDataView;
import jutils.core.ui.ItemsListView;
import jutils.core.ui.ItemsListView.IListViewModel;
import jutils.core.ui.explorer.data.ApplicationConfig;
import jutils.core.ui.model.IDataView;

public class ApplicationsView implements IDataView<List<ApplicationConfig>>
{
    /**  */
    private final ItemsListDataView<ApplicationConfig> appsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ApplicationsView()
    {
        this.appsView = new ItemsListDataView<>( new ApplicationView(),
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
        implements IListViewModel<ApplicationConfig>
    {
        @Override
        public String getName( ApplicationConfig item )
        {
            return item.name;
        }

        @Override
        public ApplicationConfig promptForNew(
            ItemsListView<ApplicationConfig> view )
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
