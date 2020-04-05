package org.jutils.explorer;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.core.IconConstants;

public class ExplorerIcons
{
    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private ExplorerIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return IconConstants.getImages( IconConstants.OPEN_FOLDER_16,
            IconConstants.OPEN_FOLDER_24, IconConstants.OPEN_FOLDER_32 );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static Icon getApp24Icon()
    {
        return IconConstants.getIcon( IconConstants.OPEN_FOLDER_24 );
    }
}
