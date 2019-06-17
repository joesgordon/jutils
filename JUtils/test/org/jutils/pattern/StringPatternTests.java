package org.jutils.pattern;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.ValidationException;

/*******************************************************************************
 *
 ******************************************************************************/
public class StringPatternTests
{
    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testExactMatches()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = testStr;
        pattern.type = StringPatternType.EXACT;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertTrue( testStr + " should match " + pattern.patternText,
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testExactDoesntMatch()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = testStr + "2";
        pattern.type = StringPatternType.EXACT;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertFalse(
                testStr + " should match " + pattern.patternText,
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testContainsMatches()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = testStr.substring( 1, testStr.length() - 1 );
        pattern.type = StringPatternType.CONTAINS;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertTrue( "Pattern " + pattern.patternText +
                " should match text \"" + testStr + "\"",
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testContainsDoesntMatch()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = testStr + "2";
        pattern.type = StringPatternType.CONTAINS;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertFalse( "Pattern " + pattern.patternText +
                " should match text \"" + testStr + "\"",
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testWildcardMatchesBegin()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = "*" +
            testStr.substring( testStr.length() - 4, testStr.length() );
        pattern.type = StringPatternType.WILDCARD;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertTrue( "Pattern " + pattern.patternText +
                " should match text \"" + testStr + "\"",
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testWildcardMatchesEnd()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = testStr.substring( 0, testStr.length() - 4 ) +
            "*";
        pattern.type = StringPatternType.WILDCARD;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertTrue( "Pattern " + pattern.patternText +
                " should match text \"" + testStr + "\"",
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testWildcardMatchesMiddle()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = testStr.substring( 0, 3 ) + "*" +
            testStr.substring( testStr.length() - 4, testStr.length() );
        pattern.type = StringPatternType.WILDCARD;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertTrue( "Pattern " + pattern.patternText +
                " should match text \"" + testStr + "\"",
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testWildcardDoesntMatch()
    {
        String testStr = "bailiwick";
        StringPattern pattern = new StringPattern();

        pattern.isCaseSensitive = true;
        pattern.name = "test";
        pattern.patternText = "z*z*z";
        pattern.type = StringPatternType.WILDCARD;

        try
        {
            IMatcher matcher = pattern.createMatcher();

            Assert.assertFalse( "Pattern " + pattern.patternText +
                " should match text \"" + testStr + "\"",
                matcher.matches( testStr ) );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }
}
