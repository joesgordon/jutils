package org.jutils.data;

/*******************************************************************************
 * Defines an immutable build information object.
 ******************************************************************************/
public class BuildInfo
{
    /** The version of the build. */
    public final String version;
    /** The build date. */
    public final String buildDate;

    /***************************************************************************
     * Creates a new build information object with the provided version and
     * date.
     * @param version the version of the build.
     * @param date the build date.
     **************************************************************************/
    public BuildInfo( String version, String date )
    {
        this.version = version;
        this.buildDate = date;
    }
}
