package jutils.core.ui.cksum;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.IconConstants;
import jutils.core.OptionUtils;
import jutils.core.io.cksum.Crc16.Crc16Algorithm;
import jutils.core.io.cksum.Crc16.Crc16Config;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ButtonedFormField;
import jutils.core.ui.fields.HexShortFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class Crc16ConfigView implements IDataView<Crc16Config>
{
    /**  */
    private final JComponent view;

    /**  */
    private final HexShortFormField polynomialField;
    /**  */
    private final HexShortFormField initialField;
    /**  */
    private final BooleanFormField reflectInField;
    /**  */
    private final BooleanFormField reflectOutField;
    /**  */
    private final HexShortFormField xoroutField;

    /**  */
    private Crc16Config config;

    /***************************************************************************
     * 
     **************************************************************************/
    public Crc16ConfigView()
    {
        int cols = 4;
        this.polynomialField = new HexShortFormField( "Polynomial", null,
            cols );
        this.initialField = new HexShortFormField( "Initial Value", null,
            cols );
        this.reflectInField = new BooleanFormField( "Reflect In" );
        this.reflectOutField = new BooleanFormField( "Reflect Out" );
        this.xoroutField = new HexShortFormField( "XOR Out", null, cols );

        this.view = createView();

        polynomialField.setUpdater( ( d ) -> this.config.polynomial = d );
        initialField.setUpdater( ( d ) -> this.config.initial = d );
        reflectInField.setUpdater( ( d ) -> this.config.reflectIn = d );
        reflectOutField.setUpdater( ( d ) -> this.config.reflectOut = d );
        xoroutField.setUpdater( ( d ) -> this.config.xorout = d );

        setData( new Crc16Config() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        ButtonedFormField<Short> polyField;

        polyField = new ButtonedFormField<Short>( polynomialField );
        polyField.button.setIcon(
            IconConstants.getIcon( IconConstants.CONFIG_16 ) );
        polyField.button.setToolTipText( "Choose Algorithm" );
        polyField.button.addActionListener( ( e ) -> handleChooseAlgorithm() );

        form.addField( polyField );
        form.addField( initialField );
        form.addField( reflectInField );
        form.addField( reflectOutField );
        form.addField( xoroutField );

        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleChooseAlgorithm()
    {
        Crc16Algorithm algo = null;

        algo = OptionUtils.showComboDialog( getView(), "Choose Algorithm",
            "Algorithm", "Algorithm", Crc16Algorithm.values(),
            Crc16Algorithm.CCITT_FALSE );

        if( algo != null )
        {
            setData( algo.getConfig() );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Crc16Config getData()
    {
        return this.config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Crc16Config data )
    {
        this.config = data;

        polynomialField.setValue( config.polynomial );
        initialField.setValue( config.initial );
        reflectInField.setValue( config.reflectIn );
        reflectOutField.setValue( config.reflectOut );
        xoroutField.setValue( config.xorout );
    }
}
