package org.jutils.io;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitsReaderTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testLongBits()
    {
        List<BrTestParams> vals = new ArrayList<>();

        for( int i = 0; i < Long.SIZE; i++ )
        {
            vals.add( new BrTestParams( i, i, 1L ) );
            vals.add( new BrTestParams( i, i, 0 ) );
        }

        // for( int m = 2; m <= 2; m++ )
        for( int m = 1; m < BitsReader.MASKS.length; m++ )
        {
            int shiftCount = Long.SIZE - m;
            long mask = BitsReader.MASKS[m];
            long alts = mask;

            for( long i = 0; i < m; i++ )
            {
                if( ( i % 2 ) == 0 )
                {
                    alts &= ~( 1L << i );
                }
            }

            long altAlts = ( ~alts ) & mask;

            for( int i = 0; i <= shiftCount; i++ )
            {
                vals.add( new BrTestParams( i, i + m - 1, mask ) );
                vals.add( new BrTestParams( i, i + m - 1, alts ) );
                vals.add( new BrTestParams( i, i + m - 1, altAlts ) );
            }
        }

        for( int i = 0; i < vals.size(); i++ )
        {
            BrTestParams btp = vals.get( i );
            // LogUtils.printDebug( "var[%d] = %s", i, btp );

            BitsReader reader = new BitsReader( btp.start, btp.end );

            long actual;

            actual = 0L;
            actual = reader.write( btp.value, actual );
            Assert.assertEquals( String.format( "Write %s to 0", btp ),
                btp.zeroShift, actual );

            actual = -1L;
            actual = reader.write( btp.value, actual );
            Assert.assertEquals( String.format( "Write %s to -1", btp ),
                btp.oneShift, actual );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class BrTestParams
    {
        public final int start;
        public final int end;
        public final long value;
        public final long zeroShift;
        public final long oneShift;

        public BrTestParams( int start, int end, long value )
        {
            this.start = start;
            this.end = end;
            this.value = value;

            this.zeroShift = value << start;

            long val = -1;

            for( long i = start; i <= end; i++ )
            {
                val &= ~( 1L << i );
            }

            val |= ( value << start );

            this.oneShift = val;
        }

        @Override
        public String toString()
        {
            return String.format( "{ %d, %d, %016X, %016X, %016X }", start, end,
                value, zeroShift, oneShift );
        }
    }
}
