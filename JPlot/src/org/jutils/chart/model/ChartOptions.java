package org.jutils.chart.model;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartOptions
{
    /**  */
    public static final String NINE_NINES_SENTINEL_VALUE = "999999999.999999999";

    /**  */
    public boolean antialias;
    /**  */
    public boolean textAntiAlias;
    /**  */
    public boolean gridlinesVisible;
    /**  */
    public PointRemovalMethod removalMethod;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartOptions()
    {
        this.antialias = true;
        this.textAntiAlias = true;
        this.gridlinesVisible = true;
    }

    /***************************************************************************
     * Defines what happens to points that are removed.
     **************************************************************************/
    public static enum PointRemovalMethod implements INamedItem
    {
        /** Removed points completely. */
        DELETE( "Delete" ),
        /** Replaces removed points with a flag value of 999999999.999999999. */
        NINE_NINES( ChartOptions.NINE_NINES_SENTINEL_VALUE ),
        /** Replaces removed points with NaN. */
        NAN( "NaN" );

        /**  */
        public final String name;

        /**
         * @param name
         */
        private PointRemovalMethod( String name )
        {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }
    }
}
