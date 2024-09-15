package jutils.core.ui.event;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class WindowCloseListener extends WindowAdapter
{
    /**  */
    private final Runnable onClose;

    /***************************************************************************
     * @param onClose
     **************************************************************************/
    public WindowCloseListener( Runnable onClose )
    {
        this.onClose = onClose;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void windowClosing( WindowEvent e )
    {
        onClose.run();
    }
}
