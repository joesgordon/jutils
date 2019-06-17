package org.jutils.chart.model;

import java.io.File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Series
{
    /**  */
    public String name;
    /**  */
    public String resource;
    /**  */
    public boolean visible;
    /**  */
    public boolean isPrimaryDomain;
    /**  */
    public boolean isPrimaryRange;

    /**  */
    public final MarkerStyle marker;
    /**  */
    public final MarkerStyle highlight;
    /**  */
    public final LineStyle line;

    /**  */
    public final ISeriesData<?> data;

    /***************************************************************************
     * @param data
     **************************************************************************/
    public Series( ISeriesData<?> data )
    {
        this.data = data;

        this.visible = true;
        this.isPrimaryDomain = true;
        this.isPrimaryRange = true;

        this.marker = new MarkerStyle();
        this.highlight = new MarkerStyle();
        this.line = new LineStyle();

        // if( data.getCount() == 0 )
        // {
        // throw new IllegalArgumentException( "The series contains no data" );
        // }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Interval calcDomainSpan()
    {
        Double min = null;
        Double max = null;

        for( IDataPoint p : data )
        {
            if( !p.isHidden() )
            {
                if( min == null || max == null )
                {
                    min = p.getX();
                    max = p.getX();
                }
                else
                {
                    min = Math.min( min, p.getX() );
                    max = Math.max( max, p.getX() );
                }
            }
        }

        if( min == null || max == null )
        {
            return null;
        }

        return new Interval( min, max );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Interval calcRangeSpan()
    {
        Double min = null;
        Double max = null;

        for( IDataPoint p : data )
        {
            if( !p.isHidden() )
            {
                if( min == null || max == null )
                {
                    min = p.getY();
                    max = p.getY();
                }
                else
                {
                    min = Math.min( min, p.getY() );
                    max = Math.max( max, p.getY() );
                }
            }
        }

        if( min == null || max == null )
        {
            return null;
        }

        return new Interval( min, max );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getResourceFile()
    {
        return resource == null ? null : new File( resource );
    }
}
