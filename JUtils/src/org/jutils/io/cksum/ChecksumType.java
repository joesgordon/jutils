package org.jutils.io.cksum;

import org.jutils.INamedItem;

public enum ChecksumType implements INamedItem
{
    MD5( "MD5" ),
    CRC32( "CRC-32" ),
    SHA_256( "SHA-256" ),
    SHA_1( "SHA-1" );

    public final String name;

    private ChecksumType( String name )
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
