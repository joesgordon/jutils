package jutils.core.ui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/*******************************************************************************
 *
 ******************************************************************************/
public class AltSplitPane extends JSplitPane
{
    /**  */
    private static final long serialVersionUID = -7212927759119569622L;
    /**  */
    public static final String PROPERTYNAME_DIVIDER_BORDER_VISIBLE = "dividerBorderVisible";
    /**  */
    public boolean borderless = false;
    /**  */
    public boolean borderlessSupported = false;

    /***************************************************************************
     * 
     **************************************************************************/
    public AltSplitPane()
    {
        this( JSplitPane.HORIZONTAL_SPLIT, false, null, null );
    }

    /***************************************************************************
     * @param newOrientation
     **************************************************************************/
    public AltSplitPane( int newOrientation )
    {
        this( newOrientation, false );
    }

    /***************************************************************************
     * @param newOrientation
     * @param newContinuousLayout
     **************************************************************************/
    public AltSplitPane( int newOrientation, boolean newContinuousLayout )
    {
        this( newOrientation, newContinuousLayout, null, null );
    }

    /***************************************************************************
     * @param newOrientation
     * @param newLeftComponent
     * @param newRightComponent
     **************************************************************************/
    public AltSplitPane( int newOrientation, Component newLeftComponent,
        Component newRightComponent )
    {
        this( newOrientation, false, newLeftComponent, newRightComponent );
    }

    /***************************************************************************
     * @param newOrientation
     * @param newContinuousLayout
     * @param newLeftComponent
     * @param newRightComponent
     **************************************************************************/
    public AltSplitPane( int newOrientation, boolean newContinuousLayout,
        Component newLeftComponent, Component newRightComponent )
    {
        super( newOrientation, newContinuousLayout, newLeftComponent,
            newRightComponent );
    }

    /***************************************************************************
     * @param borderLess
     **************************************************************************/
    public void setBorderless( boolean borderLess )
    {
        boolean oldVisibility = isBorderless();

        if( oldVisibility == borderLess )
        {
            return;
        }

        borderless = borderLess;
        firePropertyChange( PROPERTYNAME_DIVIDER_BORDER_VISIBLE, oldVisibility,
            borderLess );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isBorderless()
    {
        return borderless;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Border getBorder()
    {
        if( isBorderless() )
        {
            return BorderFactory.createEmptyBorder();
        }

        return super.getBorder();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void updateUI()
    {
        super.updateUI();
        if( isBorderless() )
        {
            setEmptyDividerBorder();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setEmptyDividerBorder()
    {
        SplitPaneUI splitPaneUI = getUI();
        if( splitPaneUI instanceof BasicSplitPaneUI )
        {
            BasicSplitPaneUI basicUI = ( BasicSplitPaneUI )splitPaneUI;
            basicUI.getDivider().setBorder( BorderFactory.createEmptyBorder() );
        }
    }
}
