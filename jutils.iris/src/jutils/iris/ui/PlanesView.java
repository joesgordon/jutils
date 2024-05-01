package jutils.iris.ui;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jutils.core.ui.TabularView;
import jutils.core.ui.TabularView.ITabularModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.core.ui.model.IView;
import jutils.iris.rasters.IChannel;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlanesView implements IView<JComponent>
{
    /**  */
    private final JTabbedPane view;
    /**  */
    private final ChannelTable [] channelViews;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlanesView()
    {
        this.channelViews = new ChannelTable[IRaster.MAX_CHANNELS];

        for( int c = 0; c < channelViews.length; c++ )
        {
            channelViews[c] = new ChannelTable();
        }

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JTabbedPane createView()
    {
        JTabbedPane tabs = new JTabbedPane();

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
        view.removeAll();

        for( int c = 0; c < raster.getChannelCount(); c++ )
        {
            ChannelTable cv = channelViews[c];
            IChannel channel = raster.getChannel( c );

            cv.setChannel( channel );
            view.addTab( cv.getName(), cv.getView() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ChannelTable implements IView<JComponent>
    {
        /**  */
        private final ChannelModel model;
        /**  */
        private final TabularView view;

        /**  */
        private String name;

        /**
         * @param channelIndex
         */
        public ChannelTable()
        {
            this.model = new ChannelModel();
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
        public void setChannel( IChannel channel )
        {
            model.setChannel( channel );

            this.name = channel.getName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JComponent getView()
        {
            return view.getView();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ChannelModel implements ITabularModel
    {
        /**  */
        private IChannel channel;
        /**  */
        private ITabularNotifier notifier;

        /**
         * @param valueBuilder
         */
        public ChannelModel()
        {
            this.channel = null;
            this.notifier = null;
        }

        /**
         * @param channel
         */
        public void setChannel( IChannel channel )
        {
            this.channel = channel;

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
            return channel == null ? 0 : channel.getHeight();
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
            return channel == null ? 0 : channel.getWidth();
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
            return "" + channel.getValueAt( col, row );
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
