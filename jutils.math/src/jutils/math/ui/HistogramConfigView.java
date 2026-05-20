package jutils.math.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HistogramConfigView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final IntegerFormField binCountField;

    /***************************************************************************
     * 
     **************************************************************************/
    public HistogramConfigView()
    {
        this.binCountField = new IntegerFormField( "Bin Count" );
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createForm(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( binCountField );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        // TODO Auto-generated method stub
        return view;
    }
}
