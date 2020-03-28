package org.jutils.apps.filespy.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jutils.core.ui.explorer.DefaultExplorerItem;
import org.jutils.core.ui.explorer.IExplorerItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SearchRecord implements IExplorerItem
{
    /**  */
    private ArrayList<LineMatch> linesFound;

    private final DefaultExplorerItem item;

    /***************************************************************************
     * @param file
     **************************************************************************/
    public SearchRecord( File file )
    {
        this.item = new DefaultExplorerItem( file );
        this.linesFound = new ArrayList<>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getFile()
    {
        return item.getFile();
    }

    /***************************************************************************
     * @param line
     **************************************************************************/
    public void addLine( LineMatch line )
    {
        linesFound.add( line );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<LineMatch> getLinesFound()
    {
        return new ArrayList<LineMatch>( linesFound );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFilename()
    {
        return item.getFilename();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getLastModified()
    {
        return item.getLastModified();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getParentPath()
    {
        return item.getParentPath();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getSizeInKb()
    {
        return item.getSizeInKb();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getType()
    {
        return item.getType();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getSystemName()
    {
        return item.getSystemName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return getSystemName();
    }
}
