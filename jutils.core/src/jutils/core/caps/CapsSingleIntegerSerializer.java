package jutils.core.caps;

import jutils.core.io.BitsReader;
import jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class CapsSingleIntegerSerializer
    implements IStdSerializer<Double, Integer>
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
    public CapsSingleIntegerSerializer()
    {
        this.signReader = new BitsReader( 31, 31 );
        this.manReader = new BitsReader( 8, 30 );
        this.expReader = new BitsReader( 0, 7 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Double read( Integer value )
    {
        boolean sign = signReader.read( value ) == 1L;
        int mantissa = manReader.read( value );
        int exponent = expReader.read( value );

        return CapsSingle.calcValue( sign, exponent, mantissa );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Double item, Integer resource )
    {
        // TODO Auto-generated method stub
    }
}
