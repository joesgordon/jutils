package org.jutils.apps;

import java.awt.TrayIcon;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jutils.apps.ui.AppsFrameView;
import org.jutils.core.SwingUtils;
import org.jutils.core.ui.ExitListener;
import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * Defines the set of functions that displays a frame application.
 ******************************************************************************/
public class JUtilsApp implements IFrameApp
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        List<IToolView> apps = JUtilsMain.getTools();
        AppsFrameView frameView = new AppsFrameView( apps );
        JFrame frame = frameView.getView();

        TrayIcon icon = SwingUtils.createTrayIcon(
            JUtilsIcons.getImage( JUtilsIcons.APP_16 ), "JUtils Apps",
            frame, null );

        SwingUtils.addTrayMenu( icon, createPopup( frameView ) );

        return frame;
    }

    /***************************************************************************
     * Creates the popup menu for the tray icon that displays all the apps in
     * the gallery.
     * @param frame the frame to use as the parent of the popup menu.
     * @return the popup menu for this application's tray icon.
     **************************************************************************/
    private static JPopupMenu createPopup( AppsFrameView frame )
    {
        JPopupMenu menu = new JPopupMenu();

        menu.add( frame.createMenu() );

        menu.addSeparator();

        JMenuItem menuItem = new JMenuItem( "Exit" );
        menuItem.addActionListener( new ExitListener( frame.getView() ) );
        menu.add( menuItem );

        return menu;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
