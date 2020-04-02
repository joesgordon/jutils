package org.jutils.plot.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.model.IDataView;
import org.jutils.plot.model.Chart;
import org.jutils.plot.model.Series;
import org.jutils.plot.ui.event.SaveSeriesDataListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesPropertiesView implements IDataView<Series>
{
    /**  */
    private final JPanel view;
    /**  */
    private final SeriesDataView dataView;
    /**  */
    private final SeriesView seriesView;

    /**  */
    private final Action saveAction;

    /**  */
    private final Chart chart;
    /**  */
    private Series series;

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public SeriesPropertiesView( Chart chart )
    {
        this.chart = chart;

        this.dataView = new SeriesDataView();
        this.seriesView = new SeriesView();

        this.saveAction = createSaveAction();

        this.view = createView();

        this.series = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( createPanels(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createPanels()
    {
        JTabbedPane tabs = new JTabbedPane();
        JScrollPane propPane = new JScrollPane( seriesView.getView() );

        tabs.addTab( "Properties", propPane );
        tabs.addTab( "Data", dataView.getView() );

        return tabs;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, saveAction );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;

        listener = ( e ) -> handleSavePressed();
        icon = IconConstants.getIcon( IconConstants.SAVE_AS_16 );
        action = new ActionAdapter( listener, "Save", icon );

        return action;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleSavePressed()
    {
        SaveSeriesDataListener ssdl = new SaveSeriesDataListener( this,
            chart.options.removalMethod );

        FileChooserListener listener = new FileChooserListener( view,
            "Choose File to Save", true, ssdl, ssdl );

        listener.showDialog();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Series getData()
    {
        return series;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Series series )
    {
        this.series = series;

        dataView.setData( series.data );
        seriesView.setData( series );
    }

    /***************************************************************************
     * @param pointIdx
     **************************************************************************/
    public void setSelected( int pointIdx )
    {
        dataView.setSelected( pointIdx );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }
}
