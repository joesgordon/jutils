package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.model.*;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LegendWidget implements IChartWidget
{
    /**  */
    private static final int LEGEND_PADDING = 0;
    /**  */
    private static final int LABEL_PADDING = 4;
    /**  */
    private static final int MARKER_SIZE = 10;

    /**  */
    private final Legend legend;
    /**  */
    private final List<PlotWidget> plots;

    /**  */
    private final TextLabel nameLabel;
    /**  */
    private final TextWidget nameWidget;

    /**  */
    private final Layer2d layer;

    /**  */
    private PlacementGrid grid;

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public LegendWidget( Legend legend, List<PlotWidget> plots )
    {
        this.nameLabel = new TextLabel();
        this.nameWidget = new TextWidget( nameLabel );
        this.layer = new Layer2d();
        this.legend = legend;
        this.plots = plots;
        this.grid = new PlacementGrid( new ArrayList<>(), new Dimension(),
            false, 0, 0 );

        nameLabel.alignment = HorizontalAlignment.LEFT;
        nameLabel.font = nameLabel.font.deriveFont( 14.0f );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        layer.repaint = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return layout( canvasSize );
    }

    /***************************************************************************
     * @param availSize
     * @param isVertical
     * @return
     **************************************************************************/
    private PlacementGrid buildGrid( Dimension availSize, boolean isVertical )
    {
        List<PlotKey> keys = new ArrayList<>();

        for( PlotWidget p : plots )
        {
            if( p.series.visible )
            {
                nameLabel.text = p.series.name;

                Dimension keySize = nameWidget.calculateSize( availSize );

                keySize.height = Math.max( keySize.height, MARKER_SIZE ) + 4;
                keySize.width += 4 + MARKER_SIZE;

                PlotKey key = new PlotKey( p, keySize );

                keys.add( key );
            }
        }

        return new PlacementGrid( keys, availSize, isVertical, 6,
            legend.border.thickness );
    }

    /***************************************************************************
     * @param size
     * @return
     **************************************************************************/
    public Dimension layout( Dimension size )
    {
        grid = buildGrid( size, legend.side.isVertical );
        layer.setSize( grid.size );

        return grid.size;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point unusedPoint,
        Dimension unusedSize )
    {
        if( layer.repaint )
        {
            draw( layer.getGraphics() );
            layer.repaint = false;
        }

        layer.paint( graphics );
    }

    /***************************************************************************
     * @param graphics
     **************************************************************************/
    private void draw( Graphics2D graphics )
    {
        Point location = new Point( LEGEND_PADDING, LEGEND_PADDING );

        Dimension size = new Dimension( grid.size );

        size.width -= 2 * LEGEND_PADDING;
        size.height -= 2 * LEGEND_PADDING;

        // ---------------------------------------------------------------------
        // Fill
        // ---------------------------------------------------------------------
        graphics.setColor( legend.fill );
        graphics.fillRect( location.x, location.y, size.width, size.height );

        // ---------------------------------------------------------------------
        // Draw border
        // ---------------------------------------------------------------------
        if( legend.border.visible && legend.border.thickness > 0 )
        {
            int thickness = legend.border.thickness;
            int half = thickness / 2;

            graphics.setStroke( new BasicStroke( thickness ) );
            graphics.setColor( legend.border.color );
            graphics.drawRect( location.x + half, location.y + half,
                size.width - thickness, size.height - thickness );

            location.x += thickness;
            location.y += thickness;

            size.width -= 2 * thickness;
            size.height -= 2 * thickness;
        }

        // ---------------------------------------------------------------------
        // Draw keys
        // ---------------------------------------------------------------------
        graphics.setStroke( new BasicStroke( 2 ) );
        graphics.setColor( Color.black );

        for( KeyList list : grid.items )
        {
            // LogUtils.printDebug( "drawing series keys at " + colPoint );

            for( PlotKey key : list.keys )
            {
                Point p = new Point( key.loc );

                p.x = p.x + 6;
                p.y = p.y + key.size.height / 2;

                int ms = key.s.marker.getSize();
                key.s.marker.setSize( MARKER_SIZE );
                key.s.marker.draw( graphics, p );
                key.s.marker.setSize( ms );

                p.x = key.loc.x + 12;
                p.y = key.loc.y;

                nameLabel.text = key.s.series.name;
                nameWidget.repaint();
                nameWidget.draw( graphics, p, key.size );

                // graphics.setStroke( new BasicStroke() );
                //
                // graphics.setColor( Color.green );
                // graphics.drawRect( key.loc.x, key.loc.y, key.size.width,
                // key.size.height );
                //
                // graphics.setColor( Color.blue );
                // graphics.drawRect( p.x, p.y, key.size.width, key.size.height
                // );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PlacementGrid
    {
        public final List<KeyList> items;
        public final Dimension size;

        private final boolean isVertical;

        public PlacementGrid( List<PlotKey> keys, Dimension availableSize,
            boolean isVertical, int itemSpacing, int thickness )
        {
            this.items = new ArrayList<>();
            this.isVertical = isVertical;
            this.size = new Dimension();

            int directionLimit = isVertical ? availableSize.height
                : availableSize.width;

            KeyList list = new KeyList();
            int init_off = LABEL_PADDING + LEGEND_PADDING;

            int x = init_off;
            int y = init_off;

            items.add( list );

            for( PlotKey key : keys )
            {
                int listLen = getItemLen( list.size );
                int nextLen = listLen + getItemLen( key.size );

                if( nextLen > directionLimit && !list.keys.isEmpty() )
                {
                    x = isVertical ? x + list.size.width + itemSpacing
                        : init_off;
                    y = isVertical ? init_off
                        : y + list.size.height + itemSpacing;

                    list = new KeyList();
                    items.add( list );
                }

                key.loc.x = x;
                key.loc.y = y;

                list.keys.add( key );

                int space = list.keys.size() < 1 ? 0 : itemSpacing;

                x += isVertical ? 0 : key.size.width + itemSpacing;
                y += isVertical ? key.size.height + itemSpacing : 0;

                list.size.width = isVertical
                    ? Math.max( list.size.width, key.size.width )
                    : list.size.width + key.size.width + space;

                list.size.height = isVertical
                    ? list.size.height + key.size.height + space
                    : Math.max( list.size.height, key.size.height );
            }

            for( KeyList kl : items )
            {
                size.width += kl.size.width;
                size.height += kl.size.height;
            }

            size.width += 2 * ( LABEL_PADDING + thickness );
            size.height += 2 * ( LABEL_PADDING + thickness );

            if( isVertical )
            {
                size.height = availableSize.height;
            }
            else
            {
                size.width = availableSize.width;
            }
        }

        private int getItemLen( Dimension item )
        {
            return isVertical ? item.height : item.width;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class KeyList
    {
        public final List<PlotKey> keys;
        public final Dimension size;

        public KeyList()
        {
            this.keys = new ArrayList<>();
            this.size = new Dimension();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PlotKey
    {
        public final PlotWidget s;
        public final Point loc;
        public final Dimension size;

        public PlotKey( PlotWidget s, Dimension size )
        {
            this.s = s;
            this.loc = new Point();
            this.size = size;
        }
    }
}
