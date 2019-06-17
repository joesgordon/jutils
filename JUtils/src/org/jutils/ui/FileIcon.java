package org.jutils.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.jutils.IconConstants;

/*******************************************************************************
 * Defines an icon for files that uses the icon from a {@link FileSystemView} or
 * {@link IconConstants#CHECK_16}, {@link IconConstants#INVALID_16},
 * {@link IconConstants#OPEN_FILE_16}, or {@link IconConstants#OPEN_FOLDER_16}.
 ******************************************************************************/
public class FileIcon implements Icon
{
    /** The view to use to get icons for files/folders that exist. */
    public final FileSystemView fileSys;
    /** The icon used to indicate a problem. */
    private final Icon errorIcon;
    /** The icon used to indicate no existence but also no problem. */
    private final Icon checkIcon;
    /** The icon used for files with no icon in the view. */
    private final Icon fileIcon;
    /** The icon used for directories with no icon in the view. */
    private final Icon dirIcon;

    /** The icon currently being displayed. Never {@code null} by convention. */
    private Icon icon;

    /***************************************************************************
     * Creates a new file icon. By default the error icon is shown.
     **************************************************************************/
    public FileIcon()
    {
        this.fileSys = FileSystemView.getFileSystemView();
        this.errorIcon = IconConstants.getIcon( IconConstants.INVALID_16 );
        this.checkIcon = IconConstants.getIcon( IconConstants.CHECK_16 );
        this.fileIcon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
        this.dirIcon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

        this.icon = errorIcon;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        icon.paintIcon( c, g, x, y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIconWidth()
    {
        return icon.getIconWidth();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIconHeight()
    {
        return icon.getIconHeight();
    }

    /***************************************************************************
     * Sets the file for this icon to represent.
     * @param file the file path this icon represents.
     **************************************************************************/
    public void setFile( File file )
    {
        if( file == null )
        {
            icon = errorIcon;
        }
        else if( file.isDirectory() )
        {
            icon = dirIcon;
        }
        else if( file.isFile() )
        {
            icon = fileSys.getSystemIcon( file );

            if( icon == null )
            {
                icon = fileIcon;
            }
        }
    }

    /***************************************************************************
     * Sets the icon as an error icon. Useful for relating a problem with a
     * path.
     **************************************************************************/
    public void setErrorIcon()
    {
        this.icon = errorIcon;
    }

    /***************************************************************************
     * Sets the icon as a check icon. Useful for relating this no problem
     * exists, but no icon does either (e.g. when saving a new file).
     **************************************************************************/
    public void setCheckIcon()
    {
        this.icon = checkIcon;
    }
}
