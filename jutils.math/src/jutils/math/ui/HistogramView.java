package jutils.math.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import jutils.core.laf.UIProperty;
import jutils.core.ui.PaintingComponent;
import jutils.core.ui.event.MouseEventsListener;
import jutils.core.ui.event.MouseEventsListener.MouseEventType;
import jutils.core.ui.model.IView;
import jutils.math.Histogram;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HistogramView implements IView<JComponent>
{
    /**  */
    private final PaintingComponent view;

    /**  */
    private final JLabel hoverLabel;

    /**  */
    private Histogram histogram;
    /**  */
    private Point hoverPoint;
    /**  */
    private int hoverStart;
    /**  */
    private int hoverEnd;
    /**  */
    private int hoverCount;

    /***************************************************************************
     * 
     **************************************************************************/
    public HistogramView()
    {
        this.view = new PaintingComponent( ( c, g ) -> paintHistogram( c, g ) );
        this.histogram = new Histogram( "Dice Values", 13, 12 );
        this.hoverLabel = new JLabel();

        this.hoverPoint = null;
        this.hoverStart = 0;
        this.hoverEnd = 0;
        this.hoverCount = 0;

        hoverLabel.setHorizontalAlignment( SwingConstants.CENTER );
        hoverLabel.setVerticalAlignment( SwingConstants.CENTER );
        hoverLabel.setOpaque( true );
        hoverLabel.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
        hoverLabel.setBorder( new LineBorder( Color.yellow, 2 ) );

        histogram.addValue( 2 );

        histogram.addValue( 3 );
        histogram.addValue( 3 );
        histogram.addValue( 11 );
        histogram.addValue( 11 );

        histogram.addValue( 4 );
        histogram.addValue( 4 );
        histogram.addValue( 4 );
        histogram.addValue( 10 );
        histogram.addValue( 10 );
        histogram.addValue( 10 );

        histogram.addValue( 5 );
        histogram.addValue( 5 );
        histogram.addValue( 5 );
        histogram.addValue( 5 );
        histogram.addValue( 9 );
        histogram.addValue( 9 );
        histogram.addValue( 9 );
        histogram.addValue( 9 );

        histogram.addValue( 6 );
        histogram.addValue( 6 );
        histogram.addValue( 6 );
        histogram.addValue( 6 );
        histogram.addValue( 6 );
        histogram.addValue( 8 );
        histogram.addValue( 8 );
        histogram.addValue( 8 );
        histogram.addValue( 8 );
        histogram.addValue( 8 );

        histogram.addValue( 7 );
        histogram.addValue( 7 );
        histogram.addValue( 7 );
        histogram.addValue( 7 );
        histogram.addValue( 7 );
        histogram.addValue( 7 );
        histogram.addValue( 7 );

        histogram.addValue( 12 );
        view.setBackground( Color.gray );

        MouseEventsListener mve;

        mve = new MouseEventsListener( ( t, e ) -> handleMouseEvent( t, e ) );

        view.addMouseListener( mve );
        view.addMouseMotionListener( mve );
    }

    /***************************************************************************
     * @param t
     * @param e
     **************************************************************************/
    private void handleMouseEvent( MouseEventType t, MouseEvent e )
    {
        Histogram h = this.histogram;

        if( t == MouseEventType.MOVED && h != null )
        {
            Point pt = e.getPoint();
            int x = pt.x;
            int xMax = view.getWidth();
            double percentX = x / ( double )xMax;

            int idxMax = h.bins.length;

            double didx = percentX * idxMax;
            int iidx = ( int )Math.floor( didx );

            double valuesPerBin = ( h.maxValue + 1 ) / ( double )h.bins.length;
            double lowerBin = valuesPerBin * iidx;
            double upperBin = lowerBin + valuesPerBin - 1;

            int lbindex = ( int )Math.round( lowerBin );
            int ubindex = ( int )Math.round( upperBin );

            this.hoverStart = lbindex;
            this.hoverEnd = ubindex;
            this.hoverPoint = pt;
            this.hoverCount = histogram.bins[iidx];

            view.repaint();

            // LogUtils.print( "Pos: %d of %d (%.3f) => %d(%.3f) values %d -
            // %d",
            // x, xMax, percentX, iidx, didx, lbindex, ubindex );
        }
        else if( t == MouseEventType.EXITED )
        {
            hoverPoint = null;
            view.repaint();
        }
    }

    /***************************************************************************
     * @param c
     * @param g
     **************************************************************************/
    private void paintHistogram( JComponent c, Graphics2D g )
    {
        Point hp = this.hoverPoint;
        int w = c.getWidth();
        int h = c.getHeight();

        g.setBackground( c.getBackground() );
        g.clearRect( 0, 0, w, h );

        float xs = w / ( float )histogram.bins.length;
        int width = 1 + ( int )xs;

        float ys = ( h - 4 ) / ( float )histogram.getMaxBinCount();

        // -----------------------------------------------------------------
        // Paint bins above the low threshold value and below the high
        // threshold value.
        // -----------------------------------------------------------------

        g.setColor( Color.black );
        for( int i = 0; i < histogram.bins.length; i++ )
        {
            paintBin( c, g, i, histogram.bins[i], xs, ys, width );
        }

        if( hp != null )
        {
            String txt = hoverStart == hoverEnd
                ? String.format( "%d: %d", hoverStart, hoverCount )
                : String.format( "%d - %d: %d", hoverStart, hoverEnd,
                    hoverCount );
            Point tp = new Point( hoverPoint );

            hoverLabel.setText( txt );
            hoverLabel.repaint();
            hoverLabel.validate();
            Dimension td = hoverLabel.getPreferredSize();
            td.width += 6;
            td.height += 4;
            tp.y -= td.height;
            hoverLabel.setSize( td );

            if( ( tp.x + td.width ) > w )
            {
                tp.x = w - td.width;
            }

            g.translate( tp.x, tp.y );
            hoverLabel.paint( g );
            g.translate( -tp.x, -tp.y );
        }
    }

    /***************************************************************************
     * @param c
     * @param g
     * @param i
     * @param v
     * @param xs
     * @param ys
     * @param width
     **************************************************************************/
    private static void paintBin( JComponent c, Graphics2D g, int i, int v,
        float xs, float ys, int width )
    {
        try
        {
            int x = ( int )( i * xs );
            int height = ( int )( ys * v );
            int y = ( int )( c.getHeight() - height );
            g.fillRect( x, y, width, height );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @param histogram
     **************************************************************************/
    public void setData( Histogram histogram )
    {
        this.histogram = histogram;

        view.repaint();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
