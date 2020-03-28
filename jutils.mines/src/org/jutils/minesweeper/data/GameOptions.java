package org.jutils.minesweeper.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GameOptions
{
    /**  */
    public boolean enableQuestion;
    /**  */
    public final GamePalette palette;

    /***************************************************************************
     * 
     **************************************************************************/
    public GameOptions()
    {
        this.enableQuestion = false;
        this.palette = new GamePalette();
    }
}
