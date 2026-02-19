package jutils.math.dsp;

import javax.swing.JFrame;

import jutils.core.io.LogUtils;
import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DspMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        testA2d();

        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void testA2d()
    {
        DigAnaConverter c = new DigAnaConverter();

        double max = c.getMaximum();
        double min = c.getMinimum();
        double rng = max - min;
        int count = 101;

        for( int i = 0; i < count; i++ )
        {
            double sample = i * rng / ( count - 1 ) + min;
            testA2d( c, sample );
        }
    }

    /***************************************************************************
     * @param c
     * @param sample
     **************************************************************************/
    private static void testA2d( DigAnaConverter c, double sample )
    {
        int dig = c.analogToDigital( sample );
        double s2 = c.digitalToAnalog( dig );
        double pe = Math.abs( s2 - sample );

        LogUtils.printDebug( "%f => %08X => %f: %f%%", sample, dig, s2, pe );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        DspTestFrame view = new DspTestFrame();
        JFrame frame = view.getView();

        return frame;
    }
}
