package jutils.iris.data;

import jutils.iris.colors.IColorizer;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IRasterAlbum
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRasterCount();

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public IRaster getRaster( int index );

    /***************************************************************************
     * @return
     **************************************************************************/
    public IColorizer getColorizer();

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName();
}