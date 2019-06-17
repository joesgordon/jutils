package org.jutils.ui.hex;

import java.awt.*;
import java.util.List;

import javax.swing.JPanel;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.BitsFormField;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.validation.*;
import org.jutils.utils.BitArray;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitArrayView implements IDataView<BitArray>, IValidationField
{
    /**  */
    private final JPanel view;
    /**  */
    private final BitsFormField bitsField;
    /**  */
    private final HexBytesField leftField;
    /**  */
    private final HexBytesField rightField;

    /**  */
    private final AggregateValidityChangedManager validityManager;
    /**  */
    private final BitsUpdater bitsUpdater;
    /**  */
    private final LeftUpdater leftUpdater;
    /**  */
    private final RightUpdater rightUpdater;

    /**  */
    private IUpdater<BitArray> updater;

    /**  */
    private BitArray bits;

    /***************************************************************************
     * 
     **************************************************************************/
    public BitArrayView( List<byte []> quickList )
    {
        this.bitsField = new BitsFormField( "Bits" );
        this.leftField = new HexBytesField( quickList );
        this.rightField = new HexBytesField( quickList );
        this.view = createView();

        this.validityManager = new AggregateValidityChangedManager();
        this.bitsUpdater = new BitsUpdater( this );
        this.leftUpdater = new LeftUpdater( this );
        this.rightUpdater = new RightUpdater( this );

        this.updater = null;

        bitsField.getView().setToolTipText( "Bits Text" );
        leftField.getView().setToolTipText( "Left Aligned Hex Text" );
        rightField.getView().setToolTipText( "Right Aligned Hex Text" );

        setData( new BitArray() );

        validityManager.addField( bitsField );
        validityManager.addField( leftField );
        validityManager.addField( rightField );

        bitsField.setUpdater( bitsUpdater );
        leftField.setUpdater( leftUpdater );
        rightField.setUpdater( rightUpdater );
    }

    /***************************************************************************
     * @return
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
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<BitArray> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public BitArray getData()
    {
        return bits;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( BitArray bits )
    {
        this.bits = bits;

        bitsField.setValue( bits );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityManager.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityManager.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityManager.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static abstract class AbstractUpdater<T> implements IUpdater<T>
    {
        private boolean enabled = true;

        public void setEnabled( boolean enabled )
        {
            this.enabled = enabled;
        }

        @Override
        public final void update( T data )
        {
            if( enabled )
            {
                updateValue( data );
            }
        }

        public abstract void updateValue( T value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class BitsUpdater extends AbstractUpdater<BitArray>
    {
        private final BitArrayView view;

        public BitsUpdater( BitArrayView view )
        {
            this.view = view;
        }

        @Override
        public void updateValue( BitArray bits )
        {
            view.bits.set( bits );

            byte [] aligned;

            aligned = view.bits.getLeftAligned();
            view.leftUpdater.setEnabled( false );
            view.leftField.setValue( aligned );
            view.leftUpdater.setEnabled( true );

            aligned = view.bits.getRightAligned();
            view.rightUpdater.setEnabled( false );
            view.rightField.setValue( aligned );
            view.rightUpdater.setEnabled( true );

            if( view.updater != null )
            {
                view.updater.update( view.bits );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class LeftUpdater extends AbstractUpdater<byte []>
    {
        private final BitArrayView view;

        public LeftUpdater( BitArrayView view )
        {
            this.view = view;
        }

        @Override
        public void updateValue( byte [] data )
        {
            view.bits.set( data );

            view.bitsUpdater.setEnabled( false );
            view.bitsField.setValue( view.bits );
            view.bitsUpdater.setEnabled( true );

            view.rightUpdater.setEnabled( false );
            view.rightField.setValue( view.bits.getRightAligned() );
            view.rightUpdater.setEnabled( true );

            if( view.updater != null )
            {
                view.updater.update( view.bits );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RightUpdater extends AbstractUpdater<byte []>
    {
        private final BitArrayView view;

        public RightUpdater( BitArrayView view )
        {
            this.view = view;
        }

        @Override
        public void updateValue( byte [] data )
        {
            view.bits.set( data );

            view.bitsUpdater.setEnabled( false );
            view.bitsField.setValue( view.bits );
            view.bitsUpdater.setEnabled( true );

            view.leftUpdater.setEnabled( false );
            view.leftField.setValue( view.bits.getLeftAligned() );
            view.leftUpdater.setEnabled( true );

            if( view.updater != null )
            {
                view.updater.update( view.bits );
            }
        }
    }
}
