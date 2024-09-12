package org.jutils.summer.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

import org.jutils.core.ArrayPrinter;
import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.IReader;
import org.jutils.core.io.cksum.ChecksumType;
import org.jutils.summer.data.ChecksumResult;
import org.jutils.summer.data.SumFile;

/*******************************************************************************
 * 
 *******************************************************************************/
public class ChecksumFileSerializer implements IReader<ChecksumResult, File>
{
    /**  */
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat(
        "M/d/YYYY h:mm:ss a" );

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChecksumResult read( File file )
        throws IOException, ValidationException
    {
        File parentFile = file.getAbsoluteFile().getParentFile();

        ChecksumResult input = new ChecksumResult();
        String ext = IOUtils.getFileExtension( file ).toUpperCase();

        try
        {
            input.type = ChecksumType.valueOf( ext );
			//JOptionPane.showMessageDialog(null, "Woot", "InfoBox: " + ext, JOptionPane.INFORMATION_MESSAGE);
        }
        catch( IllegalArgumentException ex )
        {
            throw new ValidationException(
                "Incompatible extention, \"" + ext + "\". Must be one of " +
                    ArrayPrinter.toString( ChecksumType.values() ),
                ex );
        }

        input.commonDir = parentFile;

        try( FileReader fr = new FileReader( file );
             BufferedReader reader = new BufferedReader( fr ) )
        {
            String line;

            while( ( line = reader.readLine() ) != null )
            {
                SumFile fs = new SumFile();

                line = line.trim();

                if( line.isEmpty() || line.charAt( 0 ) == '#' )
                {
                    continue;
                }

                int idx = line.indexOf( '*' );

                if( idx > -1 && line.length() > idx )
                {
                    fs.checksum = line.substring( 0, idx ).trim().toLowerCase();
                    fs.path = line.substring( idx + 1 ).trim();
                    fs.file = new File( parentFile, fs.path );

                    input.files.add( fs );
                }
            }
        }

        return input;
    }

    /***************************************************************************
     * @param results
     * @return
     **************************************************************************/
    public static String write( ChecksumResult results )
    {
        StringBuilder str = new StringBuilder();
		
		if (results.type.name == "SHA-256")
		{
			str.append( "# SHA-256 compliant checksum(s) " );
		}
		else if (results.type.name == "SHA-1")
		{
			str.append( "# SHA-1 compliant checksum(s) " );
		}
		else if (results.type.name == "CRC-32")
		{
			str.append( "# CRC-32 compliant checksum(s) " );
		}
		else if (results.type.name == "MD5")
		{
			str.append(
            "# MD5 checksums compatible with MD5summer (http://www.md5summer.org)" );
		}
		else
		{
			throw new IllegalStateException(
                    "checksum null for type " + results.type.name );
		}			
        str.append( Utils.NEW_LINE );
        str.append( "# Generated " );
        str.append( DATE_FMT.format( new Date() ) );
        str.append( Utils.NEW_LINE );
        str.append( Utils.NEW_LINE );

        for( SumFile sf : results.files )
        {
            if( sf == null )
            {
                throw new IllegalStateException( "SumFile null" );
            }
            else if( sf.checksum == null )
            {
                throw new IllegalStateException(
                    "checksum null for file " + sf.file );
            }

            str.append( sf.checksum.toLowerCase() );
            str.append( " *" );
            str.append( sf.path );
            str.append( Utils.NEW_LINE );
        }

        return str.toString();
    }

    /***************************************************************************
     * @param input
     * @param outputFile
     * @throws FileNotFoundException
     **************************************************************************/
    public static void write( ChecksumResult input, File outputFile )
        throws FileNotFoundException
    {
        try( PrintStream stream = new PrintStream( outputFile ) )
        {
			if (input.type.name == "SHA-256")
			{
				stream.println( "# SHA-256 compliant checksum(s) " );
			}
			else if (input.type.name == "SHA-1")
			{
				stream.println( "# SHA-1 compliant checksum(s) " );
			}
			else if (input.type.name == "CRC-32")
			{
				stream.println( "# CRC-32 compliant checksum(s) " );
			}
			else if (input.type.name == "MD5")
			{
				stream.println(
				"# MD5 checksums compatible with MD5summer (http://www.md5summer.org)" );
			}
			else
			{
				throw new IllegalStateException(
						"checksum null for type " + input.type.name );
			}			

            stream.print( "# Generated " );
            stream.println( DATE_FMT.format( new Date() ) );
            stream.println();

            for( SumFile sf : input.files )
            {
                stream.print( sf.checksum.toLowerCase() );
                stream.print( " *" );
                stream.println( sf.path );
            }
        }
    }

    /***************************************************************************
     * @param input
     * @param outputFile
     * @param append
     * @param replace
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public void write( ChecksumResult results, File outputFile, boolean append,
        boolean replace ) throws ValidationException, IOException
    {
        if( append && outputFile.canRead() )
        {
            ChecksumResult existing = read( outputFile );

            results = merge( results, existing, replace );
        }

        write( results, outputFile );
    }

    /***************************************************************************
     * @param results
     * @param existing
     * @param replace
     * @return
     **************************************************************************/
    private static ChecksumResult merge( ChecksumResult results,
        ChecksumResult existing, boolean replace )
    {
        ChecksumResult merged = new ChecksumResult();

        merged.commonDir = results.commonDir;

        if( replace )
        {
            merged.files.addAll( existing.files );

            for( SumFile sum : results.files )
            {
                for( SumFile es : existing.files )
                {
                    if( es.path.equals( sum.path ) )
                    {
                        merged.files.remove( es );
                        break;
                    }
                }

                merged.files.add( sum );
            }
        }
        else
        {
            merged.files.addAll( existing.files );
            merged.files.addAll( results.files );
        }

        return merged;
    }
}
