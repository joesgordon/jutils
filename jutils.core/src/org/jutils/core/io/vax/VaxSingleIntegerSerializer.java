package org.jutils.core.io.vax;

import org.jutils.core.io.BitsReader;
import org.jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class VaxSingleIntegerSerializer
    implements IStdSerializer<Double, Integer>
{
    /**  */
    private final BitsReader signReader;
    /**  */
    private final BitsReader expReader;
    /**  */
    private final BitsReader manReader;

    /***************************************************************************
     * 
     **************************************************************************/
    public VaxSingleIntegerSerializer()
    {
        this.signReader = new BitsReader( 31, 31 );
        this.expReader = new BitsReader( 23, 30 );
        this.manReader = new BitsReader( 0, 22 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Double read( Integer value )
    {
        boolean sign = signReader.read( value ) == 1L;
        int exponent = expReader.read( value );
        int mantissa = manReader.read( value );

        return VaxSingle.calcVaxValue( sign, exponent, mantissa );
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
