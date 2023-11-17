package jutils.core.io.bits;

import jutils.core.io.BitBuffer;

public interface IBitShifter
{
    public void shift( BitBuffer from, BitBuffer to, int byteCount );
}
