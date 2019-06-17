package org.jutils.io.cksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Checksum implements IChecksum
{
    private final MessageDigest complete;

    public Sha1Checksum()
    {
        try
        {
            this.complete = MessageDigest.getInstance( "SHA-1" );
        }
        catch( NoSuchAlgorithmException ex )
        {
            throw new IllegalStateException(
                "SHA-1 is not supported on this device", ex );
        }
    }

    @Override
    public void update( byte [] bytes )
    {
        complete.update( bytes );
    }

    @Override
    public void update( byte [] bytes, int off, int len )
    {
        complete.update( bytes, off, len );
    }

    @Override
    public void reset()
    {
        complete.reset();
    }

    @Override
    public byte [] getChecksum()
    {
        return complete.digest();
    }
}
