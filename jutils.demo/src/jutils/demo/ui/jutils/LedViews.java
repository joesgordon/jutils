package jutils.demo.ui.jutils;

import java.awt.Color;

import javax.swing.JComponent;

import jutils.core.ui.LedBooleanLabel;
import jutils.core.ui.LedButton;
import jutils.core.ui.LedColorButton;
import jutils.core.ui.LedLabel;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedViews implements IView<JComponent>
{
    /**  */
    private final LedLabel label;
    /**  */
    private final LedButton button;
    /**  */
    private final LedColorButton colorButton;
    /**  */
    private final LedBooleanLabel booleanLabel;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedViews()
    {
        this.label = new LedLabel();
        this.button = new LedButton();
        this.colorButton = new LedColorButton();
        this.booleanLabel = new LedBooleanLabel();

        label.icon.setIconSize( 32 );
        label.icon.led.setBorderSize( 2 );
        label.icon.setShape( true );
        label.setText( "LedLabel" );
        button.setText( "LedButton" );
        colorButton.setData( Color.red );
        booleanLabel.setValue( false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( "Label", label.getView() );
        form.addField( "Button", button.getView() );
        form.addField( "Color Button", colorButton.getView() );
        form.addField( "Boolean Label", booleanLabel.getView() );

        return form.getView();
    }
}
