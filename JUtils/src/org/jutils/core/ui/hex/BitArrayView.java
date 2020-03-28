package org.jutils.core.ui.hex;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import org.jutils.core.ui.fields.BitsFormField;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.validation.AggregateValidityChangedManager;
import org.jutils.core.ui.validation.IValidationField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;
import org.jutils.core.utils.BitArray;

/*******************************************************************************
 * Defines the UI for editing a {@link BitArray}: a bit field (which can be any
 * number of bits), a left-shifted hexadecimal field (which represents the bits
 * shifted to the leftmost byte and zero-padded on the right), and a
 * right-shifted hexadecimal field (which represents the bits shifted to the
 * rightmost byte and zero-padded on the left). E.g. the bits {@code 0b11001}
 * can be represented as {@code 0xC8} ({@code 0b11001000}) if left-shifted and
 * {@code 0x19} if right-shifted ({@code 0b00011001}.
 ******************************************************************************/
public class BitArrayView implements IDataView<BitArray>, IValidationField
{
    /** Contains the components which display the {@link BitArray}. */
    private final JPanel view;
    /** Displays the bits of the array. */
    private final BitsFormField bitsField;
    /** Displays the bits as a left-align hexadecimal value. */
    private final HexBytesField leftField;
    /** Displays the bits as a right-align hexadecimal value. */
    private final HexBytesField rightField;

    /** The manager combining the validity of all components. */
    private final AggregateValidityChangedManager validityManager;

    /**  */
    private BitArray bits;

    /***************************************************************************
     * @param quickList
     **************************************************************************/
    public BitArrayView( List<byte []> quickList )
    {
        this.bitsField = new BitsFormField( "Bits" );
        this.leftField = new HexBytesField( "Left", quickList );
        this.rightField = new HexBytesField( "Right", quickList );
        this.view = createView();

        this.validityManager = new AggregateValidityChangedManager();

        bitsField.getView().setToolTipText( "Bits Text" );
        leftField.getView().setToolTipText( "Left Aligned Hex Text" );
        rightField.getView().setToolTipText( "Right Aligned Hex Text" );

        setData( new BitArray() );

        validityManager.addField( bitsField );
        validityManager.addField( leftField );
        validityManager.addField( rightField );

        bitsField.setUpdater( ( d ) -> handleBitsChanged( d ) );
        leftField.setUpdater( ( d ) -> handleLeftChanged( d ) );
        rightField.setUpdater( ( d ) -> handleRightChanged( d ) );
    }

    /***************************************************************************
     * @param bits
     **************************************************************************/
    private void handleBitsChanged( BitArray bits )
    {
        this.bits.set( bits );

        byte [] aligned;

        aligned = bits.getLeftAligned();
        leftField.setValue( aligned );

        aligned = bits.getRightAligned();
        rightField.setValue( aligned );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void handleLeftChanged( byte [] data )
    {
        bits.set( data );

        bitsField.setValue( bits );
        rightField.setValue( bits.getRightAligned() );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void handleRightChanged( byte [] data )
    {
        bits.set( data );

        bitsField.setValue( bits );
        leftField.setValue( bits.getLeftAligned() );
    }

    /***************************************************************************
     * @return the new main panel for this view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 2, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 8, 0 ), 0, 0 );
        panel.add( bitsField.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.5, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 8 ), 0, 0 );
        panel.add( leftField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.5, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( rightField.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public BitArray getData()
    {
        return bits;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( BitArray bits )
    {
        this.bits = bits;

        bitsField.setValue( bits );
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
