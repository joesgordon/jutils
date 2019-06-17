package org.jutils.ui.hex;

public enum HexBufferSize
{
    VERY_SMALL( 0x400, "Xtra-Small (1 kb)" ),
    SMALL( 0x10000, "Small (64 kb)" ),
    MEDIUM( 0x80000, "Medium (512 kb)" ),
    LARGE( 0x100000, "Large (1 Mb)" );

    /** The size of the buffer in bytes. */
    public final int size;
    /** The text description of the size. */
    public final String desc;

    private HexBufferSize( int size, String desc )
    {
        this.size = size;
        this.desc = desc;
    }

    @Override
    public String toString()
    {
        return desc;
    }
}
