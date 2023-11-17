package jutils.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ConvForm<T> implements IView<JComponent>
{
    /**  */
    private final StandardFormView view;
    /**  */
    private final List<ConvField<?, T>> fields;

    /**  */
    private IUpdater<T> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public ConvForm()
    {
        this.view = new StandardFormView();
        this.fields = new ArrayList<>();

        this.updater = null;
    }

    /***************************************************************************
     * @param field
     **************************************************************************/
    public void addField( ConvField<?, T> field )
    {
        fields.add( field );
        view.addField( field );

        field.setUpdater( new FormUpdater<>( this, field ) );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setValue( T data )
    {
        for( ConvField<?, T> cf : fields )
        {
            cf.setUpdaterEnabled( false );
            cf.setValue( data );
            cf.setUpdaterEnabled( true );
        }
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    private void setUpdatersEnabled( boolean enabled )
    {
        for( ConvField<?, T> cf : fields )
        {
            cf.setUpdaterEnabled( enabled );
        }
    }

    /***************************************************************************
     * @param data
     * @param field
     **************************************************************************/
    private void updateExcept( T data, ConvField<?, T> field )
    {
        // LogUtils.printDebug( "updatingExcept" );

        for( ConvField<?, T> cf : fields )
        {
            if( cf != field )
            {
                cf.setValue( data );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FormUpdater<T> implements IUpdater<T>
    {
        private final ConvForm<T> form;
        private final ConvField<?, T> field;

        public FormUpdater( ConvForm<T> form, ConvField<?, T> icf )
        {
            this.form = form;
            this.field = icf;
        }

        @Override
        public void update( T data )
        {
            form.setUpdatersEnabled( false );

            form.updateExcept( data, field );

            form.setUpdatersEnabled( true );

            if( form.updater != null )
            {
                form.updater.update( data );
            }
        }
    }
}
