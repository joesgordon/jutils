package org.jutils.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jutils.ui.model.IComponentView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CardPanel implements IComponentView
{
    /**  */
    private final Map<Component, String> stringMap;
    /**  */
    private final CardLayout layout;
    /**  */
    private final JPanel panel;

    /***************************************************************************
     * 
     **************************************************************************/
    public CardPanel()
    {
        this.layout = new CardLayout();
        this.panel = new JPanel( layout );
        this.stringMap = new HashMap<Component, String>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return panel;
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void addCard( Component comp )
    {
        String str = Integer.toString( panel.getComponentCount() );

        stringMap.put( comp, str );

        panel.add( comp, str );
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void showCard( Component comp )
    {
        String str = stringMap.get( comp );

        if( str == null )
        {
            throw new IllegalArgumentException( "Card not found!" );
        }

        layout.show( panel, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void next()
    {
        layout.next( panel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void last()
    {
        layout.last( panel );
    }
}
