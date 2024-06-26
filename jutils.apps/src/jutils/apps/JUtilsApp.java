package jutils.apps;

import java.awt.Image;
import java.awt.TrayIcon;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jutils.apps.ui.AppsFrameView;
import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.ExitListener;
import jutils.core.ui.IToolView;
import jutils.core.ui.app.AppRunner.IFrameCreator;

/*******************************************************************************
 * Defines the set of functions that displays the JUtils frame application.
 ******************************************************************************/
public class JUtilsApp implements IFrameCreator
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

        Image image = IconConstants.getImage( IconConstants.JUTILS_16 );
        TrayIcon trayicon = SwingUtils.createTrayIcon( image, "JUtils Apps",
            frame, null );

        SwingUtils.addTrayMenu( trayicon, createPopup( frameView ) );

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
}
