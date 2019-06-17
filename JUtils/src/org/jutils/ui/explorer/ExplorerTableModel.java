package org.jutils.ui.explorer;

import java.io.File;
import java.util.*;

import javax.swing.table.AbstractTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExplorerTableModel extends AbstractTableModel
{
    /**  */
    private static final long serialVersionUID = -8775953721896711927L;

    /**  */
    public static final String [] STD_HEADERS = new String[] { "Name",
        "Location", "Size (kB)", "Type", "Modified" };
    /**  */
    public static final Class<?> [] STD_CLASSES = new Class<?>[] {
        IExplorerItem.class, File.class, Long.class, String.class,
        String.class };

    /**  */
    private final List<String> headers;
    /**  */
    private final List<Class<?>> classes;
    /**  */
    private final int [] itemIndexes;
    /**  */
    private final List<IExplorerItem> contents;

    /***************************************************************************
     * 
     **************************************************************************/
    public ExplorerTableModel()
    {
        this( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ExplorerTableModel( boolean showPath )
    {
        super();

        this.headers = new ArrayList<>( Arrays.asList( STD_HEADERS ) );
        this.classes = new ArrayList<>( Arrays.asList( STD_CLASSES ) );
        this.itemIndexes = new int[] { 0, 1, 2, 3, 4 };
        this.contents = new ArrayList<IExplorerItem>( 1024 );

        if( !showPath )
        {
            headers.remove( 1 );
            classes.remove( 1 );
            itemIndexes[1] = 2;
            itemIndexes[2] = 3;
            itemIndexes[3] = 4;
            itemIndexes[4] = -1;
        }
    }

    /***************************************************************************
     * @param row int
     * @param col int
     * @return boolean
     **************************************************************************/
    @Override
    public boolean isCellEditable( int row, int col )
    {
        return false;
    }

    /***************************************************************************
     * @return int
     **************************************************************************/
    @Override
    public int getColumnCount()
    {
        return headers.size();
    }

    /***************************************************************************
     * @return int
     **************************************************************************/
    @Override
    public int getRowCount()
    {
        return contents.size();
    }

    /***************************************************************************
     * @param col int
     * @return String
     **************************************************************************/
    @Override
    public String getColumnName( int col )
    {
        String name = null;

        if( col > -1 && col < headers.size() )
        {
            name = headers.get( col );
        }

        return name;
    }

    @Override
    public Class<?> getColumnClass( int col )
    {
        Class<?> name = null;

        if( col > -1 && col < classes.size() )
        {
            name = classes.get( col );
        }

        return name;
    }

    /***************************************************************************
     * @param rowIndex
     * @return
     **************************************************************************/
    public IExplorerItem getExplorerItem( int rowIndex )
    {
        IExplorerItem item = null;

        if( rowIndex > -1 && rowIndex < contents.size() )
        {
            item = contents.get( rowIndex );
        }

        return item;
    }

    /***************************************************************************
     * @param row int
     * @param col int
     * @return Object
     **************************************************************************/
    @Override
    public Object getValueAt( int row, int col )
    {
        IExplorerItem item = contents.get( row );
        Object value = null;

        int itemKey = itemIndexes[col];

        switch( itemKey )
        {
            case 0:
            {
                value = item;
                break;
            }
            case 1:
            {
                value = item.getFile().getParentFile();
                break;
            }
            case 2:
            {
                value = item.getSizeInKb();
                break;
            }
            case 3:
            {
                value = "  " + item.getType() + "  ";
                break;
            }
            case 4:
            {
                value = "  " + item.getLastModified() + "  ";
                break;
            }
            default:
            {
                break;
            }
        }

        return value;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isShowingPath()
    {
        return headers.size() == STD_HEADERS.length;
    }

    /***************************************************************************
     * @param files List
     **************************************************************************/
    public void addFiles( List<? extends IExplorerItem> files )
    {
        int start = contents.size();
        int end = 0;
        contents.addAll( files );
        end = contents.size();
        if( end > start )
        {
            fireTableRowsInserted( start, end - 1 );
        }
    }

    /***************************************************************************
     * @param file File
     **************************************************************************/
    public void addFile( File file )
    {
        insertFile( contents.size(), file );
    }

    /***************************************************************************
     * @param row integer
     * @param file File
     **************************************************************************/
    public void insertFile( int row, File file )
    {
        DefaultExplorerItem item = new DefaultExplorerItem( file );
        insertFile( row, item );
    }

    /***************************************************************************
     * @param file File
     **************************************************************************/
    public void addFile( IExplorerItem item )
    {
        insertFile( contents.size(), item );
    }

    /***************************************************************************
     * @param row integer
     * @param file File
     **************************************************************************/
    public void insertFile( int row, IExplorerItem item )
    {
        contents.add( row, item );
        fireTableRowsInserted( row, row );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearModel()
    {
        int size = contents.size();
        contents.clear();
        if( size > 0 )
        {
            this.fireTableRowsDeleted( 0, size - 1 );
        }
    }
}
