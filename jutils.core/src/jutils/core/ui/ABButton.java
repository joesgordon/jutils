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
    private Icon aIcon;
    /**  */
    private IABCallback aCallback;

    /**  */
    private String bText;
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
                setButton();
            }
        }
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setAText( String text )
    {
        this.aText = text;
        setButton();
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setAIcon( Icon icon )
    {
        this.aIcon = icon;
        setButton();
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
        setButton();
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setBIcon( Icon icon )
    {
        this.bIcon = icon;
        setButton();
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
    private void setButton()
    {
        if( isAState )
        {
            button.setText( aText );
            button.setIcon( aIcon );
        }
        else
        {
            button.setText( bText );
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
            setButton();
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
