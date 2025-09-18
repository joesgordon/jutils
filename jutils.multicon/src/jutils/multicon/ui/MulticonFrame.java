package jutils.multicon.ui;

import javax.swing.*;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;
import jutils.multicon.MulticonIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final JTabbedPane tabs;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonFrame()
    {
        this.frameView = new StandardFrameView();
        this.tabs = new JTabbedPane();

        frameView.setTitle( "Multicon" );
        frameView.setSize( 800, 800 );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setToolbar( createToolbar() );
        frameView.setContent( tabs );

        frameView.getView().setIconImages( MulticonIcons.getMulticonImages() );

        tabs.addTab( "Connections", new JPanel() );
        tabs.addTab( "Metrics", new JPanel() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, ( e ) -> createNewConnection(),
            "New Connection",
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );

        return toolbar;
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
}
