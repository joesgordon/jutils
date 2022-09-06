package org.jutils.core.ui.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.Utils;
import org.jutils.core.net.IpAddress;
import org.jutils.core.net.IpAddress.IpVersion;

public class IpAddressTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testDefaultConstructor()
    {
        IpAddress address = new IpAddress();
        byte [] expected = { 0, 0, 0, 0 };

        Assert.assertArrayEquals( expected, address.get() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testIpv4Constructor()
    {
        IpAddress address = new IpAddress( IpVersion.IPV4 );
        byte [] expected = { 0, 0, 0, 0 };

        Assert.assertArrayEquals( expected, address.get() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testIpv6Constructor()
    {
        IpAddress address = new IpAddress( IpVersion.IPV6 );
        byte [] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

        Assert.assertArrayEquals( expected, address.get() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testOctectsConstructor()
    {
        byte [] expected = { -1, 2, 3, 4 };
        IpAddress address = new IpAddress( expected[0], expected[1],
            expected[2], expected[3] );

        Assert.assertArrayEquals( expected, address.get() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testIpv4CopyConstructor()
    {
        byte [] expected = { -1, 2, 3, 4 };
        IpAddress a1 = new IpAddress( expected[0], expected[1], expected[2],
            expected[3] );
        IpAddress a2 = new IpAddress( a1 );

        Assert.assertArrayEquals( expected, a1.get() );
        Assert.assertArrayEquals( a1.get(), a2.get() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testIpv6CopyConstructor()
    {
        byte [] expected = { -1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16 };
        IpAddress a1 = new IpAddress( IpVersion.IPV6 );

        Utils.byteArrayCopy( expected, 0, a1.address, 0, expected.length );

        IpAddress a2 = new IpAddress( a1 );

        Assert.assertArrayEquals( expected, a1.get() );
        Assert.assertArrayEquals( a1.get(), a2.get() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testSetValue()
    {
        byte [] expected = { -1, 2, 3, 4 };
        IpAddress address = new IpAddress( IpVersion.IPV6 );

        address.setValue( 0xFF020304 );

        Assert.assertArrayEquals( expected, address.get() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testGetValue()
    {
        int expected = 0xFF020304;
        IpAddress address = new IpAddress( IpVersion.IPV6 );

        address.setValue( expected );

        Assert.assertEquals( expected, address.getValue() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testIpv4GetInetAddress()
    {
        IpAddress address = new IpAddress( -1, 2, 3, 4 );

        InetAddress ina = address.getInetAddress();

        Assert.assertArrayEquals( address.get(), ina.getAddress() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testIpv4SetInetAddress()
    {
        byte [] expected = { -1, 2, 3, 4 };
        InetAddress ina;
        try
        {
            ina = InetAddress.getByAddress( expected );
        }
        catch( UnknownHostException ex )
        {
            throw new IllegalStateException( ex );
        }

        IpAddress address = new IpAddress( IpVersion.IPV6 );

        address.setInetAddress( ina );

        Assert.assertArrayEquals( expected, address.get() );
    }
}
