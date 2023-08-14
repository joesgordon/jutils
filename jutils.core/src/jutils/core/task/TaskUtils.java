package jutils.core.task;

import java.awt.Component;
import java.awt.Window;

import jutils.core.OptionUtils;
import jutils.core.SwingUtils;
import jutils.core.ui.MessageExceptionView;
import jutils.core.ui.VerboseMessageView;

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

            OptionUtils.showErrorMessage( parent, view.getView(), error.name );
        }
        else
        {
            OptionUtils.showErrorMessage( parent, error.message, error.name );
        }
    }
}
