package org.jutils.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.data.UIProperty;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;
import org.jutils.ui.fields.IDescriptor;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Defines a control similar to a horizontal scroll-bar that pops-up the
 * position when moved. The position will be adjusted to the previous unit
 * increment value. For example a item of length 100 and unit length 10 will
 * have a position of 40 when set to 42.
 ******************************************************************************/
public class PositionIndicator implements IView<JComponent>
{
    /** The window that pops up when the position indicator is moved. */
    private final JWindow positionWindow;
    /** The label that shows the position in the popup window. */
    private final JLabel posLabel;
    /** The list of listers to be notified when the position changes. */
    private final UpdaterList<Long> posititonListeners;
    /** The object that draws the component. */
    private final PositionIndicatorPaintable paintable;
    /** The control to be drawn. */
    private final PaintingComponent component;
    /**  */
    private final PiMouseListener mouseListener;

    // TODO install escape key listener to abort dragging.

    /***************************************************************************
     * Creates a new control with the default descriptor (which displays the
     * position in a zero-padded hexadecimal string).
     **************************************************************************/
    public PositionIndicator()
    {
        this( null );
    }

    /***************************************************************************
     * Creates a new control with the provided descriptor.
     * @param positionDescriptor converts a {@link Long} into a {@link String}.
     **************************************************************************/
    public PositionIndicator( IDescriptor<Long> positionDescriptor )
    {
        IDescriptor<Long> descriptor = positionDescriptor == null
            ? createDefaultPositionDescriptor()
            : positionDescriptor;

        this.paintable = new PositionIndicatorPaintable();
        this.component = new PaintingComponent( paintable );
        this.posLabel = new JLabel();
        this.positionWindow = createWindow();
        this.posititonListeners = new UpdaterList<>();
        this.mouseListener = new PiMouseListener( this, descriptor );

        component.setMinimumSize( new Dimension( 20, 20 ) );
        component.setPreferredSize( new Dimension( 20, 20 ) );

        component.addMouseListener( mouseListener );
        component.addMouseMotionListener( mouseListener );
    }

    /***************************************************************************
     * Creates a position indicator descriptor that displays the position in a
     * zero-padded hexadecimal string.
     * @return the default position descriptor.
     **************************************************************************/
    public static IDescriptor<Long> createDefaultPositionDescriptor()
    {
        return ( v ) -> String.format( "0x%016X", v );
    }

    /***************************************************************************
     * Creates the windows that displays the position while dragging the thumb.
     * @return the created window.
     * @see #positionWindow
     **************************************************************************/
    private JWindow createWindow()
    {
        JWindow win = new JWindow();
        JPanel panel = new JPanel( new BorderLayout() );

        posLabel.setForeground( Color.white );
        posLabel.setHorizontalAlignment( SwingConstants.HORIZONTAL );
        posLabel.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );

        panel.setBackground( new Color( 0x006699 ) );
        panel.setBorder( new LineBorder( Color.darkGray ) );

        panel.add( posLabel, BorderLayout.CENTER );

        win.setContentPane( panel );
        win.setSize( 140, 25 );
        win.validate();

