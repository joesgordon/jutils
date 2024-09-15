package jutils.hexulator;

import java.awt.Container;
import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import jutils.core.ui.IToolView;
import jutils.hexulator.ui.HexulatorView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexulatorTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Container getView()
    {
        HexulatorView view = new HexulatorView();

        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Translates bytes to different data types";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Hexulator";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return HexulatorIcons.getAppImages();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return HexulatorIcons.getIcon( HexulatorIcons.APP_024 );
    }
}
