package org.jutils.ui;

import javax.swing.JToolBar;

import org.jutils.SwingUtils;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JGoodiesToolBar extends JToolBar
{
    /**  */
    private static final long serialVersionUID = -6534005087551056674L;

    /***************************************************************************
     * 
     **************************************************************************/
    public JGoodiesToolBar()
    {
        super();

        init();
    }

    /***************************************************************************
     * @param orientation
     **************************************************************************/
    public JGoodiesToolBar( int orientation )
    {
        super( orientation );

        init();
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public JGoodiesToolBar( String name )
    {
        super( name );

        init();
    }

    /***************************************************************************
     * @param name
     * @param orientation
     **************************************************************************/
    public JGoodiesToolBar( String name, int orientation )
    {
        super( name, orientation );

        init();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void init()
    {
        SwingUtils.setToolbarDefaults( this );

        putClientProperty( Options.HEADER_STYLE_KEY, HeaderStyle.BOTH );
    }
}
