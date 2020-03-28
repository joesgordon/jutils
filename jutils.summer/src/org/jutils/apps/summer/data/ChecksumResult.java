package org.jutils.apps.summer.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.cksum.ChecksumType;

/*******************************************************************************
 *  
 *******************************************************************************/
public class ChecksumResult
{
    /**  */
    public final List<SumFile> files;
    /**  */
    public ChecksumType type;
    /**  */
    public File commonDir;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChecksumResult()
    {
        this.files = new ArrayList<>();
        this.type = ChecksumType.MD5;
    }

    /***************************************************************************
     * @param input
     **************************************************************************/
    public ChecksumResult( ChecksumResult input )
    {
        this();

        this.type = input.type;
        this.commonDir = input.commonDir;

        for( SumFile sf : input.files )
        {
            this.files.add( new SumFile( sf ) );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<File> buildFileList()
    {
        List<File> files = new ArrayList<>();

        for( SumFile path : this.files )
        {
            files.add( path.file );
        }

        return files;
    }

    /***************************************************************************
     * @param dir
     **************************************************************************/
    public void setPaths( File commonDir )
    {
        this.commonDir = commonDir;

        int relPathStart = 0;

        if( commonDir != null )
        {
            String path = IOUtils.getStandardAbsPath( commonDir );

            // LogUtils.printDebug( "Common dir: '" + path + "'" );

            relPathStart = path.length();
        }

        for( SumFile sf : files )
        {
            sf.path = IOUtils.getStandardAbsPath( sf.file ).substring(
                relPathStart );
        }
    }

    /***************************************************************************
     * @param commonDir
     **************************************************************************/
    public void setFiles( File commonDir )
    {
        this.commonDir = commonDir;

        if( commonDir != null )
        {
            for( SumFile sf : files )
            {
                sf.file = new File( commonDir, sf.path );
            }
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long calculateSize()
    {
        long size = 0;

        for( SumFile path : this.files )
        {
            size += path.length;
        }

        return size;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getCommonPath()
    {
        String path = null;

        if( commonDir != null )
        {
            path = commonDir.getAbsolutePath();
        }

        return path;
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void setFiles( List<File> files )
    {
        // -----------------------------------------------------------------
        // Set common directory.
        // -----------------------------------------------------------------
        commonDir = IOUtils.findClosestCommonAncestor( files );

        // -----------------------------------------------------------------
        // Create new list.
        // -----------------------------------------------------------------
        this.files.clear();

        for( File file : files )
        {
            SumFile sf = new SumFile();
            Long len;

            sf.file = file;

            len = file.length();
            sf.length = len;

            this.files.add( sf );
        }

        setPaths( commonDir );
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void addFiles( List<File> files )
    {
        List<File> newList = new ArrayList<>( buildFileList() );

        newList.addAll( files );

        setFiles( newList );
    }

    /***************************************************************************
     * @throws ValidationException
     **************************************************************************/
    public void validate() throws ValidationException
    {
        if( files.isEmpty() )
        {
            throw new ValidationException( "No files selected." );
        }

        IOUtils.validateDirInput( commonDir, "Common Directory" );
    }
}
