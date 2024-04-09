package jutils.iris.albums;

import java.util.ArrayList;
import java.util.List;

import jutils.iris.colors.IColorizer;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterListAlbum implements IRasterAlbum
{
    /**  */
    public String name;
    /**  */
    private final List<IRaster> rasters;
    /**  */
    private IColorizer colors;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterListAlbum()
    {
        this.rasters = new ArrayList<>();
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void addRaster( IRaster raster )
    {
        rasters.add( raster );
    }

    /***************************************************************************
     * @param model
     **************************************************************************/
    public void setColorizer( IColorizer model )
    {
        this.colors = model;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRasterCount()
    {
        return rasters.size();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IRaster getRaster( int index )
    {
        return rasters.get( index );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IColorizer getColorizer()
    {
        return colors;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
