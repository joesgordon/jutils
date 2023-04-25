package org.jutils.core.vax;

import org.jutils.core.io.BitsReader;
import org.jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class VaxDoubleLongSerializer implements IStdSerializer<Double, Long>
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
    public VaxDoubleLongSerializer()
    {
        this.signReader = new BitsReader( 63, 63 );
        this.expReader = new BitsReader( 55, 62 );
        this.manReader = new BitsReader( 0, 54 );
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

        return VaxDouble.calcVaxValue( sign, exponent, mantissa );
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
