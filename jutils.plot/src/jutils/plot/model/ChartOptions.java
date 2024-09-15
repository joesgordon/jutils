package jutils.plot.model;

import jutils.core.INamedItem;

/*******************************************************************************
 * Defines the options for a chart.
 ******************************************************************************/
public class ChartOptions
{
    /**
     * Defines the sentinel string that represents an invalid value. This string
     * is read as 1E9, so check against the literal string value for detection.
     */
    public static final String NINE_NINES_SENTINEL_VALUE = "999999999.999999999";

    /** Turns on anti-aliasing for all plot shape drawing. */
    public boolean antialias;
    /** Turns on anti-aliasing for all plot text drawing. */
    public boolean textAntiAlias;
    /** Draws the major grid lines on the plot. */
    public boolean gridlinesVisible;
    /** Disables the ability to remove points if {@code false}. */
    public boolean removalEnabled;
    /** Defines how data is written when points are hidden. */
    public PointRemovalMethod removalMethod;

    /***************************************************************************
     * Creates new options.
     **************************************************************************/
    public ChartOptions()
    {
        this.antialias = true;
        this.textAntiAlias = true;
        this.gridlinesVisible = true;
        this.removalEnabled = true;
        this.removalMethod = PointRemovalMethod.NAN;
    }

    /***************************************************************************
     * Defines what happens to points that are removed.
     **************************************************************************/
    public static enum PointRemovalMethod implements INamedItem
    {
        /** Removed points completely. */
        DELETE( "Delete", null ),
        /** Replaces removed points with a flag value of 999999999.999999999. */
        NINE_NINES( ChartOptions.NINE_NINES_SENTINEL_VALUE,
            ChartOptions.NINE_NINES_SENTINEL_VALUE ),
        /** Replaces removed points with NaN. */
        NAN( "NaN", "NaN" );

        /**  */
        public final String name;
        /**  */
        public final String value;

        /**
         * @param name
         * @param value
         */
        private PointRemovalMethod( String name, String value )
        {
            this.name = name;
            this.value = value;
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
