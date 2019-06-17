package org.jutils.io.bits;

import org.jutils.io.BitBuffer;

public interface IBitShifter
{
    public void shift( BitBuffer from, BitBuffer to, int byteCount );
}
