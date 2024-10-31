package jutils.telemetry.io.ch09;

import jutils.core.ValidationException;
import jutils.telemetry.data.ch10.CompGen1Body;
import jutils.telemetry.io.ch09.ascii.TmatsParser;

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
