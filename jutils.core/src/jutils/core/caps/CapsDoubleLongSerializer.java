package jutils.core.caps;

import jutils.core.io.BitsReader;
import jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class CapsDoubleLongSerializer implements IStdSerializer<Double, Long>
{
    /**  */
    private final BitsReader signReader;
    /**  */
    private final BitsReader manReader;
    /**  */
    private final BitsReader expReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public CapsDoubleLongSerializer()
    {
        this.signReader = new BitsReader( 47, 47 );
        this.manReader = new BitsReader( 8, 46 );
        this.expReader = new BitsReader( 0, 7 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Double read( Long value )
    {
        boolean sign = signReader.read( value ) == 1L;
        long exponent = expReader.read( value );
        long mantissa = manReader.read( value );

        return CapsDouble.calcValue( sign, exponent, mantissa );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Double item, Long resource )
    {
        // TODO Auto-generated method stub
    }
}
