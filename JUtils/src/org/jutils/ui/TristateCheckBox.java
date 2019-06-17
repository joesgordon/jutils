package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

import org.jutils.data.UIProperty;
import org.jutils.ui.event.ActionAdapter;

/*******************************************************************************
 * Maintenance tip - There were some tricks to getting this code working: <ol>
 * <li>You have to overwite addMouseListener() to do nothing <li>You have to add
 * a mouse event on mousePressed by calling super.addMouseListener() <li>You
 * have to replace the UIActionMap for the keyboard event "pressed" with your
 * own one. <li>You have to remove the UIActionMap for the keyboard event
 * "released". <li>You have to grab focus when the next state is entered,
 * otherwise clicking on the component won't get the focus. <li>You have to make
 * a TristateDecorator as a button model that wraps the original button model
 * and does state management. </ol>
 ******************************************************************************/
public class TristateCheckBox extends JCheckBox
{
    /**  */
    private static final long serialVersionUID = -7151843638250343722L;
    /**  */
    private final TristateDecorator model;

    /***************************************************************************
     * @param text String
     * @param icon Icon
     * @param initial Boolean
     **************************************************************************/
    public TristateCheckBox( String text, Icon icon, Boolean initial )
    {
        super( text, icon );
        this.model = new TristateDecorator( getModel() );

        // Add a listener for when the mouse is pressed
        super.addMouseListener( new MouseAdapter()
        {
            @Override
            public void mousePressed( MouseEvent e )
            {
                grabFocus();
                model.nextState();
            }
        } );

        // Reset the keyboard action map
        ActionMap map = new ActionMapUIResource();
        map.put( "pressed", new ActionAdapter( ( e ) -> {
            grabFocus();
            model.nextState();
        }, "pressed", null ) );
        map.put( "released", null );
        SwingUtilities.replaceUIActionMap( this, map );

        // set the model to the adapted model
        setModel( model );
        setState( initial );

        setIcon( new TristateIcon( UIProperty.CHECKBOX_ICON.getIcon() ) );
    }

