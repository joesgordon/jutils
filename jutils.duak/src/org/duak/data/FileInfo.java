package org.duak.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jutils.core.task.ITaskStatusHandler;

public class FileInfo implements Comparable<FileInfo>
{
    private FileInfo parent;
    private File dir;
    private boolean isDir;
    private long size;
    private List<FileInfo> children;

    public FileInfo( File file )
    {
        this( null, file );
    }

    public FileInfo( FileInfo parent, File file )
    {
        this.parent = parent;
        dir = file;
        isDir = file.isDirectory();
        size = 0;
        children = new ArrayList<FileInfo>();
    }

    public FileInfo getParent()
    {
        return parent;
    }

    public File getFile()
    {
        return dir;
    }

    public long getSize()
    {
        return size;
    }

    public boolean isDir()
    {
        return isDir;
    }

    public List<FileInfo> getChildren()
    {
        return children;
    }

    public void setSize( long size )
    {
        this.size = size;
    }

    public void refresh( ITaskStatusHandler handler )
    {
        if( dir.isDirectory() && handler.canContinue() )
        {
            File [] fs = dir.listFiles();

            children.clear();

            if( fs == null )
            {
                return;
            }

            for( File f : fs )
            {
                FileInfo fr = new FileInfo( f );
                if( f.isDirectory() )
                {
                    fr.refresh( handler );
                }
                children.add( fr );
            }
        }
    }

    public int getNumFiles()
    {
        int count = 0;

        for( FileInfo fr : children )
        {
            if( fr.isDir )
            {
                count += fr.getNumFiles();
            }
            else
            {
                count++;
            }
        }

        return count;
    }

    public int getNumDirs()
    {
        int count = 0;

        for( FileInfo fr : children )
        {
            if( fr.isDir )
            {
                count++;
                count += fr.getNumFiles();
            }
        }

        return count;
    }

    public List<FileInfo> getDirectories()
    {
        List<FileInfo> dirs = new ArrayList<FileInfo>();

        for( FileInfo fr : children )
        {
            if( fr.isDir )
            {
                dirs.add( fr );
            }
        }

        return dirs;
    }

    @Override
    public String toString()
    {
        return dir.getAbsolutePath();
    }

    @Override
    public int compareTo( FileInfo thatInfo )
    {
        if( isDir() && !thatInfo.isDir() )
        {
            return -1;
        }
        else if( !isDir() && thatInfo.isDir() )
        {
            return 1;
        }

        return toString().compareTo( thatInfo.toString() );
    }
}
