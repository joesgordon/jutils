package org.jutils.pattern;

import java.io.File;

import org.jutils.INamedItem;
import org.jutils.ValidationException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilenamePattern
{
    /***************************************************************************
     * 
     **************************************************************************/
    private FilenamePattern()
    {
    }

    /***************************************************************************
     * @param pattern
     * @return
     * @throws ValidationException
     **************************************************************************/
    public static IFilenameMatcher createMatcher( StringPattern pattern )
        throws ValidationException
    {
        IMatcher strMatcher = pattern.createMatcher();

        return new FilenameMatcher( strMatcher );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IFilenameMatcher extends INamedItem
    {
        /**
         * @param file
         * @return
         */
        public boolean match( File file );

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FilenameMatcher implements IFilenameMatcher
    {
        /**  */
        private final IMatcher strMatcher;

        /**
         * @param strMatcher
         */
        public FilenameMatcher( IMatcher strMatcher )
        {
            this.strMatcher = strMatcher;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean match( File file )
        {
            return strMatcher.matches( file.getName() );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return strMatcher.getName();
        }
    }
}