        return win;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return component;
    }

    /***************************************************************************
     * Adds the provided listener to be called when the position is updated.
     * @param l the listener to be added.
     **************************************************************************/
    public void addPositionListener( IUpdater<Long> l )
    {
        posititonListeners.add( l );
    }

    /***************************************************************************
     * Adds a method to be invoked when the tab is right-clicked.
     * @param callback the method to be invoked.
     **************************************************************************/
    public void addRightClick( IUpdater<MouseEvent> callback )
    {
        mouseListener.addRightClick( callback );
    }

    /***************************************************************************
     * Sets the color of the "Thumb" that represents the position.
     * @param c the new color of the "Thumb"
     **************************************************************************/
    public void setThumbColor( Color c )
    {
        paintable.thumbColor = c;
        component.repaint();
    }

    /***************************************************************************
     * Sets the length of the item containing the indicated position.
     * @param length the length of the item.
     **************************************************************************/
    public void setLength( long length )
    {
        paintable.length = length;
        component.repaint();
    }

    /***************************************************************************
     * Returns the previously set length of the item containing the indicated
     * position.
     * @return the length of the item.
     **************************************************************************/
    public long getLength()
    {
        return paintable.length;
    }

    /***************************************************************************
     * Sets the indicated position.
     * @param position the position to be displayed.
     **************************************************************************/
    public void setPosition( long position )
    {
        paintable.setPosition( position );
        component.repaint();
    }

    /***************************************************************************
     * Returns the previously set indicated position.
     * @return the position being displayed.
     **************************************************************************/
    public long getPosition()
    {
        return paintable.getPosition();
    }

    /***************************************************************************
     * @param position the position of the bookmark to add.
     **************************************************************************/
    public void addBookmark( long position )
    {
        paintable.bookmarks.add( position );
        component.repaint();
    }

    /***************************************************************************
     * @param position the position of the bookmark to remove.
     **************************************************************************/
    public void removeBookmark( long position )
    {
        paintable.bookmarks.remove( position );
        component.repaint();
    }

    /***************************************************************************
     * @return a copy of the list of bookmarks added.
     **************************************************************************/
    public List<Long> getBookmarks()
    {
        return new ArrayList<>( paintable.bookmarks );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearBookmarks()
    {
        paintable.bookmarks.clear();
        component.repaint();
    }

    /***************************************************************************
     * Invokes the callback on all listeners to the position change based on the
     * x-coordinate of the thumb.
     * @param x the x-position in the {@link #component}'s coordinate space that
     * defines the left of the thumb.
     * @return the indicated position represented by the provided position.
     **************************************************************************/
    private long fireThumbMoved( int x )
    {
        long pos = paintable.setThumbPosition( x );
        component.repaint();

        firePositionUpdated( pos );

        return pos;
    }

    /***************************************************************************
     * Invokes the callback on all listeners to the position change.
     * @param position the new indicated position.
     **************************************************************************/
    private void firePositionUpdated( long position )
    {
        posititonListeners.fire( position );
    }

    /***************************************************************************
     * Defines the object that draws the control.
     **************************************************************************/
    private static final class PositionIndicatorPaintable implements IPaintable
    {
        /**  */
        private static final int BM_WIDTH = 4;

        /** The bounds of the thumb. Defaults to zeros. */
        private final Rectangle thumb;
        /**  */
        private final Rectangle track;
        /**  */
        private final List<Long> bookmarks;

        /** The color of the thumb. */
        private Color thumbColor;
        /** The color of the shadow of the thumb. */
        private Color thumbShadow;

        /**  */
        private Color bookmarkColor;
        /**  */
        private Color bookmarkShadow;

        /** The length of the item containing the indicated position. */
        private long length;
        /** The current indicated position. */
        private long position;

        /**
         * Creates a new paintable.
         */
        public PositionIndicatorPaintable()
        {
            this.thumb = new Rectangle();
            this.track = new Rectangle();
            this.bookmarks = new ArrayList<>();

            this.thumbColor = UIProperty.SCROLLBAR_THUMB.getColor();
            this.thumbShadow = UIProperty.SCROLLBAR_THUMBSHADOW.getColor();

            this.bookmarkColor = new Color( 0xFF, 0xFF, 0x00 );
            this.bookmarkShadow = new Color( 0x000000 );

            this.length = 0;
            this.position = 50;
        }

        /**
         * @param x the thumb position in the components coordinate space.
         * @return the indicated position represented by the provided position.
         */
        public long setThumbPosition( int x )
        {
            long pos = calculatePosition( x );

            // LogUtils.printDebug( "Moving thumb to %d", x );
            // int thumbPos = paintable.calculateThumbPosition( pos );
            //
            // if( thumbPos != x )
            // {
            // LogUtils.printDebug(
            // "Wrong: Expected index %d for position %d; Actual %d", x, pos,
            // thumbPos );
            // }

            setPosition( pos );

            return pos;
        }

        /**
         * @param position the indicated position.
         */
        public void setPosition( long position )
        {
            this.position = position;
        }

        /**
         * @return the original position when setting with
         */
        public long getPosition()
        {
            return position;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint( JComponent c, Graphics2D g2 )
        {
            // LogUtils.printDebug( "Drawing position %d", position );

            Rectangle cb = c.getBounds();
            Insets insets = c.getInsets();

            track.x = insets.left + 1;
            track.y = insets.top + 1;
            track.width = cb.width - ( insets.left + insets.right + 2 );
            track.height = cb.height - ( insets.top + insets.bottom + 2 );

            thumb.y = track.y;
            thumb.height = track.height;

            calculateThumb();

            Object aaHint = g2.getRenderingHint(
                RenderingHints.KEY_ANTIALIASING );

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );

            g2.setColor( thumbShadow );
            g2.fillRoundRect( thumb.x, thumb.y, thumb.width, thumb.height, 5,
                5 );

            g2.setColor( thumbColor );
            g2.fillRoundRect( thumb.x + 1, thumb.y + 1, thumb.width - 2,
                thumb.height - 2, 4, 4 );

            int off = ( thumb.width - BM_WIDTH ) / 2;
            for( long pos : bookmarks )
            {
                int idx = calculateThumbPosition( pos ) + off;
                g2.setColor( bookmarkShadow );
                g2.fillRect( idx, thumb.y, BM_WIDTH, thumb.height );

                g2.setColor( bookmarkColor );
                g2.fillRect( idx + 1, thumb.y + 1, BM_WIDTH - 2,
                    thumb.height - 2 );
            }

            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaHint );
        }

        /**
         * 
         */
        private void calculateThumb()
        {
            thumb.width = ( int )( track.width / ( double )length );

            thumb.width = Math.max( thumb.width, 16 );
            thumb.width = Math.min( thumb.width, track.width );

            thumb.x = calculateThumbPosition();
        }

        /**
         * @return the calculated position.
         */
        private int calculateThumbPosition()
        {
            return calculateThumbPosition( getPosition(), length, track.x,
                track.width, thumb.width );
        }

        /**
         * @param position the position being displayed
         * @return the calculated position
         */
        private int calculateThumbPosition( long position )
        {
            int x = calculateThumbPosition( position, length, track.x,
                track.width, thumb.width );

            // this.positionError = position - calculatePosition( x );

            return x;
        }

        /**
         * Gets the indicated position based on the x-coordinate of the thumb.
         * @param x the x-position in the {@link #component}'s coordinate space
         * that defines the left of the thumb.
         * @return the calculated position.
         */
        private long calculatePosition( int x )
        {
            long position = calculatePosition( x, length, track.x, track.width,
                thumb.width );

            return position;
        }

        /**
         * @param position the position within the length.
         * @param length the length of the item containing positions.
         * @param trackx the x position within the component's coordinate space
         * of the start of the track.
         * @param trackWidth the width of the track.
         * @param thumbWidth the width of the thumb.
         * @return the calculated position.
         */
        private static int calculateThumbPosition( long position, long length,
            int trackx, int trackWidth, int thumbWidth )
        {
            int posMax = trackWidth - thumbWidth;

            int posIdx = ( int )Math.round(
                ( position / ( length - 1.0 ) * posMax ) );

            posIdx = Math.max( posIdx, 0 );
            posIdx = Math.min( posIdx, posMax );
            int x = posIdx + trackx;

            // LogUtils.printDebug(
            // "Calculated thumb position %d (x = %d) for position = %d;" +
            // " length = %d; trackx = %d; trackWidth = %d;" +
            // " thumbWidth = %d; posMax = %d",
            // posIdx, x, position, length, trackx, trackWidth, thumbWidth,
            // posMax );

            return x;
        }

        /**
         * @param x the x position in the component's coordinate space for which
         * the position is calculated.
         * @param length the length of the item containing positions.
         * @param trackx the x position within the component's coordinate space
         * of the start of the track.
         * @param trackWidth the width of the track.
         * @param thumbWidth the width of the thumb.
         * @return the calculated position.
         */
        private static long calculatePosition( int x, long length, int trackx,
            int trackWidth, int thumbWidth )
        {
            int posMax = trackWidth - thumbWidth;
            int posIdx = x - trackx;

            posIdx = Math.max( posIdx, 0 );
            posIdx = Math.min( posIdx, posMax );

            long position = Math.round(
                ( double )posIdx / ( double )posMax * ( length - 1.0 ) );

            // position = ( position / unitLength ) * unitLength;

            // LogUtils.printDebug(
            // "Calculated position %d for posIdx = %d (x = %d); length = %d;" +
            // " trackx = %d; trackWidth = %d; thumbWidth = %d;" +
            // " posMax = %d",
            // position, posIdx, x, length, trackx, trackWidth, thumbWidth,
            // posMax );

            return position;
        }

        /**
         * @return a copy of the thumb bounds.
         */
        public Rectangle getThumbBounds()
        {
            return new Rectangle( thumb );
        }
    }

    /***************************************************************************
     * Defines the mouse listener to move and interact with the thumb.
     **************************************************************************/
    private static final class PiMouseListener extends MouseAdapter
    {
        /** The position indicator listened to. */
        private final PositionIndicator indicator;
        /** The descriptor used to create a string for a position. */
        private final IDescriptor<Long> positionDescriptor;
        /**  */
        private final UpdaterList<MouseEvent> rightClickListeners;

        /** The last point at which the mouse was pressed. */
        private Point start;
        /**  */
        private int thumbxStart;
        /**  */
        private int dragx;
        /**  */
        private boolean dragging;

        /**
         * Creates a new listener with the provided indicator and description.
         * @param pi the position indicator listened to.
         * @param descriptor the descriptor used to create a string for a
         * position.
         */
        public PiMouseListener( PositionIndicator pi,
            IDescriptor<Long> descriptor )
        {
            this.indicator = pi;
            this.positionDescriptor = descriptor;
            this.rightClickListeners = new UpdaterList<>();

            this.start = null;
            this.thumbxStart = 0;
            this.dragx = 0;
            this.dragging = false;
        }

        /**
         * Adds a method to be invoked when the tab is right-clicked.
         * @param callback the method to be invoked.
         */
        public void addRightClick( IUpdater<MouseEvent> callback )
        {
            this.rightClickListeners.add( callback );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            Rectangle thumb = indicator.paintable.getThumbBounds();
            if( SwingUtilities.isLeftMouseButton( e ) &&
                !thumb.contains( e.getPoint() ) )
            {
                indicator.fireThumbMoved( e.getX() - thumb.width / 2 );
            }
            else if( RightClickListener.isRightClick( e ) )
            {
                rightClickListeners.fire( e );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) )
            {
                start = e.getPoint();
                Rectangle thumb = indicator.paintable.getThumbBounds();
                thumbxStart = thumb.x;
                dragx = start.x;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseDragged( MouseEvent e )
        {
            if( !SwingUtilities.isLeftMouseButton( e ) )
            {
                return;
            }

            if( start != null &&
                indicator.paintable.getThumbBounds().contains( start ) )
            {
                dragging = true;
            }

            if( dragging && e.getX() != dragx )
            {
                dragx = e.getX();

                int delta = e.getX() - start.x;
                int thumbx = thumbxStart + delta;

                long pos = indicator.fireThumbMoved( thumbx );

                String text = positionDescriptor.getDescription( pos );
                indicator.posLabel.setText( text );

                Point csp = indicator.component.getLocationOnScreen();
                Point msp = e.getLocationOnScreen();

                msp.x = ( int )( csp.x + indicator.component.getWidth() / 2.0 -
                    indicator.positionWindow.getWidth() / 2.0 );
                msp.y = csp.y + indicator.component.getHeight() + 2;

                indicator.positionWindow.setLocation( msp );
                indicator.positionWindow.setVisible( true );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased( MouseEvent e )
        {
            start = null;
            dragging = false;
            indicator.positionWindow.setVisible( false );

            indicator.component.repaint();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseMoved( MouseEvent e )
        {
            if( indicator.paintable.getThumbBounds().contains( e.getPoint() ) )
            {
                indicator.component.setCursor(
                    new Cursor( Cursor.HAND_CURSOR ) );
            }
            else
            {
                indicator.component.setCursor(
                    new Cursor( Cursor.DEFAULT_CURSOR ) );
            }
        }
    }
}
