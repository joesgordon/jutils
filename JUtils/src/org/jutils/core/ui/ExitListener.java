package org.jutils.core.ui;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.*;

import javax.swing.Action;

import org.jutils.core.IconConstants;
import org.jutils.core.ui.event.ActionAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExitListener implements ActionListener
{
    /**  */
    private final Window win;

    /***************************************************************************
     * @param win
     **************************************************************************/
    public ExitListener( Window win )
    {
        this.win = win;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        doDefaultCloseOperation( win );
    }

    /***************************************************************************
     * @param win
     * @return
     **************************************************************************/
    public static Action createStandardExitAction( Window win )
    {
        Action action;

        action = new ActionAdapter( new ExitListener( win ), "Exit",
            IconConstants.getIcon( IconConstants.CLOSE_16 ) );

        action.putValue( Action.MNEMONIC_KEY, ( int )'x' );

        return action;
    }

    /***************************************************************************
     * @param win
     **************************************************************************/
    public static void doDefaultCloseOperation( Window win )
    {
        WindowEvent wev = new WindowEvent( win, WindowEvent.WINDOW_CLOSING );
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( wev );
    }
}
