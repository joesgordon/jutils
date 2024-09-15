package jutils.core.ui;

import java.awt.Color;

import javax.swing.JButton;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class LedButton implements IView<JButton>
{
    /**  */
    private final LedIcon icon;
    /**  */
    private final JButton button;

    /**  */
    private Color onColor;
    /**  */
    private Color offColor;
    /**  */
    private IUpdater<Boolean> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedButton()
    {
        this.onColor = LedBooleanLabel.DEFAULT_TRUE_COLOR;
        this.offColor = LedBooleanLabel.DEFAULT_FALSE_COLOR;

        this.icon = new LedIcon( offColor );
        this.button = new JButton( icon );

        button.addActionListener( ( e ) -> handlePress() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<Boolean> updater )
    {
        this.updater = updater;
    }
}
