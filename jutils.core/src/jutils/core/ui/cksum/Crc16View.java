package jutils.core.ui.cksum;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jutils.core.io.cksum.Crc16;
import jutils.core.ui.TextHexView;
import jutils.core.ui.TitleView;
import jutils.core.ui.fields.HexShortFormField;
import jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class Crc16View implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final Crc16ConfigView configView;
    /**  */
    private final TextHexView messageView;
    /**  */
    private final HexShortFormField valueField;

    /***************************************************************************
     * 
     **************************************************************************/
    public Crc16View()
    {
        this.configView = new Crc16ConfigView();
        this.messageView = new TextHexView();
        this.valueField = new HexShortFormField( "Value" );

        this.view = createView();

        valueField.setEditable( false );

        messageView.addEnterListener( ( m ) -> calcCrc( m ) );
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    private void calcCrc( byte [] m )
    {
        Crc16 crc = new Crc16( configView.getData() );

        short value = crc.calculate( m );

        valueField.setValue( value );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.setBorder( new EmptyBorder( 8, 8, 8, 8 ) );

        panel.add( createForm(), BorderLayout.WEST );
        panel.add( messageView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        TitleView configTitle = new TitleView( "Config", configView.getView() );
        TitleView valueTitle = new TitleView( "Value", valueField.getView() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 8 ), 0, 0 );
        panel.add( configTitle.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 0, 0, 8 ), 0, 0 );
        panel.add( valueTitle.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        // TODO Auto-generated method stub
        return view;
    }
}
