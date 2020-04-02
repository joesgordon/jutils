package org.jutils.core.time;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MicrosPrinter
{
    /**  */
    private final char [] buffer;

    /***************************************************************************
     * 
     **************************************************************************/
    public MicrosPrinter()
    {
        this.buffer = new char[Long.toString( Long.MIN_VALUE ).length() + 1];
    }

    /***************************************************************************
     * @param microseconds
     * @return
     **************************************************************************/
    public static String toString( long microseconds )
    {
        MicrosPrinter mp = new MicrosPrinter();
        return mp.toSecondsString( microseconds );
    }

    /***************************************************************************
     * @param microseconds
     * @return
     **************************************************************************/
    public String toSecondsString( long microseconds )
    {
        String sec;
        int bidx = buffer.length - 1;
        int sidx;
        int lim = microseconds < 0L ? 1 : 0;

        sec = Long.toString( microseconds );

        sidx = sec.length() - 1;

        for( int i = 0; i < 6; i++ )
        {
            if( sidx < lim )
            {
                buffer[bidx--] = '0';
            }
            else
            {
                buffer[bidx--] = sec.charAt( sidx-- );
            }
        }

        buffer[bidx--] = '.';

        if( sidx < lim )
        {
            buffer[bidx--] = '0';
        }

        while( sidx > -1 )
        {
            buffer[bidx--] = sec.charAt( sidx-- );
        }

        return new String( buffer, bidx + 1, buffer.length - bidx - 1 );
    }
}
