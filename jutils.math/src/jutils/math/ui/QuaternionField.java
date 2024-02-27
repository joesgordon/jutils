package jutils.math.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.DoubleFormField;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.validation.AggregateValidityChangedManager;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.ValidationView;
import jutils.core.ui.validation.Validity;
import jutils.math.Quaternion;

/*******************************************************************************
 * 
 ******************************************************************************/
public class QuaternionField implements IDataFormField<Quaternion>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationView view;
    /**  */
    private final JPanel panel;

    /**  */
    private final DoubleFormField iField;
    /**  */
    private final DoubleFormField jField;
    /**  */
    private final DoubleFormField kField;
    /**  */
    private final DoubleFormField rField;

    /**  */
    private final AggregateValidityChangedManager validityManager;

    /**  */
    private Quaternion vec;
    /**  */
    private IUpdater<Quaternion> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public QuaternionField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public QuaternionField( String name, String units )
    {
        this.name = name;
        this.iField = new DoubleFormField( "I" );
        this.jField = new DoubleFormField( "J" );
        this.kField = new DoubleFormField( "K" );
        this.rField = new DoubleFormField( "Real" );

        this.validityManager = new AggregateValidityChangedManager();

        this.vec = null;
        this.updater = null;

        this.panel = createPanel();
        this.view = new ValidationView( this, units, panel );

        validityManager.addField( iField );
        validityManager.addField( jField );
        validityManager.addField( kField );
        validityManager.addField( rField );

        this.setValue( new Quaternion( 0.0, 1.0, 2.0, 3.0 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int m = StandardFormView.DEFAULT_FIELD_MARGIN;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( iField.getTextField(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( jField.getTextField(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( kField.getTextField(), constraints );

        constraints = new GridBagConstraints( 3, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( rField.getTextField(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Quaternion getValue()
    {
        return vec;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Quaternion value )
    {
        this.vec = value;

        iField.setValue( value.i );
        jField.setValue( value.j );
        kField.setValue( value.k );
        rField.setValue( value.r );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Quaternion> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Quaternion> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        iField.setEditable( editable );
        jField.setEditable( editable );
        kField.setEditable( editable );
        rField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return this.name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return this.view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityManager.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityManager.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityManager.getValidity();
    }
}
