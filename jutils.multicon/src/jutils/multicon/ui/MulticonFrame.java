package jutils.multicon.ui;

import javax.swing.*;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.*;
import jutils.core.ui.event.WindowCloseListener;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.ItemsListModel;
import jutils.multicon.MulticonIcons;
import jutils.multicon.data.LinkType;

/*******************************************************************************
 * Defines the main window for Multicon.
 ******************************************************************************/
public class MulticonFrame implements IView<JFrame>
{
    /** The frame view. */
    private final StandardFrameView frameView;
    /**  */
    private final SplitButtonView<LinkType> newButton;
    /**  */
    private final ABButton bindButton;
    /** The tabs in the content view. */
    private final MulticonView view;

    /***************************************************************************
     * Creates the main window for Multicon.
     **************************************************************************/
    public MulticonFrame()
    {
        this.frameView = new StandardFrameView();
        this.newButton = new SplitButtonView<>( "New",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ),
            LinkType.getSortedTypes(), new ConnectionTypeModel() );
        this.bindButton = new ABButton( "Bind",
            MulticonIcons.getIcon( MulticonIcons.MULTICON_016 ),
            () -> handleBind(), "Unbind",
            IconConstants.getIcon( IconConstants.STOP_16 ),
            () -> handleUnbind() );
        this.view = new MulticonView();

        frameView.setTitle( "Multicon" );
        frameView.setSize( 800, 800 );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setToolbar( createToolbar() );
        frameView.setContent( view.getView() );

        frameView.getView().setIconImages( MulticonIcons.getMulticonImages() );
        frameView.getView().addWindowListener(
            new WindowCloseListener( () -> handleFrameClose() ) );

        bindButton.getView().setEnabled( false );
        view.addItemSelectedListener( ( d ) -> handleLinkSelected( d ) );
    }

    /***************************************************************************
     * Creates the toolbar for the window.
     * @return a new toolbar for the window.
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        newButton.install( toolbar );

        newButton.addItemSelected( ( t, c ) -> handleNewConnection( t ) );
        newButton.addButtonListener( ( e ) -> createNewConnection() );

        toolbar.add( bindButton.getView() );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleFrameClose()
    {
        view.closeAll();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean handleBind()
    {
        return view.bind();
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    private void handleLinkSelected( ILinkView view )
    {
        boolean isSelected = view != null;

        bindButton.getView().setEnabled( isSelected );
        if( isSelected )
        {
            bindButton.setState( !view.getLink().isConnected() );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean handleUnbind()
    {
        return view.unbind();
    }

    /***************************************************************************
     * @param conType
     **************************************************************************/
    private void handleNewConnection( LinkType conType )
    {
        view.addConnection( conType );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createNewConnection()
    {
        NewConnectionView newView = new NewConnectionView();
        OkDialogView dialogView = new OkDialogView( getView(),
            newView.getView() );

        dialogView.setTitle( "New Connection" );

        if( dialogView.show( 800, 600 ) )
        {
            // TODO add the link
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ConnectionTypeModel
        implements ItemsListModel<LinkType>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getName( LinkType item )
        {
            return item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTooltip( LinkType item )
        {
            return "Create a new " + item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Icon getIcon( LinkType item )
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
