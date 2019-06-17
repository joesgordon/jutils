package org.jutils.task;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JOptionPane;

import org.jutils.SwingUtils;
import org.jutils.ui.MessageExceptionView;
import org.jutils.ui.VerboseMessageView;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class TaskUtils
{
    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private TaskUtils()
    {
    }

    /***************************************************************************
     * @param comp
     * @param error
     **************************************************************************/
    public static void displayError( Component comp, TaskError error )
    {
        Window parent = SwingUtils.getComponentsWindow( comp );

        if( error.exception != null )
        {
            MessageExceptionView.showExceptionDialog( parent, error.message,
                error.name, error.exception );
            error.exception.printStackTrace();
        }
        else if( error.description != null )
        {
            VerboseMessageView view = new VerboseMessageView();

            view.setMessages( error.message, error.description );

            JOptionPane.showMessageDialog( parent, view.getView(), error.name,
                JOptionPane.ERROR_MESSAGE );
        }
        else
        {
            JOptionPane.showMessageDialog( parent, error.message, error.name,
                JOptionPane.ERROR_MESSAGE );
        }
    }
}
