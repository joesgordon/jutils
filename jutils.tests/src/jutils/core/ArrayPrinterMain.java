package jutils.core;

import jutils.core.ArrayPrinter;
import jutils.core.io.LogUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public class ArrayPrinterMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static final void main( String [] args )
    {
        int len = 4;

        byte [] bytes = { 0, 1, 2, 3, 44, 55, 66 };
        short [] shorts = { 0, 1, 2, 3, 44, 55, 66 };
        int [] ints = { 0, 1, 2, 3, 44, 55, 66 };
        long [] longs = { 0, 1, 2, 3, 44, 55, 66 };

        float [] floats = { 0.7894237f, 1.7894237f, 2.7894237f, 3.7894237f,
            44.7894237f, 55.7894237f, 66.7894237f };
        double [] doubles = { 0.7894237, 1.7894237, 2.7894237, 3.7894237,
            44.7894237, 55.7894237, 66.7894237 };

        LogUtils.printDebug( "Bytes" );
        LogUtils.printDebug( ArrayPrinter.toString( bytes ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( bytes, ", ", 0, bytes.length ) );
        LogUtils.printDebug( ArrayPrinter.toString( bytes, ", ", 1, len ) );

        LogUtils.printDebug( "Shorts" );
        LogUtils.printDebug( ArrayPrinter.toString( shorts ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( shorts, ", ", 0, shorts.length ) );
        LogUtils.printDebug( ArrayPrinter.toString( shorts, ", ", 1, len ) );

        LogUtils.printDebug( "Ints" );
        LogUtils.printDebug( ArrayPrinter.toString( ints ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( ints, ", ", 0, ints.length ) );
        LogUtils.printDebug( ArrayPrinter.toString( ints, ", ", 1, len ) );

        LogUtils.printDebug( "Longs" );
        LogUtils.printDebug( ArrayPrinter.toString( longs ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( longs, ", ", 0, longs.length ) );
        LogUtils.printDebug( ArrayPrinter.toString( longs, ", ", 1, len ) );

        LogUtils.printDebug( "Floats" );
        LogUtils.printDebug( ArrayPrinter.toString( floats ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( floats, ", ", 0, floats.length ) );
        LogUtils.printDebug( ArrayPrinter.toString( floats, ", ", 1, 4 ) );

        LogUtils.printDebug( ArrayPrinter.toString( doubles ), 5, 1 );
        LogUtils.printDebug(
            ArrayPrinter.toString( doubles, ", ", 0, doubles.length, 5, 1 ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( doubles, ", ", 1, 4, 5, 1 ) );

        LogUtils.printDebug( "Doubles" );
        LogUtils.printDebug( ArrayPrinter.toString( doubles ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( doubles, ", ", 0, doubles.length ) );
        LogUtils.printDebug( ArrayPrinter.toString( doubles, ", ", 1, 4 ) );

        LogUtils.printDebug( ArrayPrinter.toString( doubles ), 5, 1 );
        LogUtils.printDebug(
            ArrayPrinter.toString( doubles, ", ", 0, doubles.length, 5, 1 ) );
        LogUtils.printDebug(
            ArrayPrinter.toString( doubles, ", ", 1, 4, 5, 1 ) );

        LogUtils.printDebug( "Args" );
        LogUtils.printDebug(
            ArrayPrinter.argsToString( 1, "jfdkls", 5.5, Math.PI ) );
    }
}
