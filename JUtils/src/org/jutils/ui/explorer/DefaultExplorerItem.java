package org.jutils.ui.explorer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

public class DefaultExplorerItem implements IExplorerItem
{
    /**  */
    public static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();

    /**  */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm" );

    /**  */
    private final File file;
    /**  */
    private final String lastModified;
    /**  */
    private final String parentPath;
    /**  */
    private final long sizeKb;
    /**  */
    private final String type;
    /**  */
    private final String systemName;

    /***************************************************************************
     * @param file
     **************************************************************************/
    public DefaultExplorerItem( File file )
    {
        File parent = file.getParentFile();

        this.file = file;
        this.lastModified = DATE_FORMAT.format(
            new Date( file.lastModified() ) );
        this.parentPath = parent == null ? "" : parent.getAbsolutePath();
        this.sizeKb = getSizeInKb( file );
        this.type = getType( file, getExtension() );
        this.systemName = FILE_SYSTEM_VIEW.getSystemDisplayName( file );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFilename()
    {
        return file.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getLastModified()
    {
        return lastModified;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getParentPath()
    {
        return parentPath;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getSizeInKb()
    {
        return sizeKb;
    }

    /**
     * @param file
     * @return
     */
    public static long getSizeInKb( File file )
    {
        long fileLen = file.length();
        long len = ( fileLen + 512 ) / 1024;

        len = fileLen > 0 && len == 0 ? 1 : len;

        return file.isDirectory() ? -1 : len;

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getType()
    {
        return type;
    }

    public static String getType( File file, String ext )
    {
        String desc = FILE_SYSTEM_VIEW.getSystemTypeDescription( file );

        if( desc == null )
        {
            if( file.isFile() )
            {
                desc = ext;
                desc = desc.toUpperCase();
                if( desc.length() > 0 )
                {
                    desc += " ";
                }
                desc += "File";
            }
            else
            {
                desc = "Folder";
            }
        }

        return desc;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getExtension()
    {
        String name = file.getName();
        int idx = name.lastIndexOf( '.' );

        if( idx > -1 && idx + 1 < name.length() )
        {
            name = name.substring( idx + 1 );
        }
        else
        {
            name = "";
        }

        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getSystemName()
    {
        return systemName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getFile()
    {
        return file;
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    @Override
    public String toString()
    {
        return getSystemName();
    }
}
