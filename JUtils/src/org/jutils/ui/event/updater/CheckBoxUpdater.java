package org.jutils.ui.event.updater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CheckBoxUpdater implements ActionListener
{
    /**  */
    public final IUpdater<Boolean> updater;

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public CheckBoxUpdater( IUpdater<Boolean> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        JCheckBox jcb = ( JCheckBox )e.getSource();
        updater.update( jcb.isSelected() );
    }
}
