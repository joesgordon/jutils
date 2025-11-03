package jutils.core.io.cksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Md5Checksum implements IChecksum
{
    /**  */
    private final MessageDigest complete;

    /***************************************************************************
     * 
     **************************************************************************/
    public Md5Checksum()
    {
        try
        {
            this.complete = MessageDigest.getInstance( "MD5" );
        }
        catch( NoSuchAlgorithmException ex )
        {
            throw new IllegalStateException(
                "MD5 is not supported on this device", ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void update( byte [] bytes )
    {
        complete.update( bytes );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void update( byte [] bytes, int off, int len )
    {
        complete.update( bytes, off, len );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void reset()
    {
        complete.reset();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getChecksum()
    {
        return complete.digest();
    }
}
