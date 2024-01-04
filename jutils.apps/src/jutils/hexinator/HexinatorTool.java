package jutils.hexinator;

import java.awt.Container;
import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import jutils.core.ui.IToolView;
import jutils.hexinator.ui.HexinatorView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexinatorTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Container getView()
    {
        HexinatorView view = new HexinatorView();

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
        return "Hexinator";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
