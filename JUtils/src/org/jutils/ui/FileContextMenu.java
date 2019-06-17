package org.jutils.ui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.IOUtils;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.utils.IGetter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileContextMenu
{
    /**  */
    private final Component parent;
    /**  */
    private final FileIcon icon;
    /**  */
    private final JPopupMenu menu;
    /**  */
    private final JMenuItem openPathMenuItem;
    /**  */
    private final JMenuItem openParentMenuItem;
    /**  */
    private final JMenuItem copyPathMenuItem;
    /**  */
    private final JMenuItem copyNameMenuItem;
    /**  */
    private final JMenuItem copyParentPathMenuItem;
    /**  */
    private final JMenuItem copyParentNameMenuItem;
    /**  */
    private final JMenuItem propertiesMenuItem;

    /**  */
    private File file;

    // TODO Add Delete
    // TODO Add Copy To...
    // TODO Add Move To...
    // TODO Add Open With

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public FileContextMenu( Component parent )
    {
        this.parent = parent;
        this.icon = new FileIcon();
        this.openPathMenuItem = new JMenuItem();
        this.openParentMenuItem = new JMenuItem();
        this.copyPathMenuItem = new JMenuItem();
        this.copyNameMenuItem = new JMenuItem();
        this.copyParentPathMenuItem = new JMenuItem();
        this.copyParentNameMenuItem = new JMenuItem();
        this.propertiesMenuItem = new JMenuItem();
        this.menu = createMenu();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        ActionListener l;
        Action a;

        l = ( e ) -> openPath();
        a = createAction( l, "Open Path", IconConstants.OPEN_FILE_16 );
        openPathMenuItem.setAction( a );
        menu.add( openPathMenuItem );

        l = ( e ) -> openParent();
        a = createAction( l, "Open Parent", IconConstants.OPEN_FOLDER_16 );
        openParentMenuItem.setAction( a );
        menu.add( openParentMenuItem );

        menu.addSeparator();

        l = ( e ) -> copyString( () -> file.getAbsolutePath() );
        a = createAction( l, "Copy Path", IconConstants.EDIT_COPY_16 );
        copyPathMenuItem.setAction( a );
        menu.add( copyPathMenuItem );

        l = ( e ) -> copyString( () -> file.getName() );
        a = createAction( l, "Copy Name", IconConstants.EDIT_COPY_16 );
        copyNameMenuItem.setAction( a );
        menu.add( copyNameMenuItem );

        menu.addSeparator();

        l = ( e ) -> copyString( () -> file.getParent() );
        a = createAction( l, "Copy Parent Path", IconConstants.EDIT_COPY_16 );
        copyParentPathMenuItem.setAction( a );
        menu.add( copyParentPathMenuItem );

        l = ( e ) -> copyString( () -> file.getParentFile().getName() );
        a = createAction( l, "Copy Parent Name", IconConstants.EDIT_COPY_16 );
        copyParentNameMenuItem.setAction( a );
        menu.add( copyParentNameMenuItem );

        menu.addSeparator();

        l = ( e ) -> showProperties();
        a = createAction( l, "Properties", IconConstants.CONFIG_16 );
        propertiesMenuItem.setAction( a );
        menu.add( propertiesMenuItem );

        return menu;
    }

    /***************************************************************************
     * @param file
     * @param y
     * @param x
     * @param c
     * @see JPopupMenu#show(Component, int, int)
     **************************************************************************/
    public void show( File file, Component c, int x, int y )
    {
        this.file = file;

        File parent = file == null ? null : file.getParentFile();

        icon.setFile( file );

        openPathMenuItem.setIcon( icon );

        openPathMenuItem.setEnabled( file != null && file.exists() );
        openParentMenuItem.setEnabled(
            file != null && parent != null && parent.exists() );

        copyPathMenuItem.setEnabled( file != null );
        copyNameMenuItem.setEnabled( file != null );

        copyParentPathMenuItem.setEnabled( file != null );
        copyParentNameMenuItem.setEnabled( file != null && parent != null );

        propertiesMenuItem.setEnabled( file != null );

        menu.show( c, x, y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void copyString( IGetter<String> strGetter )
    {
        String str = strGetter.get();
        if( str != null )
        {
            Utils.setClipboardText( str );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showProperties()
    {
        FilePropertiesView propView = new FilePropertiesView();

        propView.setData( file );
        propView.show( parent );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void openPath()
    {
        if( file != null )
        {
            openPath( parent, file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void openParent()
    {
        if( file != null )
        {
            openPath( parent, file.getParentFile() );
        }
    }

    /***************************************************************************
     * @param parentComp
     * @param file
     **************************************************************************/
    public static void openPath( Component parentComp, File file )
    {
        if( !file.exists() )
        {
            String [] choices = new String[] { "Open Parent", "Cancel" };
            String choice = SwingUtils.showConfirmMessage( parentComp,
                "File does not exist. Open existing parent?",
                "File does not exist", choices, choices[0] );

            if( choices[0].equals( choice ) )
            {
                File parent = IOUtils.getExistingDir( file.getAbsolutePath() );

                if( parent == null )
                {
                    SwingUtils.showErrorMessage(
                        parentComp, "No parent exists for file:" +
                            Utils.NEW_LINE + file.getAbsolutePath(),
                        "Error Opening File" );
                    return;
                }

                file = parent;
            }
            else
            {
                return;
            }
        }

        if( Desktop.isDesktopSupported() )
        {
            try
            {
                Desktop.getDesktop().open( file );
            }
            catch( IOException ex )
            {
                SwingUtils.showErrorMessage(
                    parentComp, "Could not open file externally:" +
                        Utils.NEW_LINE + file.getAbsolutePath(),
                    "Error Opening File" );
            }
        }
        else
        {
            SwingUtils.showErrorMessage( parentComp,
                "Unable to open files on this platform.",
                "Error Opening File" );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Action createAction( ActionListener l, String name,
        String iconStr )
    {
        Icon icon = IconConstants.getIcon( iconStr );

        return new ActionAdapter( l, name, icon );
    }
}
