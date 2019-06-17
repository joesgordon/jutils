package org.jutils.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ZipWriter implements Closeable
{
    /**  */
    private final ZipOutputStream stream;
    /**  */
    private final byte [] buffer = new byte[4 * 1024 * 1024];

    /***************************************************************************
     * @param file
     * @throws FileNotFoundException
     **************************************************************************/
    public ZipWriter( File file ) throws FileNotFoundException
    {
        this.stream = new ZipOutputStream( new BufferedOutputStream(
            new FileOutputStream( file ), 4 * 1024 * 1024 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        stream.close();
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void addFile( File file ) throws IOException
    {
        if( file.isFile() )
        {
            addFile( file, file.getName() );
        }
        else if( file.isDirectory() )
        {
            writeDirectory( file );
        }
    }

    /***************************************************************************
     * @param f
     * @param path
     * @throws IOException
     **************************************************************************/
    public void addFile( File f, String path ) throws IOException
    {
        ZipEntry entry = new ZipEntry( path );

        if( f.isDirectory() )
        {
            stream.putNextEntry( entry );
        }
        else
        {
            writeFile( f, entry );
        }
    }

    /***************************************************************************
     * @param file
     * @param entry
     * @throws IOException
     **************************************************************************/
    private void writeFile( File file, ZipEntry entry ) throws IOException
    {
        stream.putNextEntry( entry );

        try( FileInputStream in = new FileInputStream( file ) )
        {
            int count;

            while( ( count = in.read( buffer ) ) > 0 )
            {
                stream.write( buffer, 0, count );
            }
        }
    }

    /***************************************************************************
     * @param dir
     * @throws IOException
     **************************************************************************/
    private void writeDirectory( File dir ) throws IOException
    {
        DirContents contents = listContents( dir );

        for( String path : contents.paths )
        {
            File f = new File( contents.parent, path );
            addFile( f, path );
        }
    }

    /***************************************************************************
     * @param dir
     * @return
     * @throws IOException
     **************************************************************************/
    public static DirContents listContents( File dir ) throws IOException
    {
        DirContents contents = new DirContents();

        contents.parent = dir.getAbsoluteFile().getParentFile();

        addContents( dir, dir.getName(), contents );

        return contents;
    }

    /***************************************************************************
     * @param dir
     * @param path
     * @param contents
     * @throws IOException
     **************************************************************************/
    private static void addContents( File dir, String path,
        DirContents contents ) throws IOException
    {
        File [] files = dir.listFiles();

        if( files == null )
        {
            throw new IOException(
                "Cannot list files in directory: " + dir.getAbsolutePath() );
        }

        for( File f : files )
        {
            String p = path + "/" + f.getName();

            if( f.isDirectory() )
            {
                addContents( f, p, contents );
            }
            else
            {
                contents.paths.add( p );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class DirContents
    {
        /**  */
        public File parent;
        /**  */
        public final List<String> paths;

        /**
         * 
         */
        public DirContents()
        {
            this.paths = new ArrayList<>();
        }
    }
}
