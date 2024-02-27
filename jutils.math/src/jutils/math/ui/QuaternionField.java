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
    private final DoubleFormField rField;
    /**  */
    private final DoubleFormField iField;
    /**  */
    private final DoubleFormField jField;
    /**  */
    private final DoubleFormField kField;

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
        this.rField = new DoubleFormField( "Real" );
        this.iField = new DoubleFormField( "I" );
        this.jField = new DoubleFormField( "J" );
        this.kField = new DoubleFormField( "K" );

        this.validityManager = new AggregateValidityChangedManager();

        this.vec = null;
        this.updater = null;

        this.panel = createPanel();
        this.view = new ValidationView( this, units, panel );

        validityManager.addField( rField );
        validityManager.addField( iField );
        validityManager.addField( jField );
        validityManager.addField( kField );

        this.setValue( new Quaternion( 0.0, 1.0, 2.0, 3.0 ) );

        rField.setUpdater( ( d ) -> {
            vec.r = d;
            handleFieldUpdated();
        } );
        iField.setUpdater( ( d ) -> {
            vec.i = d;
            handleFieldUpdated();
        } );
        jField.setUpdater( ( d ) -> {
            vec.j = d;
            handleFieldUpdated();
        } );
        jField.setUpdater( ( d ) -> {
            vec.k = d;
            handleFieldUpdated();
        } );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleFieldUpdated()
    {
        if( updater != null )
        {
            updater.update( vec );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int m = StandardFormView.DEFAULT_FIELD_MARGIN;

        int c = 0;

        constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( rField.getTextField(), constraints );

        constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( iField.getTextField(), constraints );

        constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( jField.getTextField(), constraints );

        constraints = new GridBagConstraints( c++, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( kField.getTextField(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param format
     **************************************************************************/
    public void setFormat( String format )
    {
        rField.setFormat( format );
        iField.setFormat( format );
        jField.setFormat( format );
        kField.setFormat( format );
    }

    /***************************************************************************
     * @param iTip
     * @param jTip
     * @param kTip
     * @param rTip
     **************************************************************************/
    public void setTooltips( String rTip, String iTip, String jTip,
        String kTip )
    {
        rField.getTextField().setToolTipText( rTip );
        iField.getTextField().setToolTipText( iTip );
        jField.getTextField().setToolTipText( jTip );
        kField.getTextField().setToolTipText( kTip );
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

        rField.setValue( value.r );
        iField.setValue( value.i );
        jField.setValue( value.j );
        kField.setValue( value.k );
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
        rField.setEditable( editable );
        iField.setEditable( editable );
        jField.setEditable( editable );
        kField.setEditable( editable );
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
