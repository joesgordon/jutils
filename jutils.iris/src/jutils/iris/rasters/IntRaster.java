package jutils.iris.rasters;

import jutils.iris.data.IRaster;
import jutils.iris.data.RasterConfig;

public class IntRaster implements IRaster
{
    public final int [] pixels;

    public IntRaster( RasterConfig config )
    {
        this.pixels = new int[config.getPixelCount()];
    }

    @Override
    public RasterConfig getConfig()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getPixel( int p )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setPixel( int p, long value )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public long getPixel( int x, int y )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setPixel( int x, int y, long value )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getChannel( int p, int c )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setChannel( int p, int c, int value )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getChannel( int x, int y, int c )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setChannel( int x, int y, int c, int value )
    {
        // TODO Auto-generated method stub

    }
}
