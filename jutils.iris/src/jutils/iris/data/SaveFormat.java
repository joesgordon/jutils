package jutils.iris.data;

import jutils.core.INamedValue;

/***************************************************************************
 * 
 **************************************************************************/
public enum SaveFormat implements INamedValue
{
    /** Raw file format */
    RAW( 0, "Raw" ),
    /** JPEG file format */
    JPEG( 1, "JPEG" ),
    /** PNG file format */
    PNG( 2, "PNG" ),
    /** Bitmap file format */
    BMP( 2, "Bitmap" ),
    /** TIFF file format */
    TIFF( 2, "TIFF" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private SaveFormat( int value, String name )
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
     * @param id
     * @return
     **************************************************************************/
    public static SaveFormat fromId( byte id )
    {
        return INamedValue.fromValue( id, values(), null );
    }
}
