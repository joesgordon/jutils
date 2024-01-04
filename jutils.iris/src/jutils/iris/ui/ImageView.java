package jutils.iris.ui;

import javax.swing.JComponent;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final ImagesView imgsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageView()
    {
        this.imgsView = new ImagesView();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        return imgsView.getView();
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
