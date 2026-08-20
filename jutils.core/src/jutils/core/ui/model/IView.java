package jutils.core.ui.model;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.border.Border;

/*******************************************************************************
 * Represents a generic method of storing a view.
 * @param <T> the type of {@link Component} stored by this view.
 ******************************************************************************/
public interface IView<T extends Component>
{
    /***************************************************************************
     * Returns a control or panel that represents this view. For performance
     * reasons, it is recommended that the view is created previously to calling
     * this function.
     * @return the previously built view.
     **************************************************************************/
    public T getView();

    /***************************************************************************
     * @param view
     * @param border
     **************************************************************************/
    public static void setBorder( IView<? extends JComponent> view,
        Border border )
    {
        JComponent v = view.getView();
        v.setBorder( border );
    }
}
