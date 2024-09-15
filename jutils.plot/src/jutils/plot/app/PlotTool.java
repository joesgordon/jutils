package jutils.plot.app;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import jutils.core.ui.IToolView;
import jutils.plot.ChartIcons;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotTool implements IToolView
{
    /**  */
    private PlotFrameView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlotTool()
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
            this.view = new PlotFrameView( PlotConstants.APP_NAME );

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
