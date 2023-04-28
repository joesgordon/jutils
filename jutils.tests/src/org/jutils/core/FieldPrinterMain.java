package org.jutils.core;

import org.jutils.core.io.FieldPrinter;

/*******************************************************************************
 *
 ******************************************************************************/
public class FieldPrinterMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static final void main( String [] args )
    {
        byte [] bytes = { 0, 1, 2, 3, 44, 55, 66, 77, ( byte )888,
            ( byte )999 };
        short [] shorts = { 0, 1, 2, 3, 44, 55, 66, 77, 888, 999 };
        int [] ints = { 0, 1, 2, 3, 44, 55, 66, 77, 888, 999 };
        long [] longs = { 0, 1, 2, 3, 44, 55, 66, 77, 888, 999 };

        float [] floats = { 0.7894237f, 1.7894237f, 2.7894237f, 3.7894237f,
            44.7894237f, 55.7894237f, 66.7894237f, 77.7894237f, 888.7894237f,
            999.7894237f };
        double [] doubles = { 0.7894237, 1.7894237, 2.7894237, 3.7894237,
            44.7894237, 55.7894237, 66.7894237, 77.7894237, 888.7894237,
            999.7894237 };

        FieldPrinter p = new FieldPrinter();

        p.printFieldValues( "bytes", bytes );
        p.printFieldValues( "bytes", 4, bytes );
        p.printFieldValues( "shorts", shorts );
        p.printFieldValues( "shorts", 5, shorts );
        p.printFieldValues( "ints", ints );
        p.printFieldValues( "ints", 6, ints );
        p.printFieldValues( "longs", longs );
        p.printFieldValues( "longs", 2, longs );

        p.printFieldValues( "floats", floats );
        p.printFieldValues( "floats", 3, floats );
        p.printFieldValues( "floats", 5, 2, floats );
        p.printFieldValues( "floats", 3, 5, 2, floats );

        p.printFieldValues( "doubles", doubles );
        p.printFieldValues( "doubles", 4, doubles );
        p.printFieldValues( "doubles", 5, 2, doubles );
        p.printFieldValues( "doubles", 5, 2, 4, doubles );

        System.out.println( p.toString() );
    }
}
