package org.jutils.io;

import java.awt.Component;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.jutils.Utils;
import org.jutils.ValidationException;
import org.jutils.data.SystemProperty;

/*******************************************************************************
 * Utility class for general I/O functions.
 ******************************************************************************/
public final class IOUtils
{
    /** The user directory as returned by {@link SystemProperty#USER_HOME}. */
    public static final File USERS_DIR;
    /** The installation directory of this class/jar. */
    public static final File INSTALL_DIR;
    /**  */
    public static final int DEFAULT_BUF_SIZE = 8 * 1024 * 1024;
    /**  */
    public static final Charset US_ASCII = Charset.forName( "US-ASCII" );

    static
    {
        USERS_DIR = new File( SystemProperty.USER_HOME.getProperty() );
        INSTALL_DIR = getInstallDir( IOUtils.class );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private IOUtils()
    {
    }

    /***************************************************************************
     * Returns a human readable representation of bytes where a KB = 1024 bytes.
     * Hat tip SO question <a
     * href="http://stackoverflow.com/questions/3758606">3758606</a>
     * @param count the number of bytes.
     * @return the human readable string.
     **************************************************************************/
    public static String byteCount( long count )
    {
        int unit = 1024;
        int exp;
        char pre;

        if( count < unit )
        {
            return count + " B";
        }

        exp = ( int )( Math.log( count ) / Math.log( unit ) );
        pre = "KMGTPE".charAt( exp - 1 );
        return String.format( "%.1f %ciB", count / Math.pow( unit, exp ), pre );
    }

    /***************************************************************************
     * Returns the deepest common path of the provided files.
     * @param files the files to find the common path.
     * @return the common path.
     **************************************************************************/
    public static File findClosestCommonAncestor( File... files )
    {
        return findClosestCommonAncestor( Arrays.asList( files ) );
    }

    /***************************************************************************
     * Returns the deepest common path of the provided files.
     * @param files the files to find the common path.
     * @return the common path.
     **************************************************************************/
    public static File findClosestCommonAncestor( List<File> files )
    {
        File ans = null;
        String ansPath = null;
        String [] paths = new String[files.size()];

        for( int i = 0; i < files.size(); i++ )
        {
            File file = files.get( i );
            paths[i] = file.getAbsolutePath();

            paths[i] = paths[i].replace( '\\', '/' );

            if( file.isDirectory() )
            {
                paths[i] += '/';
            }
        }

        for( String path : paths )
        {
            if( ansPath == null )
            {
                ansPath = new File( path ).getParent();
                ansPath += '/';
                ansPath = ansPath.replace( '\\', '/' );
            }
            else
            {
                List<String> ansParts = Utils.split( ansPath, '/' );
                List<String> pathParts = Utils.split( path, '/' );

                ansPath = "";

                for( int i = 0; i < ansParts.size() &&
                    i < pathParts.size(); i++ )
                {
                    if( ansParts.get( i ).equals( pathParts.get( i ) ) )
                    {
                        ansPath += ansParts.get( i ) + "/";
                    }
                    else
                    {
                        break;
                    }
                }

                if( ansPath.isEmpty() )
                {
                    break;
                }

                // int idx = findFirstDiff( ansPath, path );
                //
                // if( idx > 0 )
                // {
                // idx = ansPath.lastIndexOf( '/', idx );
                // ansPath = ansPath.substring( 0, idx ) + '/';
                // }
                // else
                // {
                // ansPath = null;
                // break;
                // }
            }
        }

        if( ansPath != null )
        {
            ans = new File( ansPath );
        }

        return ans;
    }

    /***************************************************************************
     * Replaces all backslashes with forward slashes and adds a trailing slash
     * if needed.
     * @param file the file to standardize.
     * @return the standardized file.
     **************************************************************************/
    public static String getStandardAbsPath( File file )
    {
        String path = file.getAbsolutePath().replace( '\\', '/' );

        if( file.isDirectory() && !path.endsWith( "/" ) )
        {
            path += '/';
        }

        return path;
    }

    /***************************************************************************
     * Returns the first directory that exists, starting at the given path and
     * traversing upwards.
     * @param dirPath The path to start looking.
     * @return The first existing directory or parent directory from the given
     * path or {@code null} if the argument is {@code null}.
     **************************************************************************/
    public static File getExistingDir( String dirPath )
    {
        File f = null;

        if( dirPath != null )
        {
            f = new File( dirPath );
            while( !f.isDirectory() )
            {
                f = f.getParentFile();
            }
        }

        return f;
    }

    /***************************************************************************
     * Returns a file in the user's home directory with the provided relative
     * path.
     * @param filename the path relative to the user's home directory.
     * @return the user's file.
     **************************************************************************/
    public static File getUsersFile( String filename )
    {
        return new File( USERS_DIR, filename );
    }

    /***************************************************************************
     * Returns a path to a file in the user's home directory
     * {@code /names[0]/names[1]/.../names[n]}.
     * @param names the names of each part of the relative path within the
     * user's home directory.
     * @return a path to a file in the user's home directory.
     **************************************************************************/
    public static File getUsersFile( String... names )
    {
        if( names.length < 1 )
        {
            throw new IllegalArgumentException(
                "Must specify at least one name." );
        }

        File file = getUsersFile( names[0] );

        for( int i = 1; i < names.length; i++ )
        {
            file = new File( file, names[i] );
        }

        return file;
    }

    /***************************************************************************
     * Returns a path to a resource that is a sibling to this class/jar file.
     * @param filename the name of the resource.
     * @return the path to the installed resource.
     **************************************************************************/
    public static File getInstallFile( String filename )
    {
        return new File( INSTALL_DIR, filename );
    }

    /***************************************************************************
     * Returns a path to a resource that is a sibling to the class/jar file.
     * @param filename the name of the resource.
     * @param installClass the class contained within the desired installation.
     * @return the path to the installed resource.
     **************************************************************************/
    public static File getInstallFile( String filename, Class<?> installClass )
    {
        File dir = getInstallDir( installClass );

        return new File( dir, filename );
    }

    /***************************************************************************
     * Returns the combined size of the provided files. This does not work for
     * directories. The returned size is the actual size and may (probably) not
     * reflect the size on disk.
     * @param files the files from which a total size will be calculated.
     * @return the total size of all files.
     **************************************************************************/
    public static long getTotalSize( List<File> files )
    {
        long size = 0;

        for( File f : files )
        {
            size += f.length();
        }

        return size;
    }

    /***************************************************************************
     * Returns a path that is the same as the provided with any extension
     * removed. This will not remove multiple extensions; only the effective
     * one.
     * @param file the path to be duplicated.
     * @return a new path with the extension removed.
     **************************************************************************/
    public static File removeExtension( File file )
    {
        File parent = file.getAbsoluteFile().getParentFile();

        return new File( parent, removeFilenameExtension( file ) );
    }

    /***************************************************************************
     * Returns a path that is the same as the provided with the extension
     * replaced with the provided extension.
     * @param file the path to be duplicated.
     * @param ext the extension to replace any existing.
     * @return a new path with the replaced extension.
     **************************************************************************/
    public static File replaceExtension( File file, String ext )
    {
        File parent = file.getAbsoluteFile().getParentFile();
        String name = removeFilenameExtension( file ) + "." + ext;
        File f = new File( parent, name );

        return f;
    }

    /***************************************************************************
     * Returns the file name minus the extension. Does not include path
     * information.
     * @param file the path to be parsed.
     * @return the simple file name.
     **************************************************************************/
    public static String removeFilenameExtension( File file )
    {
        String filename = file.getName();

        int extensionIndex = filename.lastIndexOf( "." );

        if( extensionIndex == -1 )
        {
            return filename;
        }

        return filename.substring( 0, extensionIndex );
    }

    /***************************************************************************
     * Returns the directory containing the jar or class file containing the
     * provided class.
     * @param installClass the class to be located.
     * @return the containing directory or {@code null} if the code is remote.
     **************************************************************************/
    public static File getInstallDir( Class<?> installClass )
    {
        File file = null;
        URL url = installClass.getProtectionDomain().getCodeSource().getLocation();

        try
        {
            // LogUtils.printDebug( "uri: " + url.toURI().getPath() +
            // " for class " + installClass.toString() );
            file = new File( url.toURI().getPath() ).getParentFile();
        }
        catch( URISyntaxException ex )
        {
        }

        return file;
    }

    /***************************************************************************
     * Reads the provided file into a single string.
     * @param file the file to be read.
     * @return the text in the file.
     * @throws FileNotFoundException if the file does not exist, is a directory
     * rather than a regular file, or for some other reason cannot be opened for
     * reading.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public static String readAll( File file )
        throws FileNotFoundException, IOException
    {
        StringBuilder lines = new StringBuilder();

        try( InputStream is = new FileInputStream( file );
             Reader r = new InputStreamReader( is, US_ASCII );
             BufferedReader reader = new BufferedReader( r ) )
        {
            String line;

            while( ( line = reader.readLine() ) != null )
            {
                lines.append( line );
                lines.append( Utils.NEW_LINE );
            }
        }

        return lines.toString();
    }

    /***************************************************************************
     * Returns every line in the provided file as an entry in a list.
     * @param file the file to be read.
     * @return the list of lines (without line separators).
     * @throws FileNotFoundException if the file does not exist, is a directory
     * rather than a regular file, or for some other reason cannot be opened for
     * reading.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public static List<String> readAllLines( File file )
        throws FileNotFoundException, IOException
    {
        List<String> lines = new ArrayList<String>();

        try( InputStream is = new FileInputStream( file );
             Reader r = new InputStreamReader( is, US_ASCII );
             BufferedReader reader = new BufferedReader( r ) )
        {
            String line;

            while( ( line = reader.readLine() ) != null )
            {
                lines.add( line );
            }
        }

        return lines;
    }

    /***************************************************************************
     * Returns a list of all files (in which {@code isDirectory() == false})
     * found in the provided directory.
     * @param dir the directory to be searched.
     * @return the list of all files found in the provided directory guaranteed
     * to be non-null. Will be empty on error.
     **************************************************************************/
    public static List<File> getAllFiles( File dir )
    {
        List<File> files = new ArrayList<File>();

        getAllFiles( dir, files );

        return files;
    }

    /***************************************************************************
     * Recursively adds all paths within the provided directory that are not
     * themselves a directory.
     * @param dir the directory to be searched.
     * @param files the list of files to be added to.
     **************************************************************************/
    private static void getAllFiles( File dir, List<File> files )
    {
        File [] fs = dir.listFiles();

        if( fs == null )
        {
            return;
        }

        for( File f : fs )
        {
            if( f.isDirectory() )
            {
                getAllFiles( f, files );
            }
            else
            {
                files.add( f );
            }
        }
    }

    /***************************************************************************
     * Returns the file extension for the provided file.
     * @param file the file from which an extension is parsed.
     * @return the file extension or an empty string if no extension exists.
     **************************************************************************/
    public static String getFileExtension( File file )
    {
        String fileName = file.getName();
        String extension = "";

        int i = fileName.lastIndexOf( '.' );

        if( i > -1 )
        {
            extension = fileName.substring( i + 1 );
        }

        return extension;
    }

    /***************************************************************************
     * Appends a suffix to a file name prior to the extension.
     * @param file the file path to be changed.
     * @param suffix the string to add to the end of the file name.
     * @return the new path with the appended string.
     **************************************************************************/
    public static File appendToName( File file, String suffix )
    {
        String ext = getFileExtension( file );
        File f = removeExtension( file );

        return new File( f.getAbsolutePath() + suffix + "." + ext );
    }

    /***************************************************************************
     * Returns a list of directories that contain the provided path. Neither the
     * provided path nor ancestors are required to exist.
     * @param path the path to generate an ancestor list.
     * @return the list of ancestors (parent directories).
     **************************************************************************/
    public static List<File> getAncestors( File path )
    {
        List<File> files = new ArrayList<>();

        path = path.getAbsoluteFile();

        while( path != null )
        {
            files.add( 0, path );
            path = path.getParentFile();
        }

        return files;
    }

    /***************************************************************************
     * Removes the entire contents of the provided directory including any
     * sub-directories.
     * @param dir the directory to be cleaned.
     * @throws IOException if any path cannot be deleted or any I/O error
     * occurs.
     * @see File#delete()
     **************************************************************************/
    public static void deleteContents( File dir ) throws IOException
    {
        File [] files = dir.listFiles();

        if( files != null )
        {
            for( File f : files )
            {
                if( f.isDirectory() )
                {
                    deleteContents( f );
                }

                Files.delete( f.toPath() );
            }
        }
    }

    /***************************************************************************
     * Checks to ensure that the provided file exists, is a file, and can be
     * read.
     * @param file the file to be checked.
     * @param name the conceptual name of the file.
     * @throws ValidationException if any check fails.
     **************************************************************************/
    public static void validateFileInput( File file, String name )
        throws ValidationException
    {
        if( !file.exists() )
        {
            throw new ValidationException( "The specified " + name +
                " file does not exist: " + file.getAbsolutePath() );
        }
        else if( !file.isFile() )
        {
            throw new ValidationException( "The specified " + name +
                " file exists, but is not a file: " + file.getAbsolutePath() );
        }
        else if( !file.canRead() )
        {
            throw new ValidationException( "Cannot read from the specified " +
                name + " file: " + file.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * Checks to ensure that the provided directory exists, is a directory and
     * can be read.
     * @param dir the directory to be checked.
     * @param name the conceptual name of the directory.
     * @throws ValidationException if any check fails.
     **************************************************************************/
    public static void validateDirInput( File dir, String name )
        throws ValidationException
    {
        if( !dir.exists() )
        {
            throw new ValidationException( "The specified " + name +
                " directory does not exist: " + dir.getAbsolutePath() );
        }
        else if( !dir.isDirectory() )
        {
            throw new ValidationException( "The specified " + name +
                " directory exists, but is not a directory: " +
                dir.getAbsolutePath() );
        }
        else if( !dir.canRead() )
        {
            throw new ValidationException( "Cannot read from the specified " +
                name + " directory: " + dir.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * Checks to ensure that the provided file can be created if it doesn't
     * exist or is a file that can be written to if it does.
     * @param file the file to be checked.
     * @param name the conceptual name of the file.
     * @throws ValidationException if any check fails.
     **************************************************************************/
    public static void validateFileOuput( File file, String name )
        throws ValidationException
    {
        if( !file.exists() )
        {
            File parent = file.getAbsoluteFile().getParentFile();

            if( !parent.exists() )
            {
                throw new ValidationException( "The specified " + name +
                    " file's parent directory does not exist: " +
                    parent.getAbsolutePath() );
            }
            else if( !parent.canWrite() )
            {
                throw new ValidationException(
                    "Cannot write to the specified " + name +
                        " file's parent directory: " +
                        parent.getAbsolutePath() );
            }
        }
        else if( !file.isFile() )
        {
            throw new ValidationException( "The specified " + name +
                " file is not a file: " + file.getAbsolutePath() );
        }
        else if( !file.canWrite() )
        {
            throw new ValidationException( "Cannot write to the specified " +
                name + " file: " + file.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * Checks to ensure that the provided directory exists, is a directory, and
     * can be written to.
     * @param dir the directory to be checked.
     * @param name the conceptual name of the directory.
     * @throws ValidationException if any check fails.
     **************************************************************************/
    public static void validateDirOutput( File dir, String name )
        throws ValidationException
    {
        if( !dir.exists() )
        {
            throw new ValidationException( "The specified " + name +
                " directory does not exist: " + dir.getAbsolutePath() );
        }
        else if( !dir.isDirectory() )
        {
            throw new ValidationException( "The specified " + name +
                " directory directory is not a directory: " +
                dir.getAbsolutePath() );
        }
        else if( !dir.canWrite() )
        {
            throw new ValidationException( "Cannot write to the specified " +
                name + " directory: " + dir.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * Creates all directories needed including the parent if the parent of the
     * provided file is not a directory.
     * @param file the file to be checked.
     * @return {@code true} if the parent exists after this function call;
     * {@code false} otherwise.
     **************************************************************************/
    public static boolean ensureParentExists( File file )
    {
        File dir = file.getAbsoluteFile().getParentFile();

        if( !dir.isDirectory() )
        {
            if( !dir.mkdirs() )
            {
                return false;
            }
        }

        return true;
    }

    /***************************************************************************
     * Returns {@code true} if the provided descendant is a descendant of the
     * provided ancestor.
     * @param ancestor the notional ancestor.
     * @param descendant the notional descendant.
     * @return {@code true} if the file is a descendant.
     **************************************************************************/
    public static boolean isAncestorOf( File ancestor, File descendant )
    {
        if( ancestor.equals( descendant ) )
        {
            return false;
        }

        while( ( descendant != null ) && !ancestor.equals( descendant ) )
        {
            // LogUtils.printDebug( "\tChecking: " +
            // descendant.getAbsolutePath() );
            descendant = descendant.getParentFile();
            // if( descendant == null )
            // {
            // LogUtils.printDebug( "\t\tHas no parent!" );
            // }
        }

        return descendant != null;
    }

    /***************************************************************************
     * Separates paths from the provided string using {@link File#pathSeparator}
     * as a delimiter.
     * @param paths the system specific string of paths.
     * @return the files listed in the path string.
     **************************************************************************/
    public static File [] getFilesFromString( String paths )
    {
        String [] dirPaths = paths.split( File.pathSeparator );
        File [] dirs = new File[dirPaths.length];

        for( int i = 0; i < dirs.length; i++ )
        {
            String path = dirPaths[i];

            // because new File( "" ).isDirectory() returns false
            // see https://stackoverflow.com/questions/5883808
            path = path.isEmpty() ? "." : path;

            dirs[i] = new File( path );
        }

        return dirs;
    }

    /***************************************************************************
     * Concatenates the provided files using {@link File#pathSeparator} as a
     * delimiter.
     * @param files the paths to be concatenated.
     * @return a system specific single string of paths.
     **************************************************************************/
    public static String getStringFromFiles( File [] files )
    {
        String paths = null;

        if( files != null )
        {
            paths = "";

            for( int i = 0; i < files.length; i++ )
            {
                if( paths.length() > 0 )
                {
                    paths += File.pathSeparator;
                }
                paths += files[i].getAbsolutePath();
            }
        }

        return paths;
    }

    /***************************************************************************
     * Returns all files in the provided directory that have the provided
     * extension.
     * @param dir the directory to be searched.
     * @param ext the case insensitive extension to be matched sans dot.
     * @return the files found.
     **************************************************************************/
    public static List<File> getAllFiles( File dir, String ext )
    {
        FileFilter filter = new ExtensionFilter( ext );
        File [] fa = dir.listFiles( filter );
        fa = fa == null ? new File[0] : fa;
        List<File> files = new ArrayList<>( fa.length );

        Collections.addAll( files, fa );

        return files;
    }

    /***************************************************************************
     * Checks for contents in the provided directory and asks the user if the
     * contents should be deleted or ignored/overwritten.
     * @param dir the directory to be checked.
     * @param parent the component used as the parent for any dialogs generated.
     * @return {@code true} if the processing should continue; {@code false}
     * otherwise.
     **************************************************************************/
    public static boolean checkForContents( File dir, Component parent )
    {
        File outDir = dir;
        File [] files = outDir.listFiles();

        if( files == null )
        {
            JOptionPane.showMessageDialog( parent,
                "Output directory does not exist or cannot be read: " +
                    Utils.NEW_LINE + outDir.getAbsolutePath(),
                "Configuration Error", JOptionPane.ERROR_MESSAGE );

            return false;
        }
        else if( files.length == 0 )
        {
            return true;
        }

        String delOption = "Delete";
        String contOption = "Continue";
        String cancelOption = "Cancel";
        String [] options = new String[] { delOption, contOption,
            cancelOption };
        int ans = JOptionPane.showOptionDialog( parent,
            "The output directory contains files. Do you want to delete them before proceeding?",
            "Output Directory Not Empty", JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, delOption );

        if( ans == JOptionPane.CLOSED_OPTION ||
            ans == JOptionPane.CANCEL_OPTION )
        {
            return false;
        }
        else if( ans == JOptionPane.YES_OPTION )
        {
            try
            {
                IOUtils.deleteContents( outDir );
                return true;
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( parent,
                    "Unable to delete output directory contents: " +
                        ex.getMessage(),
                    "Deletion Error", JOptionPane.ERROR_MESSAGE );
                return false;
            }
        }
        else if( ans == JOptionPane.NO_OPTION )
        {
            return true;
        }

        throw new IllegalStateException(
            "Incorrect handling of call to JOptionPane.showOptionDialog" );
    }

    /***************************************************************************
     * Returns the contents of the provided directory that match the provided
     * pattern.
     * @param dir the directory to be searched.
     * @param filePattern the pattern to be matched.
     * @return the list of files found.
     * @throws IOException if an I/O exception occurs
     * @see Files#newDirectoryStream
     **************************************************************************/
    public static List<File> listWithWildcard( File dir, String filePattern )
        throws IOException
    {
        List<File> files = new ArrayList<>();

        try( DirectoryStream<Path> paths = Files.newDirectoryStream(
            dir.toPath(), filePattern ) )
        {
            for( Path p : paths )
            {
                files.add( p.toFile() );
            }
        }

        return files;
    }

    /***************************************************************************
     * Converts a file to a URL.
     * @param file the file to convert to a url.
     * @return the URL representation of the provided file.
     * @throws IllegalStateException if {@link URI#toURL()} throws a
     * {@link MalformedURLException}
     **************************************************************************/
    public static URL fileToUrl( File file ) throws IllegalStateException
    {
        URL url = null;

        try
        {
            url = file.toURI().toURL();
        }
        catch( MalformedURLException ex )
        {
            throw new IllegalStateException(
                "It is not expected that a valid file cannot be converted to a URL.",
                ex );
        }

        return url;
    }

    /***************************************************************************
     * Determines if the provided file is binary. The provided file is binary if
     * either a {@code 0x00} byte or more than 25% non-printable bytes are found
     * in the first 2048 bytes.
     * @param file the file to be analyzed.
     * @return {@code true} if the file is binary; {@code false} if ASCII.
     * @throws FileNotFoundException if the path does not exist or an access
     * error occurs.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public static boolean isBinary( File file )
        throws FileNotFoundException, IOException
    {
        int len = file.length() < 2048 ? ( int )file.length() : 2048;
        byte [] buf = new byte[len];
        int threshold = ( int )( 0.25 * len );

        int cnt = 0;

        try( FileStream fs = new FileStream( file, true ) )
        {
            fs.read( buf );
        }

        for( int i = 0; i < len; i++ )
        {
            if( buf[i] == 0 )
            {
                // LogUtils.printDebug( "\t found \\0" );
                return true;
            }
            else if( isNonPrintable( buf[i] ) )
            {
                cnt++;

                // LogUtils.printDebug(
                // "\t non-printable %02X @ %d count %d of %d", buf[i], i, cnt,
                // threshold );

                if( cnt >= threshold )
                {
                    return true;
                }
            }
        }

        return false;
    }

    /***************************************************************************
     * Tests if the provided byte is non-printable.
     * @param b the byte under test.
     * @return {@code true} if the byte is non-printable; {@code false}
     * otherwise.
     **************************************************************************/
    private static boolean isNonPrintable( byte b )
    {
        switch( b )
        {
            case 9:
                return false;
            case 10:
                return false;
            case 13:
                return false;

            default:
                if( b > 19 && b < 127 )
                {
                    return false;
                }
                return true;
        }
    }
}
