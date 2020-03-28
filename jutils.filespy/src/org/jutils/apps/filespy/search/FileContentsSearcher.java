package org.jutils.apps.filespy.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicLong;

import org.jutils.apps.filespy.data.LineMatch;
import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.core.concurrent.IConsumer;
import org.jutils.core.concurrent.ITaskHandler;
import org.jutils.core.io.IOUtils;
import org.jutils.core.pattern.IMatcher;
import org.jutils.core.pattern.Match;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileContentsSearcher implements IConsumer<SearchRecord>
{
    /**  */
    private final IMatcher contentsPattern;
    /**  */
    private final SearchResultsHandler searchHandler;

    /**  */
    private final AtomicLong fileCount;
    /**  */
    private final AtomicLong filesSearched;

    /***************************************************************************
     * @param contentsPattern
     * @param searchHandler
     **************************************************************************/
    public FileContentsSearcher( IMatcher contentsPattern,
        SearchResultsHandler searchHandler )
    {
        this.contentsPattern = contentsPattern;
        this.searchHandler = searchHandler;
        this.fileCount = new AtomicLong( 0L );
        this.filesSearched = new AtomicLong( 0L );
    }

    /***************************************************************************
     * @param str
     * @param record
     * @param lineNumber
     * @return
     **************************************************************************/
    private boolean searchString( String str, SearchRecord record,
        int lineNumber )
    {
        boolean matched = false;
        Match m = contentsPattern.find( str );

        // LogUtils.printDebug( "\t Searching \"%s\"", str );

        if( m.matches )
        {
            matched = true;

            // Utils.printDebug( "bufLen: " + chars.length +
            // ", start: " + matcher.start() + ", end: " +
            // matcher.end() );

            record.addLine( createLineMatch( str, m, lineNumber ) );
        }

        str = null;
        record = null;

        return matched;
    }

    /***************************************************************************
     * @param str
     * @param m
     * @param lineNum
     * @return
     **************************************************************************/
    private static LineMatch createLineMatch( String str, Match m, int lineNum )
    {
        int lineStart = 0;
        int lineEnd = str.length();
        int start = m.start;
        int end = m.end;
        int length = end - start;

        if( length > 1024 )
        {
            lineStart = start;
            end = start + 1024;
            length = 1024;
            lineEnd = end;
        }

        String pre = str.substring( lineStart, start );
        String mat = str.substring( start, end );
        String pst = end == lineEnd ? "" : str.substring( end, lineEnd );

        return new LineMatch( lineNum, pre, mat, pst );
    }

    /***************************************************************************
     * @param record
     * @param stopper
     * @throws IOException
     **************************************************************************/
    private void searchFile( SearchRecord record, ITaskHandler stopper )
        throws IOException
    {
        File file = record.getFile();
        String line;

        boolean matched = false;

        long count = fileCount.get();
        long searched = filesSearched.get();

        // LogUtils.printDebug( "Searching file %s? %s", file.getAbsolutePath(),
        // stopper.canContinue() );
        searchHandler.updateStatus( "Searching file " + searched + " of " +
            count + ": " + file.getAbsolutePath() );

        if( IOUtils.isBinary( file ) )
        {
            String msg = String.format( "Skipping binary file %s",
                file.getAbsolutePath() );
            SearchResultsHandler.addErrorMessage( msg );
            // LogUtils.printDebug( msg );
            return;
        }

        try( InputStream is = new FileInputStream( file );
             Reader r = new InputStreamReader( is, IOUtils.get8BitEncoding() );
             LineNumberReader lineReader = new LineNumberReader( r ) )
        {
            while( ( line = lineReader.readLine() ) != null &&
                stopper.canContinue() )
            {
                if( searchString( line, record, lineReader.getLineNumber() ) )
                {
                    // ---------------------------------------------------------
                    // Do not break early because we want to find all the lines
                    // to display to the user.
                    // ---------------------------------------------------------
                    matched = true;
                }
            }
        }
        finally
        {
            if( matched )
            {
                searchHandler.addFile( record );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addFile()
    {
        fileCount.incrementAndGet();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void consume( SearchRecord data, ITaskHandler stopper )
    {
        try
        {
            searchFile( data, stopper );
        }
        catch( IOException ex )
        {
            // LogUtils.printDebug( "I/O Error: %s" + ex.getMessage() );
            SearchResultsHandler.addErrorMessage( ex.getMessage() );
        }
        finally
        {
            // long count = fileCount.get();
            // long searched =
            filesSearched.incrementAndGet();
            //
            // LogUtils.printDebug( "Searched %d of %d", searched, count );
        }
    }
}
