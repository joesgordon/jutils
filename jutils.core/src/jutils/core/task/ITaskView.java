package jutils.core.task;

import java.awt.Component;
import java.awt.event.ActionListener;
import jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public interface ITaskView extends IView<Component> {
  /***************************************************************************
   * @param listener
   **************************************************************************/
  void addCancelListener(ActionListener listener);

  /***************************************************************************
   * @param message
   **************************************************************************/
  void signalMessage(String message);

  /***************************************************************************
   * @param percent
   * @return {@code true} if the percent complete changed enough to issue the
   * update; {@code false} otherwise.
   **************************************************************************/
  boolean signalPercent(int percent);

  /***************************************************************************
   * @param error
   **************************************************************************/
  void signalError(TaskError error);
}
