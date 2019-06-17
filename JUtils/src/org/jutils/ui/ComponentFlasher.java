package org.jutils.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ComponentFlasher
{
    /**  */
    private JComponent comp;
    /**  */
    private Timer warningTimer;
    /**  */
    private Color oldStatusColor;
    /**  */
    private Color newStatusColor;

    /***************************************************************************
     * @param component
     **************************************************************************/
    public ComponentFlasher( JComponent component )
    {
        this( component, Color.red );
    }

    /***************************************************************************
     * @param component
     * @param flashColor
     **************************************************************************/
    public ComponentFlasher( JComponent component, Color flashColor )
    {
        newStatusColor = flashColor;
        comp = component;
        oldStatusColor = component.getBackground();
        warningTimer = new Timer( 500, new FlashListener() );

        component.setOpaque( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stopFlashing()
    {
        warningTimer.stop();
        comp.setBackground( oldStatusColor );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void startFlashing()
    {
        warningTimer.setRepeats( true );
        warningTimer.start();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FlashListener implements ActionListener
    {
        private boolean trigger = false;

        @Override
        public void actionPerformed( ActionEvent e )
        {
            trigger ^= true;

            if( trigger )
            {
                comp.setBackground( newStatusColor );
            }
            else
            {
                comp.setBackground( oldStatusColor );
            }
        }
    }
}
