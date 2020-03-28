package org.jutils.apps.summer.data;

/**
 *
 */
public class SummerOptions
{
    /**  */
    public int numThreads;

    /**
     * 
     */
    public SummerOptions()
    {
        numThreads = 8;
    }

    /**
     * @param options
     */
    public SummerOptions( SummerOptions options )
    {
        this.numThreads = options.numThreads;
    }
}
