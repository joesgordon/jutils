package jutils.iris.ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jutils.core.ui.TabularView;
import jutils.core.ui.TabularView.ITabularModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.core.ui.model.IView;
import jutils.iris.data.ChannelPlacement;
import jutils.iris.data.RasterConfig;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlanesView implements IView<JComponent>
{
    /**  */
    private final JTabbedPane view;
    /**  */
    private final ChannelTable channel0View;
    /**  */
    private final ChannelTable channel1View;
    /**  */
    private final ChannelTable channel2View;
    /**  */
    private final ChannelTable channel3View;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlanesView()
    {
        this.channel0View = new ChannelTable( 0 );
        this.channel1View = new ChannelTable( 1 );
        this.channel2View = new ChannelTable( 2 );
        this.channel3View = new ChannelTable( 3 );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JTabbedPane createView()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "0", channel0View.getView() );
        tabs.addTab( "1", channel1View.getView() );
        tabs.addTab( "2", channel2View.getView() );
        tabs.addTab( "3", channel3View.getView() );

        return tabs;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public void setRaster( IRaster raster )
    {
        RasterConfig c = raster.getConfig();

        channel0View.setRaster( raster );
        channel1View.setRaster( raster );
        channel2View.setRaster( raster );
        channel3View.setRaster( raster );

        view.removeAll();

        ChannelTable [] views = { channel0View, channel1View, channel2View,
            channel3View };
        for( int i = 0; i < c.channelCount; i++ )
        {
            view.addTab( views[i].getName(), views[i].getView() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ChannelTable implements IView<JComponent>
    {
        /**  */
        private final int channelIndex;
        /**  */
        private final ChannelModel model;
        /**  */
        private final TabularView view;

        /**  */
        private IRaster raster;
        /**  */
        private boolean isBayer;
        /**  */
        private String name;

        /**
         * @param channelIndex
         */
        public ChannelTable( int channelIndex )
        {
            this.channelIndex = channelIndex;
            this.model = new ChannelModel(
                ( r, c ) -> getChannelValue( r, c ) );
            this.view = new TabularView();

            view.setModel( model );
        }

        /**
         * @return
         */
        public String getName()
        {
            return name;
        }

        /**
         * @param raster
         */
        public void setRaster( IRaster raster )
        {
            RasterConfig c = raster.getConfig();

            this.raster = raster;
            this.isBayer = c.channelLoc == ChannelPlacement.BAYER;

            int w = isBayer ? c.width / 2 : c.width;
            int h = isBayer ? c.height / 2 : c.height;

            model.setDimension( w, h );

            this.name = c.channels[channelIndex].name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view.getView();
        }

        /**
         * @param row
         * @param col
         * @return
         */
        private int getChannelValue( int row, int col )
        {
            int r = row;
            int c = col;

            if( isBayer )
            {
                boolean isSecPair = channelIndex < 2;
                boolean isChOdd = ( channelIndex & 1 ) == 1;

                r = row * 2 + ( isSecPair ? 0 : 1 );
                c = col * 2 + ( isChOdd ? 1 : 0 );
            }

            return raster.getChannelAt( c, r, channelIndex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static interface IChannelSupplier
    {
        /**
         * @param row
         * @param col
         * @return
         */
        public int build( int row, int col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ChannelModel implements ITabularModel
    {
        /**  */
        private final IChannelSupplier valueBuilder;
        /**  */
        private final Dimension rasterDim;

        /**  */
        private ITabularNotifier notifier;

        /**
         * @param valueBuilder
         */
        public ChannelModel( IChannelSupplier valueBuilder )
        {
            this.valueBuilder = valueBuilder;
            this.rasterDim = new Dimension( 0, 0 );

            this.notifier = null;
        }

        public void setDimension( int width, int height )
        {
            this.rasterDim.width = width;
            this.rasterDim.height = height;

            if( notifier != null )
            {
                notifier.fireStructureChanged();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerNotifier( ITabularNotifier notifier )
        {
            this.notifier = notifier;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRowCount()
        {
            return rasterDim.height;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getRowName( int row )
        {
            return "" + row;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getColCount()
        {
            return rasterDim.width;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getColName( int col )
        {
            return "" + col;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getColClass( int col )
        {
            return String.class;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValue( int row, int col )
        {
            return valueBuilder.build( row, col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setValue( Object value, int row, int col )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( int row, int col )
        {
            return false;
        }
    }
}
