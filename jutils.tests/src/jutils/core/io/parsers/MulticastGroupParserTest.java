package jutils.core.io.parsers;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.ValidationException;
import jutils.core.net.IpAddress;

public class MulticastGroupParserTest
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_AnyInvalid()
    {
        testAddressInvalid( "0.0.0.0" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_GroupMinMinus1Invalid()
    {
        testAddressInvalid( "223.255.255.255" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_GroupMinValid()
    {
        testAddressValid( "224.0.0.0" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_GroupMaxValid()
    {
        testAddressValid( "239.255.255.255" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_GroupMaxPlus1Invalid()
    {
        testAddressInvalid( "240.0.0.0" );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    private static IpAddress testAddressValid( String str )
    {
        IpAddress address = null;

        try
        {
            address = testAddress( str );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }

        return address;
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    private static IpAddress testAddressInvalid( String str )
    {
        IpAddress address = null;

        try
        {
            address = testAddress( str );

            Assert.fail( "Expected exception" );
        }
        catch( ValidationException ex )
        {
        }

        return address;
    }

    /***************************************************************************
     * @param str
     * @return
     * @throws ValidationException
     **************************************************************************/
    private static IpAddress testAddress( String str )
        throws ValidationException
    {
        MulticastGroupParser parser = new MulticastGroupParser();

        return parser.parse( str );
    }
}