    /***************************************************************************
     * @param text String
     * @param initial Boolean
     **************************************************************************/
    public TristateCheckBox( String text, Boolean initial )
    {
        this( text, null, initial );
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public TristateCheckBox( String text )
    {
        this( text, null );
    }

    /***************************************************************************
     *
     **************************************************************************/
    public TristateCheckBox()
    {
        this( null );
    }

    /***************************************************************************
     * No one may add mouse listeners, not even Swing!
     **************************************************************************/
    @Override
    public synchronized void addMouseListener( MouseListener l )
    {
        ;
    }

    /***************************************************************************
     * Set the new state to either SELECTED, NOT_SELECTED or DONT_CARE. If state
     * == null, it is treated as DONT_CARE.
     * @param state Boolean
     **************************************************************************/
    public void setState( Boolean state )
    {
        model.setState( state );
        this.repaint();
    }

    /***************************************************************************
     * Return the current state, which is determined by the selection status of
     * the model.
     * @return Boolean
     **************************************************************************/
    public Boolean getState()
    {
        return model.getState();
    }

    private static final class TristateIcon implements Icon
    {
        /**  */
        private Icon defaultIcon = null;

        /**  */
        private Color checkColor = null;

        /***************************************************************************
         * @param defaultIcon
         **************************************************************************/
        public TristateIcon( Icon defaultIcon )
        {
            this.defaultIcon = defaultIcon;
            checkColor = UIManager.getColor( "CheckBox.check" );
            if( checkColor == null )
            {
                checkColor = Color.black;
            }
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public int getIconHeight()
        {
            return defaultIcon.getIconHeight();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public int getIconWidth()
        {
            return defaultIcon.getIconWidth();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void paintIcon( Component c, Graphics g, int x, int y )
        {
            TristateCheckBox icon = ( TristateCheckBox )c;
            defaultIcon.paintIcon( c, g, x, y );
            if( icon.getState() == null )
            {
                drawBox( g, x, y );
            }
        }

        /***************************************************************************
         * @param g
         * @param x
         * @param y
         **************************************************************************/
        protected void drawBox( Graphics g, int x, int y )
        {
            int controlSize = getIconWidth();
            int gap = 3;

            g.setColor( checkColor );
            g.fill3DRect( x + gap, y + gap, controlSize - 2 * gap,
                controlSize - 2 * gap, true );
        }
    }

    /*******************************************************************************
     * Exactly which Design Pattern is this? Is it an Adapter, a Proxy or a
     * Decorator? In this case, my vote lies with the Decorator, because we are
     * extending functionality and "decorating" the original model with a more
     * powerful model.
     ******************************************************************************/
    private static final class TristateDecorator implements ButtonModel
    {
        /**  */
        private final ButtonModel other;

        /**  */
        private Boolean state = null;

        /***********************************************************************
         * @param other ButtonModel
         **********************************************************************/
        public TristateDecorator( ButtonModel other )
        {
            this.other = other;
        }

        /***********************************************************************
         * @param state Boolean
         **********************************************************************/
        public void setState( Boolean state )
        {
            this.state = state;

            // other.setArmed( false );
            // setPressed( false );
            if( state == null )
            {
                // other.setArmed( true );
                // setPressed( true );
                setSelected( true );
            }
            else if( state.booleanValue() )
            {
                // other.setArmed( false );
                // setPressed( false );
                setSelected( true );
            }
            else
            {
                // other.setArmed( false );
                // setPressed( false );
                setSelected( false );
            }
        }

        /***********************************************************************
         * The current state is embedded in the selection / armed state of the
         * model. We return the SELECTED state when the checkbox is selected but
         * not armed, DONT_CARE state when the checkbox is selected and armed
         * (grey) and NOT_SELECTED when the checkbox is deselected.
         * @return Boolean
         **********************************************************************/
        public Boolean getState()
        {
            return state;
            // if( isSelected() && !isArmed() )
            // {
            // // normal black tick
            // return Boolean.TRUE;
            // }
            // else if( isSelected() && isArmed() )
            // {
            // // don't care grey tick
            // return null;
            // }
            // else
            // {
            // // normal deselected
            // return Boolean.FALSE;
            // }
        }

        /***********************************************************************
         * We rotate between NOT_SELECTED, SELECTED and DONT_CARE.
         **********************************************************************/
        public void nextState()
        {
            Boolean current = getState();
            if( current == null )
            {
                setState( Boolean.FALSE );
            }
            else if( current.equals( Boolean.FALSE ) )
            {
                setState( Boolean.TRUE );
            }
            else if( current.equals( Boolean.TRUE ) )
            {
                setState( null );
            }
            else
            {
                throw new RuntimeException(
                    "Boolean that is neither null, TRUE, or FALSE: " +
                        current );
            }
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void setArmed( boolean b )
        {
        }

        /***************************************************************************
         * disable focusing on the component when it is not enabled.
         **************************************************************************/
        @Override
        public void setEnabled( boolean b )
        {
            // setFocusable(b);
            other.setEnabled( b );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public boolean isArmed()
        {
            return other.isArmed();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public boolean isSelected()
        {
            return other.isSelected();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public boolean isEnabled()
        {
            return other.isEnabled();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public boolean isPressed()
        {
            return other.isPressed();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public boolean isRollover()
        {
            return other.isRollover();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void setSelected( boolean b )
        {
            other.setSelected( b );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void setPressed( boolean b )
        {
            other.setPressed( b );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void setRollover( boolean b )
        {
            other.setRollover( b );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void setMnemonic( int key )
        {
            other.setMnemonic( key );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public int getMnemonic()
        {
            return other.getMnemonic();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void setActionCommand( String s )
        {
            other.setActionCommand( s );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public String getActionCommand()
        {
            return other.getActionCommand();
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void setGroup( ButtonGroup group )
        {
            other.setGroup( group );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void addActionListener( ActionListener l )
        {
            other.addActionListener( l );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void removeActionListener( ActionListener l )
        {
            other.removeActionListener( l );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void addItemListener( ItemListener l )
        {
            other.addItemListener( l );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void removeItemListener( ItemListener l )
        {
            other.removeItemListener( l );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void addChangeListener( ChangeListener l )
        {
            other.addChangeListener( l );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public void removeChangeListener( ChangeListener l )
        {
            other.removeChangeListener( l );
        }

        /***************************************************************************
         * 
         **************************************************************************/
        @Override
        public Object [] getSelectedObjects()
        {
            return other.getSelectedObjects();
        }
    }

}
