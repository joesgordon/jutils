package jutils.iris.albums;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IReferenceStream;
import jutils.iris.colors.IColorizer;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RastersFileAlbum implements IRasterAlbum
{
    /**  */
    private IColorizer colorizer;
    /**  */
    private IReferenceStream<IRaster> rasters;

    /***************************************************************************
     * @param colorizer
     * @param config
     **************************************************************************/
    public RastersFileAlbum( IColorizer colorizer )
    {
        this.colorizer = colorizer;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRasterCount()
    {
        return ( int )rasters.getCount();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IRaster getRaster( int index )
    {
        // TODO Auto-generated method stub
        try
        {
            return rasters.read( index );
        }
        catch( IOException | ValidationException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IColorizer getColorizer()
    {
        return colorizer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
