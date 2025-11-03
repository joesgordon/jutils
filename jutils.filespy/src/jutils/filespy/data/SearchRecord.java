package jutils.filespy.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import jutils.core.SwingUtils;
import jutils.core.ui.explorer.DefaultExplorerItem;
import jutils.core.ui.explorer.IExplorerItem;

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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getFilename()
    {
        return item.getFilename();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getLastModified()
    {
        return item.getLastModified();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getParentPath()
    {
        return item.getParentPath();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getSizeInKb()
    {
        return item.getSizeInKb();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getType()
    {
        return item.getType();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getSystemName()
    {
        return item.getSystemName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return getSystemName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon( FileSystemView view )
    {
        return SwingUtils.getFileIcon( view, getFile() );
    }
}
