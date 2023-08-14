package org.jutils.apps.ui;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.jutils.apps.JUtilsIcons;
import org.jutils.core.JUtilsInfo;
import org.jutils.core.data.BuildInfo;
import org.jutils.core.ui.BuildInfoView;
import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.ToolsView;
import org.jutils.core.ui.model.IView;

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

        frameView.getView().setIconImages( JUtilsIcons.getAppImages() );
    }

    /***************************************************************************
     * @param menuBar
     **************************************************************************/
    private void createMenubar( JMenuBar menuBar )
    {
        // menuBar.add( Box.createHorizontalGlue() );

        menuBar.add( createHelpMenu() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createHelpMenu()
    {
        JMenu menu = new JMenu( "Help" );
        JMenuItem item;

        item = new JMenuItem( "About" );
        item.addActionListener( ( e ) -> showAbout() );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showAbout()
    {
        BuildInfo info = JUtilsInfo.load();
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
