package org.jutils.task;

import java.awt.Component;
import java.awt.event.ActionListener;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ITaskView extends IView<Component>
{
    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addCancelListener( ActionListener listener );

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void signalMessage( String message );

    /***************************************************************************
     * @param percent
     * @return {@code true} if the percent complete changed enough to issue the
     * update; {@code false} otherwise.
     **************************************************************************/
    public boolean signalPercent( int percent );

    /***************************************************************************
     * @param error
     **************************************************************************/
    public void signalError( TaskError error );
}
