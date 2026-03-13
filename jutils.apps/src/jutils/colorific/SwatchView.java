package jutils.colorific;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import jutils.core.laf.UIProperty;
import jutils.core.ui.ColorIcon;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SwatchView implements IView<JComponent>
{
    /**  */
    private final JButton component;
    /**  */
    private final ColorIcon icon;

    /**  */
    private final EmptyBorder invisibleBorder;
    /**  */
    private final LineBorder selectedBorder;

    /***************************************************************************
     * 
     **************************************************************************/
    public SwatchView()
    {
        int bdrSize = 2;

        this.icon = new ColorIcon( UIProperty.PANEL_BACKGROUND.getColor(), 32 );
        this.component = new JButton( icon );
        this.invisibleBorder = new EmptyBorder(
            new Insets( bdrSize, bdrSize, bdrSize, bdrSize ) );
        this.selectedBorder = new LineBorder( Color.blue, bdrSize );

        component.setOpaque( false );
        component.setMargin( new Insets( 2, 2, 2, 2 ) );
        component.setEnabled( false );

        setSelected( false );
    }

    /***************************************************************************
     * @param selected
     **************************************************************************/
    public void setSelected( boolean selected )
    {
        if( selected )
        {
            component.setBorder( selectedBorder );
        }
        else
        {
            component.setBorder( invisibleBorder );
        }
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public void setColor( Color c )
    {
        if( c != null )
        {
            component.setEnabled( true );
        }
        else
        {
            c = UIProperty.PANEL_BACKGROUND.getColor();
            component.setEnabled( false );
        }

        icon.setColor( c );

        component.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Color getColor()
    {
        return icon.getColor();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public JButton getView()
    {
        return component;
    }
}
