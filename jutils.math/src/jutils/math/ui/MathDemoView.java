package jutils.math.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MathDemoView implements IView<JComponent>
{
    /**  */
    private final JTabbedPane view;

    /**  */
    private final Vector3dField vec3dField;

    /***************************************************************************
     * 
     **************************************************************************/
    public MathDemoView()
    {
        this.vec3dField = new Vector3dField(
            Vector3dField.class.getSimpleName() );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JTabbedPane createView()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Fields", createFieldsTab() );

        return tabs;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFieldsTab()
    {
        StandardFormView form = new StandardFormView();

        form.addField( vec3dField );

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
