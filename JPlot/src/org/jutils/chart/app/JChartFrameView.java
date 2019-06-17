package org.jutils.chart.app;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.chart.ChartIcons;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.model.ISeriesData;
import org.jutils.chart.model.Series;
import org.jutils.chart.ui.ChartView;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final ChartView chartView;

    /***************************************************************************
     * @param title
     * @param options
     **************************************************************************/
    public JChartFrameView( String title )
    {
        this.frameView = new StandardFrameView();
        this.chartView = new ChartView();

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );
        frameView.setContent( chartView.getView() );

        JFrame frame = frameView.getView();

        frame.setTitle( title );
        frame.setIconImages( ChartIcons.getChartImages() );
        frame.addWindowListener( new ChartWindowListener( this ) );

        chartView.chart.domainAxis.title.visible = true;
        chartView.chart.domainAxis.title.text = "X Values";

        chartView.chart.rangeAxis.title.visible = true;
        chartView.chart.rangeAxis.title.text = "Y Values";

        chartView.chart.secDomainAxis.title.visible = true;
        chartView.chart.secDomainAxis.title.text = "Sec X Values";

        chartView.chart.secRangeAxis.title.visible = true;
        chartView.chart.secRangeAxis.title.text = "Sec Y Values";

        chartView.chart.legend.visible = true;

        Series s;
        ISeriesData<?> data;

        // data = ChartUtils.createTestSeries();
        // s = new Series( data );
        // s.line.weight = 4;
        // s.isPrimaryDomain = true;
        // s.isPrimaryRange = true;
        // chartView.addSeries( s );

        chartView.chart.title.text = "Example Data Sets";

        int pointCount = 1000000;

        data = ChartUtils.createLineSeries( pointCount, -1.0, 0.0, -5.0, 5.0 );
        s = new Series( data );
        s.name = "y = -x";
        s.marker.color = new Color( 0xFF9933 );
        s.highlight.color = new Color( 0xFF9933 );
        s.line.color = new Color( 0xCC6622 );
        chartView.addSeries( s, true );

        data = ChartUtils.createSinSeries( pointCount, 1.0, 4.0, 0.0, -5.0,
            5.0 );
        s = new Series( data );
        s.name = "y = sin(x)";
        s.marker.color = new Color( 0x339933 );
        s.highlight.color = new Color( 0x339933 );
        s.line.color = new Color( 0x227722 );
        s.line.weight = 4;
        chartView.addSeries( s, true );

        data = ChartUtils.createLineSeries( pointCount, 1.0, 0.0, 5.1, 10.9 );
        s = new Series( data );
        s.name = "y = x";
        s.line.weight = 4;
        s.isPrimaryDomain = false;
        s.isPrimaryRange = false;
        chartView.addSeries( s, true );

        // data = ChartUtils.createSinSeries( pointCount, 4.0, 40.0, 0.0, -5.0,
        // 5.0
        // );
        // s = new Series( data );
        // s.name = "y = sin(x)";
        // s.marker.color = new Color( 0x393933 );
        // s.highlight.color = new Color( 0x339933 );
        // s.line.color = new Color( 0x227722 );
        // s.line.weight = 4;
        // chartView.addSeries( s, true );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChartView getChart()
    {
        return chartView;
    }

    /***************************************************************************
     * @param menuBar
     * @return
     **************************************************************************/
    private JMenuBar createMenubar( JMenuBar menubar, JMenu fileMenu )
    {
        menubar.add( createFileMenu( fileMenu ) );

        menubar.add( createViewMenu() );

        return menubar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createFileMenu( JMenu menu )
    {
        JMenuItem item;
        int i = 0;

        item = new JMenuItem( chartView.createOpenAction() );
        menu.add( item, i++ );

        menu.add( chartView.getOpenMenu(), i++ );

        item = new JMenuItem( chartView.createSaveAction() );
        menu.add( item, i++ );

        item = new JMenuItem( chartView.createSaveDataAction() );
        menu.add( item, i++ );

        item = new JMenuItem( "Clear" );
        item.addActionListener( ( e ) -> chartView.clear() );
        item.setIcon( IconConstants.getIcon( IconConstants.EDIT_CLEAR_16 ) );
        menu.add( item, i++ );

        menu.add( new JSeparator(), i++ );

        return menu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createViewMenu()
    {
        JMenu menu = new JMenu( "View" );
        JMenuItem item;

        item = new JMenuItem( chartView.createPropertiesAction() );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChartWindowListener extends WindowAdapter
    {
        private final JChartFrameView view;

        public ChartWindowListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            view.chartView.closeOptions();
        }
    }
}
