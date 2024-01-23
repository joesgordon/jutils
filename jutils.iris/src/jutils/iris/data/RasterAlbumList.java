package jutils.iris.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterAlbumList implements IRasterAlbum
{
    /**  */
    private final List<IRaster> rasters;
    /**  */
    private IColorModel colors;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterAlbumList()
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
    public void setColors( IColorModel model )
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
    public IColorModel getColors()
    {
        return colors;
    }
}
