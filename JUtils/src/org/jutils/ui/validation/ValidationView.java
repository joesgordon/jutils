package org.jutils.ui.validation;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * Displays only an {@link IValidationField} unless the field is invalid; in
 * which case, the error is also displayed in a non-editable text field.
 ******************************************************************************/
public class ValidationView implements IView<JPanel>
{
    /** The view that contains all the components. */
    private final JPanel view;
    /** The validation field to be displayed. */
    private final IValidationField field;
    /** The view of the validation field. */
    private final Component fieldView;
    /** Any units to be shown; only visible when units are specified. */
    private final JLabel unitsField;
    /** The field that displays validation errors; only visible when invalid. */
    private final JTextField errorField;
    /** The constraints for the error field. Only keep one for add/removal. */
    private final GridBagConstraints errorConstraints;

    /***************************************************************************
     * Creates a new view with the supplied field and no units.
     * @param field the validation field to be displayed.
     **************************************************************************/
    public ValidationView( IValidationField field )
    {
        this( field, null );
    }

    /***************************************************************************
     * Creates a new view with the supplied field and units.
     * @param field the validation field to be displayed.
     * @param units the units to be displayed; units field is not visible if
     * {@code null}.
     **************************************************************************/
    public ValidationView( IValidationField field, String units )
    {
        this( field, units, field.getView() );
    }

    /***************************************************************************
     * Creates a new view with the supplied field, units, and component.
     * @param field the validation field.
     * @param units the units to be displayed; units field is not visible if
     * {@code null}.
     * @param fieldView the component containing (or from) the provided field.
     **************************************************************************/
    public ValidationView( IValidationField field, String units,
        Component fieldView )
    {
        this( field, units, fieldView, false );
    }

    /***************************************************************************
     * @param field
     * @param units
     * @param fieldView
     * @param fillBoth
     **************************************************************************/
    public ValidationView( IValidationField field, String units,
        Component fieldView, boolean fillBoth )
    {
        this.field = field;
        this.fieldView = fieldView;

        this.errorField = new JTextField();
        this.unitsField = units == null ? null : new JLabel( units );
        this.errorConstraints = new GridBagConstraints( 0, 1, 2, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 0, 0, 0 ), 0, 0 );

        Dimension dim = errorField.getPreferredSize();
        dim.width = field.getView().getPreferredSize().width;

        errorField.setText( "ERROR: " + field.getValidity().reason );
        errorField.setPreferredSize( dim );
        errorField.setEditable( false );

        this.view = createView( fillBoth );

        // LogUtils.printDebug( "Adding validity changed listner" );
        field.addValidityChanged( ( v ) -> handleValidityChanged( v ) );

        setErrorFieldVisible( !field.getValidity().isValid );
    }

    /***************************************************************************
     * Creates the view and adds the components.
     * @param fillBoth
     * @return the main component that represents this view.
     **************************************************************************/
    private JPanel createView( boolean fillBoth )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        int fieldFill = fillBoth ? GridBagConstraints.BOTH
            : GridBagConstraints.HORIZONTAL;
        double fieldWeightY = fillBoth ? 1.0 : 0.0;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, fieldWeightY,
            GridBagConstraints.WEST, fieldFill, new Insets( 0, 0, 0, 0 ), 0,
            0 );
        panel.add( fieldView, constraints );

        if( unitsField != null )
        {
            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 4, 0, 0 ), 0, 0 );
            panel.add( unitsField, constraints );
        }

        return panel;
    }

    /***************************************************************************
     * @param validity
     **************************************************************************/
    private void handleValidityChanged( Validity validity )
    {
        // LogUtils.printDebug( "Validity: " + validity.toString() );
        String errText = validity.isValid ? "" : "ERROR: " + validity.reason;

        setErrorFieldVisible( !validity.isValid );
        errorField.setText( errText );
    }

    /***************************************************************************
     * Returns the validation field with which this class was initialized.
     * @return the validation field displayed.
     **************************************************************************/
    public IValidationField getField()
    {
        return field;
    }

    /***************************************************************************
     * Hides the error field if false; shows otherwise.
     * @param editable indicates the error field should be hidden if true.
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        setErrorFieldVisible( editable && !field.getValidity().isValid );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * Shows or hides the error field according to the supplied parameter.
     * @param visible shows the field when {@code true}, hides otherwise.
     **************************************************************************/
    private void setErrorFieldVisible( boolean visible )
    {
        if( visible )
        {
            view.add( errorField, errorConstraints.clone() );
        }
        else
        {
            view.remove( errorField );
        }

        view.revalidate();
        view.repaint();
    }
}
