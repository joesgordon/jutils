package jutils.core.ui.fields;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import jutils.core.ui.ColorIcon;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorField implements IDataFormField<Color>
{
    /**  */
    private final String name;
    /**  */
    private final JButton button;
    /**  */
    private final ColorIcon icon;

    /**  */
    private final ValidityListenerList validityListeners;

    /**  */
    private boolean showDescription;
    /**  */
    private IUpdater<Color> updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public ColorField( String name )
    {
        this( name, Color.red, true );
    }

    /***************************************************************************
     * @param name
     * @param c
     * @param showDescription
     **************************************************************************/
    public ColorField( String name, Color c, boolean showDescription )
    {
        this( name, c, 32, showDescription );
    }

    /***************************************************************************
     * @param name
     * @param c
     * @param size
     * @param showDescription
     **************************************************************************/
    public ColorField( String name, Color c, int size, boolean showDescription )
    {
        this.name = name;
        this.icon = new ColorIcon( c, size );
        this.button = new JButton( icon );
        this.validityListeners = new ValidityListenerList();
        this.showDescription = showDescription;

        // Dimension dim = button.getPreferredSize();
        // dim.width = dim.height;
        // button.setPreferredSize( dim );
        // button.setMinimumSize( dim );
        // button.setMaximumSize( dim );

        button.addActionListener( ( e ) -> showChooser() );

        setValue( c );
    }

    private void showChooser()
    {
        Color c = JColorChooser.showDialog( getView(), "Choose new color",
            getValue() );

        if( c != null )
        {
            setValue( c );
            fireUpdater( c );
        }
    }

    private void fireUpdater( Color c )
    {
        if( updater != null )
        {
            updater.update( c );
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
    public Color getValue()
    {
        return icon.getColor();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Color color )
    {
        icon.setColor( color );
        if( showDescription )
        {
            String text = String.format( "%02X%02X%02X:%02X", color.getRed(),
                color.getGreen(), color.getBlue(), color.getAlpha() );
            button.setText( text );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Color> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Color> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        button.setEnabled( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityListeners.addListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityListeners.removeListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityListeners.getValidity();
    }
}
