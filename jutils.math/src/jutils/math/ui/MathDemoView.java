package jutils.math.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jutils.core.ui.StandardFormView;
import jutils.core.ui.model.IView;
import jutils.math.IMatrix;
import jutils.math.Matrix;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MathDemoView implements IView<JComponent>
{
    /**  */
    private final JTabbedPane view;

    /**  */
    private final Vector3dField vec3dField;
    /**  */
    private final QuaternionField quatField;
    /**  */
    private final MatrixView matrixView;

    /***************************************************************************
     * 
     **************************************************************************/
    public MathDemoView()
    {
        this.vec3dField = new Vector3dField(
            Vector3dField.class.getSimpleName() );
        this.quatField = new QuaternionField(
            QuaternionField.class.getSimpleName() );

        this.matrixView = new MatrixView();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JTabbedPane createView()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Fields", createFieldsTab() );
        tabs.addTab( "Matrix", createMatrixTab() );

        return tabs;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createFieldsTab()
    {
        StandardFormView form = new StandardFormView();

        form.addField( vec3dField );
        form.addField( quatField );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createMatrixTab()
    {
        IMatrix m = new Matrix( 5, 4 );

        matrixView.setData( m );

        return matrixView.getView();
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
