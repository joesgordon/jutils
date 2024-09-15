package jutils.iris.data;

import java.awt.image.BufferedImage;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum BufferedImageType implements INamedValue
{
    /** @see BufferedImage#TYPE_CUSTOM */
    CUSTOM( 0, "Custom" ),
    /** @see BufferedImage#TYPE_INT_RGB */
    RGB_INT( 1, "RGB (32-bit)" ),
    /** @see BufferedImage#TYPE_INT_ARGB */
    ARGB_INT( 2, "ARGB (32-bit)" ),
    /** @see BufferedImage#TYPE_INT_ARGB_PRE */
    ARGB_PRE_INT( 3, "ARGB (32-bit, Pre-multiplied alpha)" ),
    /** @see BufferedImage#TYPE_INT_BGR */
    BGR_INT( 4, "BGR (32-bit)" ),
    /** @see BufferedImage#TYPE_3BYTE_BGR */
    BGR_3BYTE( 5, "BGR (24-bit)" ),
    /** @see BufferedImage#TYPE_4BYTE_ABGR */
    ABGR_4BYTE( 6, "ABGR (32-bit)" ),
    /** @see BufferedImage#TYPE_4BYTE_ABGR_PRE */
    ABGR_PRE_4BYTE( 7, "ABGR (32-bit, Pre-multiplied alpha)" ),
    /** @see BufferedImage#TYPE_USHORT_565_RGB */
    RGB_565_SHORT( 8, "RGB-565 (16-bit)" ),
    /** @see BufferedImage#TYPE_USHORT_555_RGB */
    RGB_555_SHORT( 9, "RGB-555 (16-bit)" ),
    /** @see BufferedImage#TYPE_BYTE_GRAY */
    MONO_BYTE( 10, "Mono (8-bit)" ),
    /** @see BufferedImage#TYPE_USHORT_GRAY */
    MONO_SHORT( 11, "Mono (16-bit)" ),
    /** @see BufferedImage#TYPE_BYTE_BINARY */
    BINARY_BYTE( 12, "Binary (8-bit)" ),
    /** @see BufferedImage#TYPE_BYTE_INDEXED */
    INDEXED_BYTE( 13, "Indexed (8-bit)" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private BufferedImageType( int value, String name )
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
    public static BufferedImageType fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }
}
