package jutils.hexinator.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import jutils.core.ui.TextHexView;
import jutils.core.ui.model.IView;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexinatorView implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final DataFieldsView fieldsView;
    /**  */
    private final TextHexView textField;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexinatorView()
    {
        this.fieldsView = new DataFieldsView();
        this.textField = new TextHexView();

        this.view = createView();

        view.setMinimumSize( view.getPreferredSize() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int r = 0;

        constraints = new GridBagConstraints( 0, r, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createOrderView(), constraints );
        r++;

        constraints = new GridBagConstraints( 0, r, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( fieldsView.getView(), constraints );
        r++;

        constraints = new GridBagConstraints( 0, r, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator( SwingConstants.HORIZONTAL ), constraints );
        r++;

        constraints = new GridBagConstraints( 0, r, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( textField.getView(), constraints );
        r++;
        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createOrderView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        JLabel label = new JLabel( "Endianness:" );
        JRadioButton leButton = new JRadioButton( "Little" );
        JRadioButton beButton = new JRadioButton( "Big" );

        ButtonGroup bg = new ButtonGroup();

        bg.add( leButton );
        bg.add( beButton );

        leButton.addActionListener(
            ( e ) -> setOrder( ByteOrdering.LITTLE_ENDIAN ) );
        beButton.addActionListener(
            ( e ) -> setOrder( ByteOrdering.BIG_ENDIAN ) );

        beButton.setSelected( true );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( leButton, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( beButton, constraints );

        return panel;
    }

    /***************************************************************************
     * @param order
     **************************************************************************/
    private void setOrder( ByteOrdering order )
    {
        fieldsView.setOrder( order );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
