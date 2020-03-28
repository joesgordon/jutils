package org.jutils.core.ui.explorer;

import java.awt.Component;
import java.util.List;

import org.jutils.core.ui.ItemListView;
import org.jutils.core.ui.ListView;
import org.jutils.core.ui.ListView.IItemListModel;
import org.jutils.core.ui.explorer.data.ExtensionConfig;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExtensionsView implements IDataView<List<ExtensionConfig>>
{
    /**  */
    private final ItemListView<ExtensionConfig> extsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtensionsView()
    {
        this.extsView = new ItemListView<>( new ExtensionView(),
            new ExtensionItemListModel() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return extsView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<ExtensionConfig> getData()
    {
        return extsView.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<ExtensionConfig> data )
    {
        extsView.setData( data );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        extsView.setEnabled( enabled );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ExtensionItemListModel
        implements IItemListModel<ExtensionConfig>
    {
        @Override
        public String getTitle( ExtensionConfig item )
        {
            return item.ext;
        }

        @Override
        public ExtensionConfig promptForNew( ListView<ExtensionConfig> view )
        {
            ExtensionConfig item = null;
            String ext = view.promptForName( "extension" );

            if( ext != null )
            {
                item = new ExtensionConfig();
                item.ext = ext;
            }

            return item;
        }
    }
}
