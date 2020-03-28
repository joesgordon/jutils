package org.jutils.apps.filespy.data;

import java.io.File;
import java.time.LocalDate;
import java.util.regex.PatternSyntaxException;

import org.jutils.core.ValidationException;
import org.jutils.core.pattern.StringPattern;
import org.jutils.core.utils.Usable;

/*******************************************************************************
 * This class defines the parameters of the files to find.
 ******************************************************************************/
public class SearchParams
{
    /** The filename pattern. */
    public final StringPattern filename;
    /** The contents pattern. */
    public final Usable<StringPattern> contents;
    /** The paths to be searched. */
    public File path;
    /** Kilobytes that the file must be more than. */
    public final Usable<Long> moreThan;
    /** Kilobytes that the file must be less than. */
    public final Usable<Long> lessThan;
    /** Date after which the last modified date of the file must occur. */
    public final Usable<LocalDate> after;
    /** Date before which the last modified date of the file must occur. */
    public final Usable<LocalDate> before;

    /** Option to search sub-folders. */
    public boolean searchSubfolders;
    /** Option to return all files not matched. */
    public boolean filenameNot;

    /***************************************************************************
     * Creates new search parameters.
     **************************************************************************/
    public SearchParams()
    {
        this.filename = new StringPattern( "Filename" );
        this.contents = new Usable<>( false, new StringPattern( "Contents" ) );
        this.path = new File( "/" );
        this.searchSubfolders = true;
        this.moreThan = new Usable<>( false );
        this.lessThan = new Usable<>( false );
        this.after = new Usable<>( false );
        this.before = new Usable<>( false );
        this.filenameNot = false;
    }

    /***************************************************************************
     * @param sp
     **************************************************************************/
    public SearchParams( SearchParams sp )
    {
        this.filename = sp.filename;
        this.contents = new Usable<>( sp.contents );
        this.path = sp.path;
        this.searchSubfolders = sp.searchSubfolders;
        this.moreThan = new Usable<>( sp.moreThan );
        this.lessThan = new Usable<>( sp.lessThan );
        this.after = new Usable<>( sp.after );
        this.before = new Usable<>( sp.before );
        this.filenameNot = sp.filenameNot;
    }

    /***************************************************************************
     * @throws ValidationException
     **************************************************************************/
    public void validate() throws ValidationException
    {
        // ---------------------------------------------------------------------
        // Check the path
        // ---------------------------------------------------------------------
        if( !path.isDirectory() )
        {
            throw new ValidationException(
                "Non-existant directory: " + path.getAbsolutePath() );
        }

        // ---------------------------------------------------------------------
        // Check regex.
        // ---------------------------------------------------------------------
        try
        {
            filename.createMatcher();
        }
        catch( PatternSyntaxException ex )
        {
            throw new ValidationException(
                "Invalid filename pattern: " + ex.getMessage() );
        }

        if( contents.isUsed )
            try
            {
                contents.data.createMatcher();
            }
            catch( PatternSyntaxException ex )
            {
                throw new ValidationException(
                    "Invalid contents pattern: " + ex.getMessage() );
            }
    }
}
