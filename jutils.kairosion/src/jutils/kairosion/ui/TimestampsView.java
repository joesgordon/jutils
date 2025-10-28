package jutils.kairosion.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.time.TimeUtils;
import jutils.core.ui.ComponentView;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that allows a user to define times in a variety of ways.
 ******************************************************************************/
public class TimestampsView implements IDataView<LocalDateTime>
{
    /** The view that contains all the fields in this view. */
    private final JPanel view;
    /**  */
    private final ComponentView formView;
    /**  */
    private final StandardFormView form;

    /**  */
    private final List<ITimestampField<?>> fields;

    /**  */
    private LocalDateTime time;

    /***************************************************************************
     * Creates a new times view.
     **************************************************************************/
    public TimestampsView()
    {
        this.formView = new ComponentView();
        this.form = new StandardFormView();

        this.fields = new ArrayList<>();

        this.view = createView();

        setData( LocalDateTime.MIN );
    }

    /***************************************************************************
     * Creates the main panel for this view.
     * @return main panel.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( formView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * Creates the toolbar for this view.
     * @return the toolbar for this view.
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createNowAction() );

        return toolbar;
    }

    /***************************************************************************
     * Creates the action for setting fields to now.
     * @return the action for setting fields to now.
     **************************************************************************/
    private Action createNowAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.TODAY_16 );
        ActionListener listener = ( e ) -> setData( TimeUtils.getUtcNow() );
        return new ActionAdapter( listener, "Now", icon );
    }

    /***************************************************************************
     * @param field
     * @param <T>
     * @return
     **************************************************************************/
    public <T> void addField( ITimestampField<T> field )
    {
        IUpdater<ITimestampField<?>> updater = ( f ) -> handleFieldUpdated( f );

        field.setUpdater( updater );
        form.addField( field.getField() );

        formView.setComponent( form.getView() );
    }

    /***************************************************************************
     * @param field
     * @param times
     **************************************************************************/
    private void handleFieldUpdated( ITimestampField<?> field )
    {
        // LocalDateTime time = field.updateDateTime(
        // field.getField().getValue() );
        //
        // for( ITimestampField<?> f : fields )
        // {
        // if( f != field )
        // {
        // f.setDateTime( time );
        // }
        // }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime getData()
    {
        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( LocalDateTime data )
    {
        this.time = data;

        for( ITimestampField<?> f : fields )
        {
            f.setDateTime( time );
        }
    }
}
