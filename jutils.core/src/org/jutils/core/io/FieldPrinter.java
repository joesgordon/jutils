package org.jutils.core.io;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FieldPrinter
{
    /**  */
    public final StringPrintStream stream;
    /**  */
    public final String indentString;
    /**  */
    public final String tierString;

    /***************************************************************************
     * 
     **************************************************************************/
    @SuppressWarnings( "resource")
    public FieldPrinter()
    {
        this( new StringPrintStream() );
    }

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public FieldPrinter( StringPrintStream stream )
    {
        this( stream, "    " );
    }

    /***************************************************************************
     * @param stream
     * @param indentString
     **************************************************************************/
    public FieldPrinter( StringPrintStream stream, String indentString )
    {
        this.stream = stream;
        this.indentString = indentString;
        this.tierString = "";
    }

    /***************************************************************************
     * @param stream
     * @param indentString
     * @param tierString
     **************************************************************************/
    private FieldPrinter( StringPrintStream stream, String indentString,
        String tierString )
    {
        this.stream = stream;
        this.indentString = indentString;
        this.tierString = tierString;
    }

    /***************************************************************************
     * @param tierName
     * @return
     **************************************************************************/
    public FieldPrinter createTier( String tierName )
    {
        stream.println( "%s%s:", tierString, tierName );

        return new FieldPrinter( stream, indentString,
            tierString + indentString );
    }

    /***************************************************************************
     * @param tierName
     * @param tier
     **************************************************************************/
    public void printTier( String tierName, ITierPrinter tier )
    {
        FieldPrinter p = createTier( tierName );

        tier.printFields( p );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, String value )
    {
        stream.println( "%s%s: %s", tierString, name, value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, Object value )
    {
        printField( name, value.toString() );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, boolean value )
    {
        printField( name, value ? "true" : "false" );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, char value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, byte value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, short value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, int value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, long value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     * @param digits
     **************************************************************************/
    public void printField( String name, long value, int digits )
    {
        String fmt = "0x%" + digits + "X";
        long mask = 0;

        if( digits == 16 )
        {
            mask = -1;
        }
        else if( digits > 0 && digits < 16 )
        {
            mask = ( 1L << ( digits * 4 ) ) - 1;
        }
        else
        {
            throw new IllegalArgumentException(
                "Invalid number of digits: " + digits );
        }

        value = value & mask;

        printField( name, String.format( fmt, value ) );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, float value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     * @param width
     * @param precision
     **************************************************************************/
    public void printField( String name, float value, int width, int precision )
    {
        printField( name, ( double )value, width, precision );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, double value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     * @param width
     * @param precision
     **************************************************************************/
    public void printField( String name, double value, int width,
        int precision )
    {
        String fmt = "% " + width + "." + precision + "f";
        printField( name, String.format( fmt, value ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return stream.toString();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ITierPrinter
    {
        /**
         * @param printer
         */
        public void printFields( FieldPrinter printer );
    }
}
