package jutils.core.ui.fields;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;
import jutils.core.utils.Usable;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class UsableFormField<T> implements IDataFormField<Usable<T>>
{
    /**  */
    private final JPanel panel;
    /**  */
    private final JCheckBox usedField;
    /**  */
    private final IDataFormField<T> field;

    /**  */
    private Usable<T> usable;
    /**  */
    private IUpdater<Usable<T>> updater;

    /***************************************************************************
     * @param field
     **************************************************************************/
    public UsableFormField( IDataFormField<T> field )
    {
        this.field = field;
        this.usedField = new JCheckBox();
        this.panel = createView();

        field.setUpdater( ( d ) -> handleDataUpdated( d ) );

        setValue( new Usable<>() );
    }

    /***************************************************************************
     * @param d
     **************************************************************************/
    private void handleDataUpdated( T d )
    {
        usable.data = d;

        fireUpdater();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        usedField.addActionListener( ( e ) -> handleChecked() );

        panel.add( usedField, BorderLayout.WEST );
        panel.add( field.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleChecked()
    {
        usable.isUsed = usedField.isSelected();
        field.setEditable( usable.isUsed );

        // LogUtils.printDebug( "UsableFormField: " + field.getName() +
        // " field is " + ( field.usable.isUsed ? "" : "not " ) + "used" );

        fireUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void fireUpdater()
    {
        if( updater != null )
        {
            updater.update( usable );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Usable<T> getValue()
    {
        return usable;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Usable<T> value )
    {
        this.usable = value;

        if( value != null )
        {
            usedField.setSelected( value.isUsed );
            field.setValue( value.data );
            field.setEditable( value.isUsed );
        }
        else
        {
            usedField.setSelected( false );
            field.setValue( null );
            field.setEditable( false );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Usable<T>> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Usable<T>> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        usedField.setEnabled( editable );
        field.setEditable( editable && usable.isUsed );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return usedField.isSelected() ? field.getValidity() : new Validity();
    }
}
