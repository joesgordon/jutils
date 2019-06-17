package org.jutils.ui;

import java.awt.Color;

import javax.swing.JButton;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IView;

public class LedButton implements IView<JButton>
{
    private final LedIcon icon;
    private final JButton button;

    private Color onColor;
    private Color offColor;
    private IUpdater<Boolean> updater;

    public LedButton()
    {
        this.onColor = LedLabel.DEFAULT_ON_COLOR;
        this.offColor = LedLabel.DEFAULT_OFF_COLOR;

        this.icon = new LedIcon( offColor );
        this.button = new JButton( icon );

        button.addActionListener( ( e ) -> handlePress() );
    }

    private void handlePress()
    {
        boolean isOn = !icon.getColor().equals( onColor );

        setLit( isOn );

        if( updater != null )
        {
            updater.update( isOn );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JButton getView()
    {
        return button;
    }

    /***************************************************************************
     * @param isOn
     **************************************************************************/
    public void setLit( boolean isOn )
    {
        setColor( isOn ? onColor : offColor );
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public void setColor( Color color )
    {
        icon.setColor( color );
        button.repaint();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        button.setText( text );
    }

    /***************************************************************************
     * @param isOn
     * @param text
     **************************************************************************/
    public void setStatus( boolean isOn, String text )
    {
        setLit( isOn );
        setText( text );
    }

    public void setUpdater( IUpdater<Boolean> updater )
    {
        this.updater = updater;
    }
}
