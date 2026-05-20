package jutils.telemetry.ch09.io;

import jutils.core.ValidationException;
import jutils.telemetry.ch09.io.ascii.TmatsParser;
import jutils.telemetry.ch10.CompGen1Body;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsReader
{
    /**  */
    private final TmatsParser asciiParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsReader()
    {
        this.asciiParser = new TmatsParser();
    }

    /***************************************************************************
     * @param body
     * @throws ValidationException
     **************************************************************************/
    public void read( CompGen1Body body ) throws ValidationException
    {
        switch( body.format )
        {
            case ASCII:
                body.tmats = asciiParser.parse( body.setup );
                break;

            case XML:
                break;
        }
    }
}
