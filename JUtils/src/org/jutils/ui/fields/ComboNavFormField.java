package org.jutils.ui.fields;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ComboNavFormField<T> implements IDataFormField<T>
{
    /**  */
    private final ComboFormField<T> comboField;
    /**  */
    private final JButton previousButton;
    /**  */
    private final JButton nextButton;
    /**  */
    private final JComponent view;

    /***************************************************************************
     * @param name
     * @param items
     **************************************************************************/
    public ComboNavFormField( String name, T [] items )
    {
        this( name, Arrays.asList( items ), null );
    }

    /***************************************************************************
     * @param name
     * @param items
     **************************************************************************/
    public ComboNavFormField( String name, List<T> items )
    {
        this( name, items, null );
    }

    /***************************************************************************
     * @param name
     * @param items
     * @param descriptor
     **************************************************************************/
    public ComboNavFormField( String name, T [] items,
        IDescriptor<T> descriptor )
    {
        this( name, Arrays.asList( items ), descriptor );
    }

    /***************************************************************************
     * @param name
     * @param items
     * @param descriptor
     **************************************************************************/
    public ComboNavFormField( String name, List<T> items,
        IDescriptor<T> descriptor )
    {
        this.comboField = new ComboFormField<>( name, items, descriptor );
        this.previousButton = new JButton();
        this.nextButton = new JButton();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int fm = StandardFormView.DEFAULT_FIELD_MARGIN;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, fm ), 0, 0 );
        panel.add( comboField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, fm ), 0, 0 );
        panel.add( previousButton, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( nextButton, constraints );

        previousButton.setAction( createNavAction( -1 ) );
        previousButton.setText( "" );

        nextButton.setAction( createNavAction( 1 ) );
        nextButton.setText( "" );

        return panel;
    }

    /***************************************************************************
     * @param offset
     **************************************************************************/
    private void navigateField( int offset )
    {
        int idx = comboField.getSelectedIndex();
        int nextIdx = idx + offset;
        int maxIdx = comboField.getView().getItemCount() - 1;

        if( nextIdx < 0 )
        {
            nextIdx = maxIdx;
        }
        else if( nextIdx > maxIdx )
        {
            nextIdx = 0;
        }

        comboField.getView().setSelectedIndex( nextIdx );
    }

    /***************************************************************************
     * @param offset
     * @return
     **************************************************************************/
    private Action createNavAction( int offset )
    {
        ActionListener listener = ( e ) -> navigateField( offset );
        String name = offset < 0 ? "Previous Item" : "Next Item";
        String iconName = offset < 0 ? IconConstants.NAV_PREVIOUS_16
            : IconConstants.NAV_NEXT_16;
        Icon icon = IconConstants.getIcon( iconName );

        return new ActionAdapter( listener, name, icon );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T getValue()
    {
        return comboField.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        comboField.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        comboField.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<T> getUpdater()
    {
        return comboField.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        comboField.setEditable( editable );
        previousButton.setEnabled( editable );
        nextButton.setEnabled( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return comboField.getName();
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
    public void addValidityChanged( IValidityChangedListener l )
    {
        comboField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        comboField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return comboField.getValidity();
    }
}
