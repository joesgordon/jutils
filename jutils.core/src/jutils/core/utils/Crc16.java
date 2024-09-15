package jutils.core.utils;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.LogUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public class Crc16 implements ITierPrinter
{
    /**  */
    public static final int TABLE_SIZE = 256;

    /**  */
    public final short [] table;
    /**  */
    public final short polynomial;
    /**  */
    public final short initial;
    /**  */
    public final boolean reflectIn;
    /**  */
    public final boolean reflectOut;
    /**  */
    public final short xorout;

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
        this.table = createTable( polynomial );
        this.polynomial = polynomial;
        this.initial = initial;
        this.reflectIn = reflectIn;
        this.reflectOut = reflectOut;
        this.xorout = xorout;

        this.value = initial;
    }

    /***************************************************************************
     * @param polynomial
     * @param initial
     * @param reflectIn
     * @param reflectOut
     * @param xorout
     * @return
     **************************************************************************/
    public static Crc16 create( int polynomial, int initial, boolean reflectIn,
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
     * @param table
     * @param src
     * @return
     **************************************************************************/
    public short calculate( byte [] data )
    {
        return calculate( data, 0, data.length );
    }

    /***************************************************************************
     * @param table
     * @param data
     * @param start
     * @param length
     * @return
     **************************************************************************/
    public short calculate( byte [] data, int start, int length )
    {
        int crc = value & 0xFFFF;

        for( int i = 0; i < length; i++ )
        {
            int dataIndex = i + start;
            int dataValue = data[dataIndex];

            dataValue = reflectIn ? reverseBits8( dataValue ) : dataValue;

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

        crc = reflectOut ? reverseBits16( crc ) : crc;
        crc ^= xorout;

        value = ( short )( crc );

        return value;
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

            dataValue = reflectIn ? reverseBits8( dataValue ) : dataValue;

            crc = crc ^ ( dataValue << 8 );

            for( int b = 0; b < 8; b++ )
            {
                boolean isHighBitSet = ( crc & 0x8000 ) != 0;
                if( isHighBitSet )
                {
                    crc = ( crc << 1 ) ^ polynomial;
                }
                else
                {
                    crc = crc << 1;
                }
            }
        }

        crc = reflectOut ? reverseBits16( crc ) : crc;
        crc ^= xorout;

        value = ( short )( crc );

        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.value = initial;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printFieldValuesHex( "Table", 8, table );
        printer.printHexField( "Polynomial", polynomial );
        printer.printHexField( "Initial", initial );
        printer.printField( "Reflect In", reflectIn );
        printer.printField( "Reflect Out", reflectOut );
        printer.printHexField( "XOR Out", xorout );
        printer.printHexField( "Checksum", value );
    }

    /**
     * @param args
     */
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
}
