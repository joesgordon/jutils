package jutils.telemetry.ui.ch09;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch09.TmatsFile;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsFileView implements IDataView<TmatsFile>
{
    /**  */
    private final JComponent view;

    /**  */
    private TmatsFile file;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsFileView()
    {
        this.view = createView();

        setData( new TmatsFile() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel();
        // TODO Auto-generated method stub
        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TmatsFile getData()
    {
        return file;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( TmatsFile data )
    {
        this.file = data;

        // TODO Auto-generated method stub
    }
}
