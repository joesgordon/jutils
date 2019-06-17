package org.jutils.io.bits;

import java.util.ArrayList;
import java.util.List;

import org.jutils.io.BitBuffer;
import org.jutils.io.BitPosition;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitShifterFactory
{
    /**  */
    private final IBitShifter [] [] shifters;

    /***************************************************************************
     * 
     **************************************************************************/
    public BitShifterFactory()
    {
        this.shifters = new IBitShifter[8][8];

        fillShifters( shifters );
    }

    /***************************************************************************
     * @param from
     * @param to
     * @return
     **************************************************************************/
    public IBitShifter getShifter( int from, int to )
    {
        return shifters[from][to];
    }

    /***************************************************************************
     * @param shifters
     **************************************************************************/
    private static void fillShifters( IBitShifter [] [] shifters )
    {
        BitPosition from = new BitPosition();
        BitPosition to = new BitPosition();

        for( int f = 0; f < 8; f++ )
        {
            from.set( 0, f );
            for( int t = 0; t < 8; t++ )
            {
                to.set( 0, t );

                List<PhaseInfo> phases = generateShifts( from, to );

                shifters[f][t] = createShifter( phases );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void printMetrics()
    {
        BitPosition from = new BitPosition();
        BitPosition to = new BitPosition();

        for( int f = 0; f < 8; f++ )
        {
            from.set( 0, f );
            for( int t = 0; t < 8; t++ )
            {
                to.set( 0, t );
                System.out.format(
                    "--------------- From %d to %d ---------------\n",
                    from.getBit(), to.getBit() );
                List<PhaseInfo> phases = generateShifts( from, to );

                if( phases.isEmpty() )
                {
                    System.out.println( "Byte" );
                }
                else
                {
                    for( PhaseInfo pi : phases )
                    {
                        System.out.println( pi.toString() );
                    }
                }
            }
        }
    }

    /***************************************************************************
     * @param from
     * @param to
     * @return
     **************************************************************************/
    private static List<PhaseInfo> generateShifts( BitPosition from,
        BitPosition to )
    {
        List<PhaseInfo> phases = new ArrayList<>();
        int remaining = 8;
        int cnt = -1;

        BitPosition f2 = new BitPosition( from );
        BitPosition t2 = new BitPosition( to );

        while( remaining > 0 )
        {
            cnt = 8 - Math.max( f2.getBit(), t2.getBit() );
            cnt = Math.min( cnt, remaining );

            if( cnt < 8 )
            {
                phases.add( new PhaseInfo( cnt, f2.getByte(), f2.getBit(),
                    t2.getByte(), t2.getBit() ) );

                f2.increment( cnt );
                t2.increment( cnt );
            }

            remaining -= cnt;
        }

        return phases;
    }

    /***************************************************************************
     * @param cnt
     * @param idx
     * @return
     **************************************************************************/
    protected static int createFromMask( int cnt, int idx )
    {
        int mask = 0;
        int shift = 8 - cnt - idx;

        if( cnt == 0 )
        {
            throw new IllegalArgumentException( "Cannot mask 0 bytes" );
        }
        else if( shift > 7 )
        {
            throw new IllegalArgumentException( "Cannot shift > 7: " + shift );
        }

        for( int i = 0; i < cnt; i++ )
        {
            mask |= ( 0x80 >>> ( i + idx ) );
        }

        return mask;
    }

    /***************************************************************************
     * @param cnt
     * @param idx
     * @return
     **************************************************************************/
    protected static int createToMask( int cnt, int idx )
    {
        return ~createFromMask( cnt, idx ) & 0xFF;
    }

    /***************************************************************************
     * @param phases
     * @return
     **************************************************************************/
    private static IBitShifter createShifter( List<PhaseInfo> phases )
    {
        IBitShifter shifter = null;

        if( phases.isEmpty() )
        {
            shifter = new ByteBitShifter();
        }
        else
        {
            List<IBitShiftPhase> shiftPhases = new ArrayList<>( 3 );

            for( PhaseInfo pi : phases )
            {

                int shift = pi.toBit - pi.fromBit;
                int fromIdx = pi.fromByte;
                int toIdx = pi.toByte;
                int fromMask = createFromMask( pi.count, pi.fromBit );
                int toMask = createToMask( pi.count, pi.toBit );

                IBitShiftPhase shiftPhase = null;

                if( shift == 0 )
                {
                    shiftPhase = new NoBitShiftPhase( fromMask, toMask, fromIdx,
                        toIdx );
                }
                else if( shift < 0 )
                {
                    shiftPhase = new LeftBitShiftPhase( fromMask, toMask,
                        fromIdx, toIdx, -shift );
                }
                else
                {
                    shiftPhase = new RightBitShiftPhase( fromMask, toMask,
                        fromIdx, toIdx, shift );
                }

                shiftPhases.add( shiftPhase );
            }

            if( shiftPhases.size() == 2 )
            {
                shifter = new TwoPhaseBitShifter( shiftPhases.get( 0 ),
                    shiftPhases.get( 1 ) );
            }
            else if( shiftPhases.size() == 3 )
            {
                shifter = new ThreePhaseBitShifter( shiftPhases.get( 0 ),
                    shiftPhases.get( 1 ), shiftPhases.get( 2 ) );
            }
            else
            {
                throw new IllegalStateException(
                    "Unsupported number of shift phases: " +
                        shiftPhases.size() );
            }
        }

        return shifter;
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class PhaseInfo
    {
        public final int count;
        public final int fromByte;
        public final int fromBit;
        public final int toByte;
        public final int toBit;

        public PhaseInfo( int count, int fromByte, int fromBit, int toByte,
            int toBit )
        {
            this.count = count;
            this.fromByte = fromByte;
            this.fromBit = fromBit;
            this.toByte = toByte;
            this.toBit = toBit;
        }

        @Override
        public String toString()
        {
            return String.format( "(%d, %d:%d, %d:%d)", count, fromByte,
                fromBit, toByte, toBit );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static class ByteBitShifter implements IBitShifter
    {
        @Override
        public void shift( BitBuffer from, BitBuffer to, int byteCount )
        {
            int fi = from.getByte();
            int ti = to.getByte();

            for( int i = 0; i < byteCount; i++ )
            {
                to.buffer[ti++] = from.buffer[fi++];
            }

            from.setPosition( fi, 0 );
            to.setPosition( ti, 0 );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static class TwoPhaseBitShifter implements IBitShifter
    {
        private final IBitShiftPhase phase1;
        private final IBitShiftPhase phase2;

        public TwoPhaseBitShifter( IBitShiftPhase phase1,
            IBitShiftPhase phase2 )
        {
            this.phase1 = phase1;
            this.phase2 = phase2;
        }

        @Override
        public void shift( BitBuffer from, BitBuffer to, int byteCount )
        {
            byte [] f = from.buffer;
            byte [] t = to.buffer;
            int fi = from.getByte();
            int ti = to.getByte();

            for( int i = 0; i < byteCount; i++ )
            {
                phase1.shift( f, t, fi, ti );
                phase2.shift( f, t, fi++, ti++ );
            }

            from.setPosition( from.getByte() + byteCount, from.getBit() );
            to.setPosition( to.getByte() + byteCount, to.getBit() );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static class ThreePhaseBitShifter implements IBitShifter
    {
        private final IBitShiftPhase phase1;
        private final IBitShiftPhase phase2;
        private final IBitShiftPhase phase3;

        public ThreePhaseBitShifter( IBitShiftPhase phase1,
            IBitShiftPhase phase2, IBitShiftPhase phase3 )
        {
            this.phase1 = phase1;
            this.phase2 = phase2;
            this.phase3 = phase3;
        }

        @Override
        public void shift( BitBuffer from, BitBuffer to, int byteCount )
        {
            byte [] f = from.buffer;
            byte [] t = to.buffer;

            int fi = from.getByte();
            int ti = to.getByte();

            for( int i = 0; i < byteCount; i++ )
            {
                phase1.shift( f, t, fi, ti );
                phase2.shift( f, t, fi, ti );
                phase3.shift( f, t, fi++, ti++ );
            }

            from.setPosition( from.getByte() + byteCount, from.getBit() );
            to.setPosition( to.getByte() + byteCount, to.getBit() );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static interface IBitShiftPhase
    {
        public void shift( byte [] from, byte [] to, int fromIdx, int toIdx );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class LeftBitShiftPhase implements IBitShiftPhase
    {
        private final int fromMask;
        private final int toMask;
        private final int fromIdx;
        private final int toIdx;
        private final int shift;

        public LeftBitShiftPhase( int fromMask, int toMask, int fromIdx,
            int toIdx, int shift )
        {
            this.fromMask = fromMask;
            this.toMask = toMask;

            this.fromIdx = fromIdx;
            this.toIdx = toIdx;

            this.shift = shift;
        }

        @Override
        public void shift( byte [] from, byte [] to, int fi, int ti )
        {
            int v = from[fi + fromIdx];

            v &= fromMask;
            v <<= shift;

            int dstIdx = ti + toIdx;
            to[dstIdx] &= toMask;
            to[dstIdx] |= v;
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class RightBitShiftPhase implements IBitShiftPhase
    {
        private final int fromMask;
        private final int toMask;
        private final int fromIdx;
        private final int toIdx;
        private final int shift;

        public RightBitShiftPhase( int fromMask, int toMask, int fromIdx,
            int toIdx, int shift )
        {
            this.fromMask = fromMask;
            this.toMask = toMask;

            this.fromIdx = fromIdx;
            this.toIdx = toIdx;

            this.shift = shift;
        }

        @Override
        public void shift( byte [] from, byte [] to, int fi, int ti )
        {
            int v = from[fi + fromIdx];

            v &= fromMask;
            v >>>= shift;

            int dstIdx = ti + toIdx;
            to[dstIdx] &= toMask;
            to[dstIdx] |= v;
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class NoBitShiftPhase implements IBitShiftPhase
    {
        private final int fromMask;
        private final int toMask;
        private final int fromIdx;
        private final int toIdx;

        public NoBitShiftPhase( int fromMask, int toMask, int fromIdx,
            int toIdx )
        {
            this.fromMask = fromMask;
            this.toMask = toMask;

            this.fromIdx = fromIdx;
            this.toIdx = toIdx;
        }

        @Override
        public void shift( byte [] from, byte [] to, int fi, int ti )
        {
            int v = from[fi + fromIdx];
            v &= fromMask;

            int dstIdx = ti + toIdx;
            to[dstIdx] &= toMask;
            to[dstIdx] |= v;
        }
    }
}
