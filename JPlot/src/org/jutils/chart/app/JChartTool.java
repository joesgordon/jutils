package org.jutils.chart.app;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.jutils.chart.ChartIcons;
import org.jutils.ui.IToolView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartTool implements IToolView
{
    /**  */
    private JChartFrameView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public JChartTool()
    {
        this.view = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        if( view == null )
        {
            this.view = new JChartFrameView( JChartAppConstants.APP_NAME );

            view.getView().setSize( 500, 500 );
            view.getView().validate();
        }

        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Creates plots";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "JChart";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ImageIcon getIcon24()
    {
        return ChartIcons.loader.getIcon( ChartIcons.CHART_024 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return ChartIcons.getChartImages();
    }
}
