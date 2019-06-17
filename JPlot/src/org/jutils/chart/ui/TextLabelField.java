package org.jutils.chart.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.chart.model.TextLabel;
import org.jutils.io.parsers.StringLengthParser;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.event.*;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextLabelField implements IDataFormField<TextLabel>
{
    /**  */
    private final String name;
    /**  */
    private final JPanel view;

    /**  */
    private final BooleanFormField visibleField;
    /**  */
    private final ValidationTextField textField;
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
        this.name = name;

        this.visibleField = new BooleanFormField( "Visible" );
        this.textField = new ValidationTextField();
        this.fontAction = createFontAction();
        this.colorView = new ColorField( "Color", Color.red, 16, false );

        this.view = createView();

        setValue( new TextLabel() );

        ITextValidator itv;

        visibleField.setUpdater( ( b ) -> label.visible = b );

        itv = new DataTextValidator<>( new StringLengthParser( 0, null ),
            ( s ) -> label.text = s );
        textField.setValidator( itv );

        colorView.setUpdater( ( c ) -> label.color = c );
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
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public TextLabel getValue()
    {
        return label;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( TextLabel value )
    {
        this.label = value;

        if( value != null )
        {
            visibleField.setValue( value.visible );
            textField.setText( value.text );
            textField.getView().setFont( value.font );
            colorView.setValue( value.color );
        }
        else
        {
            visibleField.setValue( false );
            textField.setText( "" );
            colorView.setValue( Color.black );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<TextLabel> updater )
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<TextLabel> getUpdater()
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        textField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        textField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
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
        private final TextLabelField field;
        private FontView fontView;

        public FontListener( TextLabelField field )
        {
            this.field = field;
        }

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
