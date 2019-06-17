package org.jutils.ui.app;

/*******************************************************************************
 * Defines the methods for defining the look-and-feel to be loaded and
 * creating/displaying the user interface.
 ******************************************************************************/
public interface IApplication
{
    /***************************************************************************
     * Returns the name of the look-and-feel to be used.
     * @return the name of the look-and-feel to be used.
     **************************************************************************/
    public String getLookAndFeelName();

    /***************************************************************************
     * Creates the UI and this displays it.
     **************************************************************************/
    public void createAndShowUi();
}
