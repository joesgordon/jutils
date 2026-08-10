package jutils.multicon.ui;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import jutils.core.ui.ComponentView;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.event.updater.UpdaterList;
import jutils.core.ui.model.IView;
import jutils.multicon.data.LinkType;

/*******************************************************************************
 * Defines the main view for Multicon.
 ******************************************************************************/
public class MulticonView implements IView<JComponent>
{
    /** The tabs in the content view. */
    private final JSplitPane split;
    /**  */
    private final MultiConnectionList list;
    /**  */
    private final ComponentView connectionView;
    /**  */
    private final UpdaterList<ILinkView> selectedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonView()
    {
        this.split = new JSplitPane();
        this.list = new MultiConnectionList();
        this.connectionView = new ComponentView();
        this.selectedListeners = new UpdaterList<>();

        list.addItemSelectedListener( ( d ) -> handleLinkSelected( d ) );

        split.setLeftComponent( list.getView() );
        split.setRightComponent( connectionView.getView() );

        split.setDividerLocation( 200 );
        split.setOneTouchExpandable( false );
        split.setResizeWeight( 0.0 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return split;
    }

    /***************************************************************************
     * @param onSelected
     **************************************************************************/
    public void addItemSelectedListener( IUpdater<ILinkView> onSelected )
    {
        selectedListeners.add( onSelected );
    }

    /***************************************************************************
     * @param type
     **************************************************************************/
    public void addConnection( LinkType type )
    {
        list.addConnection( type );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void closeAll()
    {
        list.closeAll();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean bind()
    {
        return list.bind();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean unbind()
    {
        return list.unbind();
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void handleLinkSelected( ILinkView view )
    {
        if( view != null )
        {
            connectionView.setComponent( view.getView() );
        }

        selectedListeners.fire( view );
    }
}
