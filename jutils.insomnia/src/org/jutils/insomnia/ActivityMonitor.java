package org.jutils.insomnia;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ActivityMonitor
{
    /**  */
    private final Runnable callback;
    /**  */
    // private final KeyEventDispatcher dispatcher;

    /**  */
    private Point lastPoint;

    /***************************************************************************
     * @param callback
     **************************************************************************/
    public ActivityMonitor( Runnable callback )
    {
        this.callback = callback;
        // this.dispatcher = ( e ) -> handleKeyEvent( e );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void connect()
    {
        // KeyboardFocusManager kfManager =
        // KeyboardFocusManager.getCurrentKeyboardFocusManager();
        //
        // kfManager.addKeyEventDispatcher( dispatcher );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        // KeyboardFocusManager kfManager =
        // KeyboardFocusManager.getCurrentKeyboardFocusManager();
        //
        // kfManager.removeKeyEventDispatcher( dispatcher );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void checkMouse()
    {
        PointerInfo info = MouseInfo.getPointerInfo();
        if( info != null )
        {
            Point point = info.getLocation();

            if( !point.equals( this.lastPoint ) )
            {
                callback.run();
            }

            lastPoint = point;
        }
    }

    /***************************************************************************
     * @param e
     * @return
     **************************************************************************/
    // private boolean handleKeyEvent( KeyEvent e )
    // {
    // char c = e.getKeyChar();
    // int code = e.getKeyCode();
    //
    // LogUtils.printDebug( "Typed %c (%X)", c, code );
    //
    // callback.run();
    //
    // return false;
    // }
}
