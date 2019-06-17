package org.jutils.io;

import java.io.File;
import java.util.Comparator;

import org.jutils.ui.explorer.DefaultExplorerItem;

/**
 *
 */
public class FileComparator implements Comparator<File>
{
    /**
     *
     */
    public FileComparator()
    {
    }

    /**
     * Compares its two arguments for order.
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare( File file1, File file2 )
    {
        if( file1.isDirectory() && file2.isFile() )
        {
            return -1;
        }
        else if( file1.isFile() && file2.isDirectory() )
        {
            return 1;
        }

        String name1 = DefaultExplorerItem.FILE_SYSTEM_VIEW.getSystemDisplayName(
            file1 );
        String name2 = DefaultExplorerItem.FILE_SYSTEM_VIEW.getSystemDisplayName(
            file2 );

        return name1.compareToIgnoreCase( name2 );
    }
}
