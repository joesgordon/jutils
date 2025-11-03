package jutils.core.io.cksum;

import jutils.core.INamedItem;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.LogUtils;
import jutils.core.utils.BitMasks;

/*******************************************************************************
 *
 ******************************************************************************/
public class Crc16 implements ITierPrinter, IChecksum
{
    /**  */
    public static final int TABLE_SIZE = 256;

    /**  */
    public final short [] table;
    /**  */
    private final Crc16Config config;

    /**  */
    private short value;

    /***************************************************************************
     * @param polynomial
     * @param initial
     * @param reflectIn
     * @param reflectOut
     * @param xorout
     **************************************************************************/
    public Crc16( short polynomial, short initial, boolean reflectIn,
        boolean reflectOut, short xorout )
    {
        this( new Crc16Config( polynomial, initial, reflectIn, reflectOut,
            xorout ) );

        this.value = initial;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public Crc16( Crc16Config config )
    {
        this.table = createTable( config.polynomial );
        this.config = new Crc16Config( config );
        this.value = config.initial;
    }

    /***************************************************************************
     * @param polynomial
     * @param initial
     * @param reflectIn
     * @param reflectOut
     * @param xorout
     * @return
     **************************************************************************/
    private static Crc16 create( int polynomial, int initial, boolean reflectIn,
        boolean reflectOut, int xorout )
    {
        return new Crc16( ( short )polynomial, ( short )initial, reflectIn,
            reflectOut, ( short )xorout );
    }

    /***************************************************************************
     * @param polynomial
     * @param reflectIn
     * @param reflectOut
     * @return
     **************************************************************************/
    public static short [] createTable( int polynomial )
    {
        short [] table = new short[TABLE_SIZE];
        int mask = ( int )BitMasks.getBitMask( 15 );

        for( int i = 0; i < table.length; i++ )
        {
            int crc = i;

            for( int bit = 0; bit < 16; bit++ )
            {
                if( ( crc & mask ) != 0 )
                {
                    crc = ( crc << 1 ) ^ polynomial;
                }
                else
                {
                    crc <<= 1;
                }
            }

            table[i] = ( short )crc;
        }

        return table;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static int reverseBits8( int value )
    {
        value = ( ( value & 0xAA ) >>> 1 ) | ( ( value & 0x55 ) << 1 );
        value = ( ( value & 0xCC ) >>> 2 ) | ( ( value & 0x33 ) << 2 );
        value = ( ( value & 0xF0 ) >>> 4 ) | ( ( value & 0x0F ) << 4 );

        return value;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static int reverseBits16( int value )
    {
        value = ( ( value & 0xAAAA ) >>> 1 ) | ( ( value & 0x5555 ) << 1 );
        value = ( ( value & 0xCCCC ) >>> 2 ) | ( ( value & 0x3333 ) << 2 );
        value = ( ( value & 0xF0F0 ) >>> 4 ) | ( ( value & 0x0F0F ) << 4 );
        value = ( value >>> 8 ) | ( value << 8 );

        return value;
    }

    /***************************************************************************
     * @param data
     * @return
     **************************************************************************/
    public short calculate( byte [] data )
    {
        return calculate( data, 0, data.length );
    }

    /***************************************************************************
     * @param bytes
     * @param start
     * @param length
     * @return
     **************************************************************************/
    public short calculate( byte [] bytes, int start, int length )
    {
        reset();

        update( bytes, start, length );

        int crc = value & 0xFFFF;

        crc = config.reflectOut ? reverseBits16( crc ) : crc;
        crc ^= config.xorout;

        value = ( short )( crc );

        return value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void update( byte [] bytes )
    {
        update( bytes, 0, bytes.length );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void update( byte [] bytes, int start, int length )
    {
        int crc = value & 0xFFFF;

        for( int i = 0; i < length; i++ )
        {
            int dataIndex = i + start;
            int dataValue = bytes[dataIndex];

            dataValue = config.reflectIn ? reverseBits8( dataValue )
                : dataValue;

            int crcHigh = crc >>> 8;
            int crcLow = crc & 0xFF;

            int tableIndex = ( dataValue ^ crcHigh ) & 0xFF;
            // int tableIndex = ( dataValue ^ crcLow ) & 0xFF;

            int tableValue = table[tableIndex] & 0xFFFF;

            crc = tableValue ^ ( crcLow << 8 );
            // crc = tableValue ^ crcHigh;

            crc &= 0xFFFF;

            // LogUtils.printDebug( "%d: 0x%02X (%d, 0x%04X) => 0x%04X",
            // dataIndex,
            // dataValue, tableIndex, tableValue, crc );
        }

        this.value = ( short )( crc );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void reset()
    {
        this.value = config.initial;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getChecksum()
    {
        byte [] b = new byte[4];
        int shift;

        for( int i = 0; i < b.length; ++i )
        {
            shift = 8 * ( b.length - 1 - i );
            b[i] = ( byte )( ( value >> shift ) & 0xFF );
        }

        return b;
    }

    /***************************************************************************
     * @param data
     * @return
     **************************************************************************/
    public short calcDirect( byte [] data )
    {
        return calcDirect( data, 0, data.length );
    }

    /***************************************************************************
     * @param table
     * @param data
     * @param start
     * @param length
     * @return
     **************************************************************************/
    public short calcDirect( byte [] data, int start, int length )
    {
        int crc = ( short )( value & 0xFFFF );

        for( int i = 0; i < length; i++ )
        {
            int dataIndex = i + start;
            int dataValue = data[dataIndex] & 0xFF;

            dataValue = config.reflectIn ? reverseBits8( dataValue )
                : dataValue;

            crc = crc ^ ( dataValue << 8 );

            for( int b = 0; b < 8; b++ )
            {
                boolean isHighBitSet = ( crc & 0x8000 ) != 0;
                if( isHighBitSet )
                {
                    crc = ( crc << 1 ) ^ config.polynomial;
                }
                else
                {
                    crc = crc << 1;
                }
            }
        }

        crc = config.reflectOut ? reverseBits16( crc ) : crc;
        crc ^= config.xorout;

        value = ( short )( crc );

        return value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printHexField( "Checksum", value );
        printer.printTier( "Configuration", config );
        printer.printFieldValuesHex( "Table", 8, table );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        byte [] data = "123456789".getBytes();

        Crc16 arc = Crc16.create( 0x8005, 0x0000, true, true, 0x0000 );
        short arcSum = ( short )0xBB3D;
        Crc16 augCcitt = Crc16.create( 0x1021, 0x1D0F, false, false, 0x0000 );
        short argCcittSum = ( short )0xE5CC;
        Crc16 buypass = Crc16.create( 0x8005, 0x0000, false, false, 0x0000 );
        short buypassSum = ( short )0xFEE8;
        Crc16 ccittFalse = Crc16.create( 0x1021, 0xFFFF, false, false, 0x0000 );
        short ccittFalseSum = 0x29B1;
        Crc16 dnp = Crc16.create( 0x3D65, 0x0000, true, true, 0xFFFF );
        short dnpSum = ( short )0xEA82;
        Crc16 en13757 = Crc16.create( 0x3D65, 0x0000, false, false, 0xFFFF );
        short en13757Sum = ( short )0xC2B7;

        String [] names = new String[] { "ARC", "AUG-CCITT", "BUYPASS",
            "CCITT-FALSE", "DNP", "EN-13757" };
        Crc16 [] crcs = new Crc16[] { arc, augCcitt, buypass, ccittFalse, dnp,
            en13757 };
        short [] expecteds = new short[] { arcSum, argCcittSum, buypassSum,
            ccittFalseSum, dnpSum, en13757Sum };

        FieldPrinter p = new FieldPrinter();

        p.printFieldValues( "Data", data );

        for( int i = 0; i < crcs.length; i++ )
        {
            String name = names[i];
            Crc16 crc = crcs[i];
            short expected = expecteds[i];
            short direct;

            crc.calculate( data );
            crc.reset();
            direct = crc.calcDirect( data );

            FieldPrinter cp = p.printTier( name, crc );

            cp.printHexField( "Expected", expected );
            cp.printHexField( "Direct", direct );
        }

        LogUtils.print( p.toString() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class Crc16Config implements ITierPrinter
    {
        /**  */
        public short polynomial;
        /**  */
        public short initial;
        /**  */
        public boolean reflectIn;
        /**  */
        public boolean reflectOut;
        /**  */
        public short xorout;

        /**
         * 
         */
        public Crc16Config()
        {
            this( ( short )0, ( short )0, false, false, ( short )0 );
        }

        /**
         * @param polynomial
         * @param initial
         * @param reflectIn
         * @param reflectOut
         * @param xorout
         */
        public Crc16Config( short polynomial, short initial, boolean reflectIn,
            boolean reflectOut, short xorout )
        {
            this.polynomial = polynomial;
            this.initial = initial;
            this.reflectIn = reflectIn;
            this.reflectOut = reflectOut;
            this.xorout = xorout;
        }

        /**
         * @param polynomial
         * @param initial
         * @param reflectIn
         * @param reflectOut
         * @param xorout
         */
        public Crc16Config( int polynomial, int initial, boolean reflectIn,
            boolean reflectOut, int xorout )
        {
            this( ( short )polynomial, ( short )initial, reflectIn, reflectOut,
                ( short )xorout );
        }

        /**
         * @param config
         */
        public Crc16Config( Crc16Config config )
        {
            this.polynomial = config.polynomial;
            this.initial = config.initial;
            this.reflectIn = config.reflectIn;
            this.reflectOut = config.reflectOut;
            this.xorout = config.xorout;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void printFields( FieldPrinter printer )
        {
            printer.printHexField( "Polynomial", polynomial );
            printer.printHexField( "Initial", initial );
            printer.printField( "Reflect In", reflectIn );
            printer.printField( "Reflect Out", reflectOut );
            printer.printHexField( "XOR Out", xorout );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum Crc16Algorithm implements INamedItem
    {
        /**  */
        ARC( "ARC" ),
        /**  */
        CCITT_FALSE( "CCITT-FALSE" ),
        /**  */
        MODBUS( "MODBUS" ),;

        /**  */
        public final String name;

        /**
         * @param name
         */
        private Crc16Algorithm( String name )
        {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }

        /**
         * @return
         */
        public Crc16Config getConfig()
        {
            switch( this )
            {
                case ARC:
                    return new Crc16Config( 0x8005, 0x0000, true, true,
                        0x0000 );

                case CCITT_FALSE:
                    return new Crc16Config( 0x1021, 0xFFFF, false, false,
                        0x0000 );

                case MODBUS:
                    return new Crc16Config( 0x8005, 0xffff, true, true,
                        0x0000 );
            }

            throw new IllegalStateException( "Not implemented: " + name );
        }
    }
}
