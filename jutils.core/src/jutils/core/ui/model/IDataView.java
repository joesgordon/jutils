package jutils.core.ui.model;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.Utils;
import jutils.core.ValidationException;
import jutils.core.io.xs.XsUtils;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.event.ItemActionListener;

/*******************************************************************************
 * Defines a view that allows for display and optionally editing of data.
 * @param <D> the type of the data to be displayed/edited.
 ******************************************************************************/
public interface IDataView<D> extends IView<Component>
{
    /***************************************************************************
     * Returns the data as seen in the view. This function may optionally build
     * this data when called, or return the original data instance (meaning the
     * data was updated without creating a new instance).
     * @return the data as seen in the view.
     **************************************************************************/
    public D getData();

    /***************************************************************************
     * Sets the components in this view to the fields within the provided data.
     * @param data the data to be displayed.
     **************************************************************************/
    public void setData( D data );

    /***************************************************************************
     * @param <D>
     * @param view
     * @return
     **************************************************************************/
    public static <D> JComponent createCopyPastePanel( IDataView<D> view )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JToolBar toolbar = new JToolBar();
        ItemActionListener<D> pasteListener = ( e ) -> view.setData(
            e.getItem() );

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createCopyAction( view ) );
        SwingUtils.addActionToToolbar( toolbar,
            createPasteAction( pasteListener ) );

        panel.add( toolbar, BorderLayout.NORTH );
        panel.add( view.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * Creates an action to copy data from the provided view to the system
     * clipboard.
     * @param <T> the type of data to be copied.
     * @param view the view containing the data to be copied.
     * @return the created action.
     **************************************************************************/
    public static <T> Action createCopyAction( IDataView<T> view )
    {
        ActionListener listener = ( e ) -> {
            T data = view.getData();
            try
            {
                String str = XsUtils.writeObjectXStream( data );
                Utils.setClipboardText( str );
            }
            catch( IOException ex )
            {
                throw new RuntimeException( ex );
            }
            catch( ValidationException ex )
            {
                throw new RuntimeException( ex );
            }
        };
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_COPY_16 );
        Action action = new ActionAdapter( listener, "Copy", icon );

        return action;
    }

    /***************************************************************************
     * Creates an action to paste data from the system clipboard to the provided
     * listener.
     * @param <T> the type of data to be pasted.
     * @param itemListener the listener to be called when the action is invoked.
     * @return the created action.
     **************************************************************************/
    public static <T> Action createPasteAction(
        ItemActionListener<T> itemListener )
    {
        ActionListener listener = ( e ) -> {
            try
            {
                String str = Utils.getClipboardText();
                T data = XsUtils.readObjectXStream( str );

                itemListener.actionPerformed(
                    new ItemActionEvent<T>( itemListener, data ) );
            }
            catch( ValidationException ex )
            {
            }
        };
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_PASTE_16 );
        Action action = new ActionAdapter( listener, "Paste", icon );

        return action;
    }
}
