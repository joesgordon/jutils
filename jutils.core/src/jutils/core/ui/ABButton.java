package jutils.core.ui;

import javax.swing.Icon;
import javax.swing.JButton;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ABButton implements IView<JButton>
{
    /**  */
    private String aText;
    /**  */
    private String aTooltip;
    /**  */
    private Icon aIcon;
    /**  */
    private IABCallback aCallback;

    /**  */
    private String bText;
    /**  */
    private String bTooltip;
    /**  */
    private Icon bIcon;
    /**  */
    private IABCallback bCallback;

    /**  */
    public final JButton button;

    /**  */
    private boolean isAState;

    /***************************************************************************
     * 
     **************************************************************************/
    public ABButton()
    {
        this( "", null, null, "", null, null );

        isAState = true;
    }

    /***************************************************************************
     * @param aText
     * @param aIcon
     * @param aCallback
     * @param bText
     * @param bIcon
     * @param bCallback
     **************************************************************************/
    public ABButton( String aText, Icon aIcon, IABCallback aCallback,
        String bText, Icon bIcon, IABCallback bCallback )
    {
        this.aText = aText;
        this.aIcon = aIcon;
        this.aCallback = aCallback;

        this.bText = bText;
        this.bIcon = bIcon;
        this.bCallback = bCallback;

        this.button = new JButton( aText, aIcon );

        this.isAState = true;

        button.addActionListener( ( e ) -> invokeButtonPressed() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeButtonPressed()
    {
        IABCallback callback = null;

        if( isAState )
        {
            callback = aCallback;
        }
        else
        {
            callback = bCallback;
        }

        if( callback != null )
        {
            if( callback.run() )
            {
                isAState = !isAState;
                updateButton();
            }
        }
    }

    /***************************************************************************
     * @param aText
     * @param bText
     **************************************************************************/
    public void setText( String aText, String bText )
    {
        this.aText = aText;
        this.bText = bText;
        updateButton();
    }

    /***************************************************************************
     * @param aTooltip
     * @param bTooltip
     **************************************************************************/
    public void setTooltip( String aTooltip, String bTooltip )
    {
        this.aTooltip = aTooltip;
        this.bTooltip = bTooltip;
        updateButton();
    }

    /***************************************************************************
     * @param aIcon
     * @param bIcon
     **************************************************************************/
    public void setIcon( Icon aIcon, Icon bIcon )
    {
        this.aIcon = aIcon;
        this.bIcon = bIcon;
        updateButton();
    }

    /***************************************************************************
     * @param aCallback
     * @param bCallback
     **************************************************************************/
    public void setIcon( IABCallback aCallback, IABCallback bCallback )
    {
        this.aCallback = aCallback;
        this.bCallback = bCallback;
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setAText( String text )
    {
        this.aText = text;
        updateButton();
    }

    /***************************************************************************
     * @param tooltip
     **************************************************************************/
    public void setATooltip( String tooltip )
    {
        this.aTooltip = tooltip;
        updateButton();
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setAIcon( Icon icon )
    {
        this.aIcon = icon;
        updateButton();
    }

    /***************************************************************************
     * @param callback
     **************************************************************************/
    public void setACallback( IABCallback callback )
    {
        this.aCallback = callback;
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setBText( String text )
    {
        this.bText = text;
        updateButton();
    }

    /***************************************************************************
     * @param tooltip
     **************************************************************************/
    public void setBTooltip( String tooltip )
    {
        this.bTooltip = tooltip;
        updateButton();
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setBIcon( Icon icon )
    {
        this.bIcon = icon;
        updateButton();
    }

    /***************************************************************************
     * @param callback
     **************************************************************************/
    public void setBCallback( IABCallback callback )
    {
        this.bCallback = callback;
    }

    /***************************************************************************
     * @param isAState
     **************************************************************************/
    private void updateButton()
    {
        if( isAState )
        {
            button.setText( aText );
            button.setToolTipText( aTooltip );
            button.setIcon( aIcon );
        }
        else
        {
            button.setText( bText );
            button.setToolTipText( bTooltip );
            button.setIcon( bIcon );
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
     * @param isAState
     **************************************************************************/
    public void setState( boolean isAState )
    {
        if( this.isAState != isAState )
        {
            this.isAState = isAState;
            updateButton();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IABCallback
    {
        /**
         * @return {@code true} when the button should be switched or
         * {@code false} to retain its current side's values
         */
        public boolean run();
    }
}
