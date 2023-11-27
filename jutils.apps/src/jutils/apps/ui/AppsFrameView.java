package jutils.apps.ui;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jutils.core.IconConstants;
import jutils.core.JUtilsMain;
import jutils.core.data.BuildInfo;
import jutils.core.ui.BuildInfoView;
import jutils.core.ui.IToolView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.ToolsView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines the window for this application that displays tools in a gallery.
 ******************************************************************************/
public class AppsFrameView implements IView<JFrame>
{
    /** The window for this application. */
    private final StandardFrameView frameView;
    /** The view that displays the tools. */
    private final ToolsView view;

    /***************************************************************************
     * Creates a new app gallery window that displays the provided tools.
     * @param tools the tools to be displayed in the gallery.
     **************************************************************************/
    public AppsFrameView( List<IToolView> tools )
    {
        this.frameView = new StandardFrameView();
        this.view = new ToolsView( tools, "JUtils" );

        createMenubar( frameView.getMenuBar() );
        frameView.setContent( view.getView() );
        frameView.setTitle( "JUtils Apps" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );

        frameView.getView().setIconImages(
            IconConstants.getAllImages( "jutils" ) );
    }

    /***************************************************************************
     * Fills the menu bar with this applications options.
     * @param menuBar the menu bar for this application
     **************************************************************************/
    private void createMenubar( JMenuBar menuBar )
    {
        // menuBar.add( Box.createHorizontalGlue() );

        menuBar.add( createHelpMenu() );
    }

    /***************************************************************************
     * Creates the help menu.
     * @return a menu that has all the "Help" options.
     **************************************************************************/
    private JMenu createHelpMenu()
    {
        JMenu menu = new JMenu( "Help" );
        JMenuItem item;

        menu.setMnemonic( 'H' );

        item = new JMenuItem( "About" );
        item.setMnemonic( 'A' );
        item.addActionListener( ( e ) -> showAbout() );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * Shows the about dialog in a modal dialog.
     **************************************************************************/
    private void showAbout()
    {
        BuildInfo info = JUtilsMain.load();
        BuildInfoView.show( getView(), "About Duak", info );
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
     * Creates a menu for the tools displayed.
     * @return a menu containing the tools displayed.
     * @see ToolsView#createMenu()
     **************************************************************************/
    public JMenuItem createMenu()
    {
        return view.createMenu();
    }
}
