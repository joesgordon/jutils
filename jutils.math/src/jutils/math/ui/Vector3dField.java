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
import jutils.math.Vector3d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Vector3dField implements IDataFormField<Vector3d>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationView view;
    /**  */
    private final JPanel panel;

    /**  */
    private final DoubleFormField xField;
    /**  */
    private final DoubleFormField yField;
    /**  */
    private final DoubleFormField zField;

    /**  */
    private final AggregateValidityChangedManager validityManager;

    /**  */
    private Vector3d vec;
    /**  */
    private IUpdater<Vector3d> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public Vector3dField( String name )
    {
        this( name, "" );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public Vector3dField( String name, String units )
    {
        this.name = name;
        this.xField = new DoubleFormField( "X" );
        this.yField = new DoubleFormField( "Y" );
        this.zField = new DoubleFormField( "Z" );

        this.validityManager = new AggregateValidityChangedManager();

        this.vec = null;
        this.updater = null;

        this.panel = createPanel();
        this.view = new ValidationView( this, units, panel );

        validityManager.addField( xField );
        validityManager.addField( yField );
        validityManager.addField( zField );

        this.setValue( new Vector3d( 1.0, 2.0, 3.0 ) );
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
        panel.add( xField.getTextField(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( yField.getTextField(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, m, 0, 0 ), 0, 0 );
        panel.add( zField.getTextField(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Vector3d getValue()
    {
        return vec;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Vector3d value )
    {
        this.vec = value;

        xField.setValue( value.x );
        yField.setValue( value.y );
        zField.setValue( value.z );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Vector3d> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Vector3d> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        xField.setEditable( editable );
        yField.setEditable( editable );
        zField.setEditable( editable );
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
