package org.jutils.ui.hex;

import java.io.*;

import org.jutils.io.FileStream;
import org.jutils.io.IStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlockBuffer
{
    /**  */
    private long position;
    /**  */
    private long fileLength;
    /** The size of the buffer to be loaded. */
    private int bufferSize;
    /**  */
    private File file;
    /**  */
    private IStream byteStream;
    /**  */
    private byte [] bytes;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlockBuffer()
    {
        this.position = 0;
        this.fileLength = 0;
        this.bufferSize = HexBufferSize.LARGE.size;
        this.file = null;
        this.byteStream = null;
        this.bytes = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isOpen()
    {
        return file != null;
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void openFile( File file ) throws IOException
    {
        this.file = file;
        if( byteStream != null )
        {
            closeFile();
        }
        byteStream = new FileStream( file, true );
        fileLength = byteStream.getLength();
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void closeFile() throws IOException
    {
        if( byteStream != null )
        {
            byteStream.close();
            byteStream = null;
            position = 0;
            fileLength = 0;
        }
    }

    /***************************************************************************
     * @param pos
     * @return
     * @throws IOException
     **************************************************************************/
    public DataBlock loadBufferAt( long pos ) throws IOException
    {
        this.position = pos;

        int bufLen = ( int )Math.min( bufferSize, fileLength - position );
        bytes = new byte[bufLen];

        // LogUtils.printDebug( "Loading buffer @ " + startOffset + " , " +
        // percent + "%" );

        byteStream.seek( pos );
        byteStream.readFully( bytes );

        return new DataBlock( pos, bytes );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getPreviousPosition()
    {
        long lastOffset = position - ( position % bufferSize ) - bufferSize;
        lastOffset = Math.max( lastOffset, 0 );
        return lastOffset;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getNextPosition()
    {
        return position - ( position % bufferSize ) + bufferSize;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getLastPosition()
    {
        return getBlockStart( fileLength - 1 );
    }

    /***************************************************************************
     * @param offset
     * @return
     **************************************************************************/
    public long getBlockStart( long offset )
    {
        long blockCount = offset / bufferSize;

        return blockCount * bufferSize;
    }

    /***************************************************************************
     * @param position
     * @return
     **************************************************************************/
    public long getBufferStart( long position )
    {
        return ( position / bufferSize ) * bufferSize;
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public long setBufferSize( int size )
    {
        this.bufferSize = size;

        return getBufferStart( position );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getBufferSize()
    {
        return bufferSize;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public long getPositionAt( int index )
    {
        return position + index;
    }

    /***************************************************************************
     * @param position
     * @return
     **************************************************************************/
    public boolean isLoaded( long position )
    {
        if( bytes == null )
        {
            return false;
        }
        return position > this.position &&
            position < this.position + bytes.length;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getLength()
    {
        return fileLength;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getFile()
    {
        return file;
    }

    /***************************************************************************
     * @return
     * @throws FileNotFoundException
     **************************************************************************/
    public IStream openStreamCopy() throws FileNotFoundException
    {
        return file == null ? null : new FileStream( file, true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class DataBlock
    {
        public final long position;
        public final byte [] buffer;

        public DataBlock( long position, byte [] buffer )
        {
            this.position = position;
            this.buffer = buffer;
        }
    }
}
