package jutils.serial;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;

/*******************************************************************************
 * 
 ******************************************************************************/
class JniBuffers
{
    /**  */
    private static final int DEFAULT_BUFF_SIZE = 1024;

    /**  */
    private final int bufferSize;
    /**  */
    private final ArrayDeque<ByteBuffer> used;
    /**  */
    private final ArrayDeque<ByteBuffer> unused;

    /***************************************************************************
     * 
     **************************************************************************/
    JniBuffers()
    {
        this( DEFAULT_BUFF_SIZE );
    }

    /***************************************************************************
     * @param bufferSize
     **************************************************************************/
    JniBuffers( int bufferSize )
    {
        this.bufferSize = bufferSize;
        this.used = new ArrayDeque<ByteBuffer>( 16 );
        this.unused = new ArrayDeque<ByteBuffer>( 16 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    synchronized ByteBuffer nextBuffer()
    {
        ByteBuffer buffer = null;
        if( !unused.isEmpty() )
        {
            buffer = unused.pop();
        }

        if( buffer == null )
        {
            buffer = ByteBuffer.allocateDirect( bufferSize );
        }

        used.add( buffer );

        return buffer;
    }

    /***************************************************************************
     * @param buffer
     **************************************************************************/
    synchronized void releaseBuffer( ByteBuffer buffer )
    {
        if( buffer != null )
        {
            used.remove( buffer );
            unused.add( buffer );
        }
    }
}
