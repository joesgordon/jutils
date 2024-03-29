package jutils.plot.ui;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import jutils.core.IconConstants;
import jutils.core.io.LogUtils;
import jutils.core.ui.OkDialogView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.event.ItemActionListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.BooleanFormField;
import jutils.core.ui.fields.ColorField;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;
import jutils.plot.model.TextLabel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextLabelField implements IDataFormField<TextLabel>
{
    /**  */
    private final JPanel view;

    /**  */
    private final BooleanFormField visibleField;
    /**  */
    private final StringFormField textField;
    /**  */
    private final Action fontAction;
    /**  */
    private final ColorField colorView;

    /**  */
    private TextLabel label;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public TextLabelField( String name )
    {
        this.visibleField = new BooleanFormField( "Visible" );
        this.textField = new StringFormField( name, 0, null );
        this.fontAction = createFontAction();
        this.colorView = new ColorField( "Color", Color.red, 16, false );

        this.view = createView();

        setValue( new TextLabel() );

        visibleField.setUpdater( ( d ) -> label.visible = d );
        textField.setUpdater( ( d ) -> {
            label.text = d;
            LogUtils.printDebug( "Set text to %s", d );
        } );
        colorView.setUpdater( ( d ) -> label.color = d );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 2 ), 0, 0 );
        panel.add( visibleField.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( textField.getView(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( new JButton( fontAction ), constraints );

        constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 2, 0, 0 ), 0, 0 );
        panel.add( colorView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createFontAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = null;
        icon = IconConstants.getIcon( IconConstants.FONT_16 );
        listener = new FontListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return textField.getName();
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
    public TextLabel getValue()
    {
        return label;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( TextLabel value )
    {
        this.label = value;

        if( value != null )
        {
            visibleField.setValue( value.visible );
            textField.setValue( value.text );
            textField.getView().setFont( value.font );
            colorView.setValue( value.color );
        }
        else
        {
            visibleField.setValue( false );
            textField.setValue( "" );
            colorView.setValue( Color.black );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<TextLabel> updater )
    {
        ;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<TextLabel> getUpdater()
    {
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        textField.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        textField.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return textField.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FontListener
        implements ActionListener, ItemActionListener<Boolean>
    {
        /**  */
        private final TextLabelField field;

        /**  */
        private FontView fontView;

        /**
         * @param field
         */
        public FontListener( TextLabelField field )
        {
            this.field = field;
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            fontView = new FontView();
            OkDialogView okView = new OkDialogView( field.getView(),
                fontView.getView(), ModalityType.DOCUMENT_MODAL );
            JDialog dialog = okView.getView();

            fontView.setData( field.label.font );
            okView.addOkListener( this );

            dialog.setTitle( "Choose Font" );
            dialog.pack();
            dialog.setLocationRelativeTo( field.getView() );
            dialog.setVisible( true );
        }

        /**
         * @{@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            if( event.getItem() )
            {
                field.label.font = fontView.getData();
                field.textField.getView().setFont( field.label.font );

                // field.textField.getView().invalidate();
                // field.getField().validate();
                // field.getField().repaint();
            }
        }
    }
}
