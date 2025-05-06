package jutils.core.time;

/*******************************************************************************
 * Defines a representation of the <a
 * href="https://learn.microsoft.com/en-us/windows/win32/api/minwinbase/ns-minwinbase-filetime">
 * FILETIME</a> structure.
 ******************************************************************************/
public class MsFileTime
{
    /** The FILETIME that corresponds to 00:00:00.000000000 January 1 1970. */
    public static final long LINUX_EPOCH = 116444736000000000L;

    /**
     * Number of 100 nanosecond intervals since 00:00:00.000000000 January 1,
     * 1601 Coordinated Universal Time (UTC)
     */
    public long time;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsFileTime()
    {
        this.time = LINUX_EPOCH;
    }
}
