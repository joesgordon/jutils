package jutils.iris.colors;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonoColorOptions
{
    /**  */
    public final MonoThreshold highThreshold;
    /**  */
    public final MonoThreshold lowThreshold;
    /**  */
    public MonoColorModel colorModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public MonoColorOptions()
    {
        this.highThreshold = new MonoThreshold();
        this.lowThreshold = new MonoThreshold();

        this.colorModel = MonoColorModel.GRAYSCALE;

        this.highThreshold.value = Integer.MAX_VALUE;
        this.lowThreshold.value = Integer.MIN_VALUE;
    }
}
