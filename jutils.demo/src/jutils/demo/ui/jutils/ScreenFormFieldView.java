package jutils.demo.ui.jutils;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.ScreenFormField;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ScreenFormFieldView implements IView<JComponent>
{
    /**  */
    private final ScreenFormField screenField;

    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public ScreenFormFieldView()
    {
        this.screenField = new ScreenFormField( "Screen" );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( screenField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
