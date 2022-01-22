package org.jutils.core.ui.fields;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.validation.IValidationField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;
import org.jutils.core.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UsableFormField<T>
    implements IDataFormField<Usable<T>>, IValidationField
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

        field.setUpdater( new DataUpdater<>( this, ( d ) -> usable.data = d ) );

        setValue( new Usable<>() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        usedField.addActionListener( new CheckedListener<>( this ) );

        panel.add( usedField, BorderLayout.WEST );
        panel.add( field.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Usable<T> getValue()
    {
        return usable;
    }

    /***************************************************************************
     * 
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
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Usable<T>> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Usable<T>> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        usedField.setEnabled( editable );
        field.setEditable( editable && usable.isUsed );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void callUpdater()
    {
        if( updater != null )
        {
            updater.update( usable );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return usedField.isSelected() ? field.getValidity() : new Validity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CheckedListener<T> implements ActionListener
    {
        private final UsableFormField<T> field;

        public CheckedListener( UsableFormField<T> field )
        {
            this.field = field;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            field.usable.isUsed = field.usedField.isSelected();
            // LogUtils.printDebug( "UsableFormField: " + field.getName() +
            // " field is " + ( field.usable.isUsed ? "" : "not " ) + "used" );
            field.field.setEditable( field.usable.isUsed );
            // field.field.setValue( field.field.getValue() );

            field.callUpdater();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DataUpdater<T> implements IUpdater<T>
    {
        private final UsableFormField<T> field;
        private final IUpdater<T> updater;

        public DataUpdater( UsableFormField<T> field, IUpdater<T> updater )
        {
            this.field = field;
            this.updater = updater;
        }

        @Override
        public void update( T data )
        {
            updater.update( data );

            field.callUpdater();
        }
    }
}
