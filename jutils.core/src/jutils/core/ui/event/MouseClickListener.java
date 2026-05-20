package jutils.core.ui.event;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import jutils.core.INamedItem;
import jutils.core.data.BitFieldInfo;
import jutils.core.data.INamedBitFlag;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MouseClickListener extends MouseEventsListener
{
    /***************************************************************************
     * @param isPrimary
     * @param handler
     **************************************************************************/
    public MouseClickListener( IClickHandler handler )
    {
        super( new ClickCallback( handler ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ClickCallback implements IMouseCallback
    {
        /**  */
        private final IClickHandler handler;

        /**  */
        private boolean isPopup;

        /**
         * @param handler
         */
        public ClickCallback( IClickHandler handler )
        {
            this.handler = handler;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void handleEvent( MouseEventType type, MouseEvent event )
        {
            if( type == MouseEventType.PRESSED )
            {
                this.isPopup = event.isPopupTrigger();
            }
            else if( type == MouseEventType.RELEASED )
            {
                this.isPopup = isPopup || event.isPopupTrigger();
            }
            else if( type == MouseEventType.CLICKED )
            {
                this.isPopup = isPopup || event.isPopupTrigger();

                MouseButton button = MouseButton.fromEvent( event, isPopup );
                int count = event.getClickCount();
                Point point = event.getPoint();
                int modifiers = event.getModifiersEx();

                handler.handleClick( button, count, point, modifiers );

                this.isPopup = false;
            }
            else
            {
                return;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IClickHandler
    {
        /**
         * @param button the button that was clicked.
         * @param count the number of times the button was clicked.
         * @param point the point relative to the Source where the button was
         * clicked.
         * @param modifiers the {@link ExtendedModifier extended modifiers}.
         */
        public void handleClick( MouseButton button, int count, Point point,
            int modifiers );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum MouseButton implements INamedItem
    {
        /**  */
        NONE( "None" ),
        /**  */
        PRIMARY( "Primary" ),
        /**  */
        SECONDARY( "Secondary" ),
        /**  */
        MIDDLE( "Middle" ),
        /**  */
        BUTTON4( "Button 4" ),
        /**  */
        BUTTON5( "Button 5" ),
        /**  */
        BUTTON6( "Button 6" ),
        /**  */
        BUTTON7( "Button 7" ),
        /**  */
        BUTTON8( "Button 8" ),
        /**  */
        BUTTON9( "Button 9" ),
        /**  */
        BUTTON10( "Button 10" ),
        /**  */
        OTHER( "Other" );

        /**  */
        public final String name;

        /**
         * @param name
         */
        private MouseButton( String name )
        {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }

        /**
         * @param event
         * @param isPopup
         * @return
         */
        public static MouseButton fromEvent( MouseEvent event, boolean isPopup )
        {
            int buttonNum = event.getButton();

            if( buttonNum == MouseEvent.NOBUTTON )
            {
                return MouseButton.NONE;
            }

            if( SwingUtilities.isMiddleMouseButton( event ) )
            {
                return MouseButton.MIDDLE;
            }

            if( SwingUtilities.isLeftMouseButton( event ) )
            {
                if( isPopup )
                {
                    return MouseButton.SECONDARY;
                }
                return MouseButton.PRIMARY;
            }

            if( SwingUtilities.isRightMouseButton( event ) )
            {
                if( isPopup )
                {
                    return MouseButton.SECONDARY;
                }
                return MouseButton.PRIMARY;
            }

            switch( buttonNum )
            {
                case 4:
                    return MouseButton.BUTTON4;

                case 5:
                    return MouseButton.BUTTON5;

                case 6:
                    return MouseButton.BUTTON6;

                case 7:
                    return MouseButton.BUTTON7;

                case 8:
                    return MouseButton.BUTTON8;

                case 9:
                    return MouseButton.BUTTON9;

                case 10:
                    return MouseButton.BUTTON10;
            }

            return MouseButton.OTHER;
        }
    }

    /***************************************************************************
     * Defines the bit flags for extended modifiers.
     **************************************************************************/
    public static enum ExtendedModifier implements INamedBitFlag
    {
        /**
         * Left or right {@code SHIFT} keys.
         * @see InputEvent#SHIFT_DOWN_MASK
         */
        SHIFT( 6, "Shift" ),
        /**
         * Left or right {@code CTRL} keys.
         * @see InputEvent#CTRL_DOWN_MASK
         */
        CONTROL( 7, "Control" ),
        /**
         * The <a href="https://en.wikipedia.org/wiki/Meta_key">{@code Meta}</a>
         * key.
         * @see InputEvent#META_DOWN_MASK
         */
        META( 8, "Meta" ),
        /**
         * Left or right {@code ALT} keys.
         * @see InputEvent#ALT_DOWN_MASK
         */
        ALT( 9, "Alt" ),
        /**
         * Alternate Graphics key, which is primarily found on non-US keyboards
         * (like European layouts).
         * @see InputEvent#ALT_GRAPH_DOWN_MASK
         */
        ALT_GRAPH( 13, "Alternate Graphics" ),;

        /**  */
        private final BitFieldInfo info;

        /**
         * @param bit
         * @param name
         */
        private ExtendedModifier( int bit, String name )
        {
            this.info = new BitFieldInfo( bit, name );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getBit()
        {
            return info.getStartBit();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getMask()
        {
            return info.getMask();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return info.name;
        }
    }
}
