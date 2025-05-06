package jutils.core.ui.times;

import javax.swing.JComponent;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MomentyView implements IView<JComponent>
{
    /**  */
    private final TimesView timesView;

    /***************************************************************************
     * 
     **************************************************************************/
    public MomentyView()
    {
        this.timesView = new TimesView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
