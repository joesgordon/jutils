package jutils.telemetry.data.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * As defined in chapter 11 section 11.2.1.1.g.
 ******************************************************************************/
public enum SecondaryHeaderTimeFormat implements INamedValue
{
    /**
     * Chapter 4 binary weighted 48-bit time format. The two LSBs of the 64-bit
     * packet secondary header time and IPTS shall be zero-filled.
     */
    CHAPTER4( 0, "Chapter 4 Binary Weighted 48-bit" ),
    /**
     * IEEE 1588 time format. The packet secondary header time and each IPTS
     * shall contain a 64-bit timestamp represented in accordance with (IAW) the
     * time representation type as specified by IEEE STD 1588-2008.1 The 32 bits
     * indicating seconds shall be placed into the MSLW portion of the secondary
     * header and the 32 bits indicating nanoseconds shall be placed into the
     * LSLW portion.
     */
    IEEE_1588( 1, "IEEE 1588" ),
    /**
     * 64-bit binary extended relative time counter (ERTC) with 1-nanosecond
     * resolution. The counter shall be derived from a free-running 1-gigahertz
     * (GHz) clock - similar to the RTC described below - just with higher
     * resolution. When this option is used, the 10-megahertz (MHz) RTC shall be
     * synchronized with the ERTC (RTC = ERTC/100).
     */
    ERTC( 2, "64-bit Binary ERTC" ),
    /**  */
    RESERVED( 3, "Reserved" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private SecondaryHeaderTimeFormat( int value, String name )
    {
        this.value = value;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static SecondaryHeaderTimeFormat fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static SecondaryHeaderTimeFormat fromValue( byte value )
    {
        return fromValue( value & 0xFF );
    }
}
