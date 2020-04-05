package org.jutils.plot.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.core.IconConstants;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.plot.ChartUtils;
import org.jutils.plot.data.QuadSide;
import org.jutils.plot.model.*;
import org.jutils.plot.ui.WidgetPanel;
import org.jutils.plot.ui.objects.LegendWidget;
import org.jutils.plot.ui.objects.PlotWidget;

public class LegendWidgetApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                StandardFrameView frameView = new StandardFrameView();
                JFrame frame = frameView.getView();
                JPanel panel = new JPanel( new GridBagLayout() );
                Legend legend = new Legend();
                List<PlotWidget> plots = createPlots();
                LegendWidget lw = new LegendWidget( legend, plots );
                WidgetPanel wp = new WidgetPanel( lw );

                legend.side = QuadSide.BOTTOM;
                legend.visible = true;

                GridBagConstraints constraints;

                constraints = new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets( 0, 0, 0, 0 ), 0, 0 );
                panel.add( createToolbar(), constraints );
                constraints = new GridBagConstraints( 1, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets( 0, 0, 0, 0 ), 0, 0 );
                panel.add( wp.getView(), constraints );

                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 500, 500 );
                frameView.setContent( panel );

                frame.setTitle( "Testing LegendWidget" );

                return frame;
            }

            private List<PlotWidget> createPlots()
            {
                List<PlotWidget> plots = new ArrayList<>();
                PlotWidget pw;

                ISeriesData<?> data;

                data = ChartUtils.createLineSeries( 100, 1.0, 0.0, 5.1, 10.9 );
                pw = new PlotWidget( new Series( data ), null );
                pw.series.name = "Series 1";
                plots.add( pw );

                data = ChartUtils.createLineSeries( 100, 1.0, 0.0, 5.1, 10.9 );
                pw = new PlotWidget( new Series( data ), null );
                pw.series.name = "Series 2";
                plots.add( pw );

                data = ChartUtils.createLineSeries( 100, 1.0, 0.0, 5.1, 10.9 );
                pw = new PlotWidget( new Series( data ), null );
                pw.series.name = "Series 3";
                plots.add( pw );

                return plots;
            }

            private Component createToolbar()
            {
                JToolBar tb = new JGoodiesToolBar();

                JButton button = new JButton(
                    IconConstants.getIcon( IconConstants.OPEN_FILE_16 ) );

                tb.add( button );

                return tb;
            }
        } );
    }
}
