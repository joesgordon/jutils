package org.jutils.ui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LedColorButton implements IDataView<Color>
{
    /**  */
    private static final Color DEFAULT_COLOR = Color.green;
    /**  */
    private static final int DEFAULT_SIZE = 16;

    /**  */
    private final LedIcon icon;
    /**  */
    private final JButton button;

    /**  */
    private IUpdater<Color> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public LedColorButton()
    {
        this( DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param size
     **************************************************************************/
    public LedColorButton( int size )
    {
        this( DEFAULT_COLOR, size );
    }

    /***************************************************************************
     * @param color
     **************************************************************************/
    public LedColorButton( Color color )
    {
        this( color, DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param color
     * @param size
     **************************************************************************/
    public LedColorButton( Color color, int size )
    {
        this.icon = new LedIcon( color, size );
        this.button = new JButton( icon );

        button.addActionListener( ( e ) -> handlePress() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handlePress()
    {
        Color c = JColorChooser.showDialog( getView(), "Choose Color",
            icon.getColor() );

        if( c != null )
        {
            setData( c );

            if( updater != null )
            {
                updater.update( icon.getColor() );
            }
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Color color )
    {
        icon.setColor( color );
        String text = String.format( "%02X%02X%02X:%02X", color.getRed(),
            color.getGreen(), color.getBlue(), color.getAlpha() );
        button.setText( text );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Color getData()
    {
        return icon.getColor();
    }

    public void setUpdater( IUpdater<Color> updater )
    {
        this.updater = updater;
    }

    public IUpdater<Color> getUpdater()
    {
        return updater;
    }

    public void setEditable( boolean editable )
    {
        button.setEnabled( editable );
    }
}
