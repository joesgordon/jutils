package org.jutils.ui;

import java.awt.Container;
import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * Defines a GUI tool to be displayed as a collection of like tools.
 ******************************************************************************/
public interface IToolView extends IView<Container>
{
    /***************************************************************************
     * Returns a description of what the function the UI enables the user to
     * perform.
     * @return
     **************************************************************************/
    public String getDescription();

    /***************************************************************************
     * Returns the name of the tool.
     * @return
     **************************************************************************/
    public String getName();

    /***************************************************************************
     * Returns the list of the tool's icons.
     * @return
     **************************************************************************/
    public List<Image> getImages();

    /***************************************************************************
     * Returns the tool's icon which is 24 x 24 pixels.
     * @return
     **************************************************************************/
    public Icon getIcon24();
}
