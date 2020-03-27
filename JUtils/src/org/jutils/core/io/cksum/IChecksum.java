package org.jutils.core.io.cksum;

public interface IChecksum
{
    public void update( byte [] bytes );

    public void update( byte [] bytes, int off, int len );

    public void reset();

    public byte [] getChecksum();
}
