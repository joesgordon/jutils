package jutils.core.ui.fields;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import jutils.core.io.parsers.StringLengthParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringAreaFormField implements IDataFormField<String>
{
    /**  */
    private final JTextArea textField;
    /**  */
    private final ParserFormField<String> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public StringAreaFormField( String name )
    {
        this( name, false );
    }

    /***************************************************************************
     * @param name
     * @param fillBoth
     **************************************************************************/
    public StringAreaFormField( String name, boolean fillBoth )
    {
        JScrollPane pane = new JScrollPane();
        StringLengthParser parser = new StringLengthParser( 0, null );
        IDescriptor<String> descriptor = ( d ) -> d;

        this.textField = new JTextArea();
        this.field = new ParserFormField<>( name, parser, textField, descriptor,
            pane, null, fillBoth );

        pane.setViewportView( textField );
        pane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        textField.setLineWrap( true );
        textField.setColumns( 20 );
        textField.setRows( 5 );
        textField.setFont( new Font( "Courier New", Font.PLAIN, 18 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextArea getTextArea()
    {
        return textField;
    }
}
