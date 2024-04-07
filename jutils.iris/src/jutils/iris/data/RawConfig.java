package jutils.iris.data;

import jutils.core.utils.ByteOrdering;
import jutils.core.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RawConfig
{
    /** Number of bytes to skip at the beginning of the file. */
    public int fileHeaderLen;
    /** Number of bytes before each image. */
    public int imageHeaderLen;
    /** The byte order of each pixel. */
    public ByteOrdering endianness;
    /** The number of images to read. */
    public final Usable<Integer> imageCount;

    /**  */
    public int width;
    /**  */
    public int height;
    /**  */
    public int bitDepth;
    /**  */
    public boolean packed;
    /**  */
    public PixelFormat format;

    /***************************************************************************
     * 
     **************************************************************************/
    public RawConfig()
    {
        this.fileHeaderLen = 0;
        this.imageHeaderLen = 0;
        this.endianness = ByteOrdering.BIG_ENDIAN;
        this.imageCount = new Usable<Integer>( false, 10 );
        this.width = 256;
        this.height = 256;
        this.bitDepth = 8;
        this.packed = false;
        this.format = PixelFormat.MONOCHROME;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public long getOffset( int index )
    {
        return imageHeaderLen + index * getTotalImageSize();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTotalImageSize()
    {
        return imageHeaderLen + getImageSize();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getImageSize()
    {
        if( packed )
        {
            return width * height * ( ( getPixelSize() + 7 ) / 8 );
        }

        return ( width * height * getPixelSize() + 7 ) / 8;
    }

    /***************************************************************************
     * @return the number of bits per pixel
     **************************************************************************/
    public int getPixelSize()
    {
        return format.channelCount * bitDepth;
    }
}
