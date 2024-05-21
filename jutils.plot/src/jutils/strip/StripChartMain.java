package jutils.strip;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.concurrent.ScheduledTask;
import jutils.core.concurrent.ScheduledTask.IPeriodic;
import jutils.core.io.LogUtils;
import jutils.core.ui.ABButton;
import jutils.core.ui.ABButton.IABCallback;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.model.IView;
import jutils.strip.data.AxisConfig;
import jutils.strip.data.DataBuffer;
import jutils.strip.data.DataMetrics;
import jutils.strip.ui.StripChartView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StripChartMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main2( String [] args )
    {
        DataMetrics dm = new DataMetrics();
        AxisConfig tm = new AxisConfig();
        int h = 400;

        double scale = 10.0;

        dm.range.min = -1000.3 * scale;
        dm.range.max = 4.1 * scale;
        dm.count = 100;

        dm.range.min = -7.799956;
        dm.range.max = 1.199603;
        dm.count = 100;
        h = 1045;

        StripUtils.calcTicks( tm.ticks, dm.range, h );

        DataBuffer db = new DataBuffer( 10 );

        for( int i = 0; i < 10000; i++ )
        {
            db.add( i, i );
            if( db.getSize() < 1 )
            {
                throw new IllegalStateException();
            }
            LogUtils.printDebug( "i = %d", i );
            db.getYMetrics( dm );
        }
    }

    /***************************************************************************
     * @return the newly created frame
     **************************************************************************/
    private static JFrame createFrame()
    {
        StripChartFrame frame = new StripChartFrame();

        return frame.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class StripChartFrame implements IView<JFrame>
    {
        /**  */
        private final StandardFrameView view;
        /**  */
        private final StripChartView chart;
        /**  */
        private final ABButton simButton;

        /**  */
        private ScheduledTask task;

        /**
         * 
         */
        public StripChartFrame()
        {
            this.view = new StandardFrameView();
            this.chart = new StripChartView();
            this.simButton = createSimButton();
            this.task = null;

            view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            view.setContent( chart.getView() );
            view.setTitle( "Strip Chart" );
            view.setSize( 800, 800 );
            view.setToolbar( createToolbar() );

            DataBuffer b;

            b = new DataBuffer( 1000 );
            b.setColor( Color.red );
            chart.addBuffer( b );

            b = new DataBuffer( 1000 );
            b.setColor( Color.yellow );
            chart.addBuffer( b );
        }

        /**
         * @return the start/stop button
         */
        private ABButton createSimButton()
        {
            Icon startIcon = IconConstants.getIcon( IconConstants.NAV_NEXT_16 );
            Icon stopIcon = IconConstants.getIcon( IconConstants.STOP_16 );

            return new ABButton( "Start", startIcon, () -> startTask(), "Stop",
                stopIcon, () -> stopTask() );
        }

        /**
         * @return the toolbar that was created.
         */
        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JToolBar();

            SwingUtils.setToolbarDefaults( toolbar );

            toolbar.add( simButton.button );

            return toolbar;
        }

        /**
         * @return {@code true} if the task was started.
         * @see IABCallback#run()
         */
        private boolean startTask()
        {
            boolean started = false;

            stopTask();

            this.task = new ScheduledTask( 30.0, new SimTask( chart ) );

            started = task.start();

            return started;
        }

        /**
         * @return {@code true} if the task was stopped.
         * @see IABCallback#run()
         */
        private boolean stopTask()
        {
            if( task != null )
            {
                task.stop();
                task.waitFor();
                task = null;
            }

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame getView()
        {
            return view.getView();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SimTask implements IPeriodic
    {
        /**  */
        private final StripChartView chart;

        /**
         * @param chart the chart to add points to
         */
        public SimTask( StripChartView chart )
        {
            this.chart = chart;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run( long count, long duration )
        {
            SwingUtilities.invokeLater( () -> {
                double x = duration / 1.0e9;
                double y;

                // x = count / 10.0;

                y = 3.5 * Math.sin( count * Math.PI / 25 ) - .82;
                chart.add( 0, x, y );

                y = -1.12 * Math.sin( .69 * count * Math.PI / 25 + .5 ) + .82;
                chart.add( 1, x, y );

                chart.getView().invalidate();
                chart.getView().repaint();
            } );
        }
    }
}
