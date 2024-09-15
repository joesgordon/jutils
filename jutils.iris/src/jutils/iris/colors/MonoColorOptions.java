package jutils.iris.colors;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonoColorOptions
{
    /**  */
    public final MonoThreshold lowThreshold;
    /**  */
    public final MonoThreshold highThreshold;

    /**  */
    public MonoColorModel colorModel;
    /**  */
    public double gain;
    /**  */
    public double offset;

    /***************************************************************************
     * 
     **************************************************************************/
    public MonoColorOptions()
    {
        this.lowThreshold = new MonoThreshold();
        this.highThreshold = new MonoThreshold();

        this.colorModel = MonoColorModel.GRAYSCALE;
        this.gain = 1.0;
        this.offset = 0.0;

        this.highThreshold.value = Integer.MAX_VALUE;
    }
}
