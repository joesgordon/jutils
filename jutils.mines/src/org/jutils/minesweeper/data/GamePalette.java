package org.jutils.minesweeper.data;

import java.awt.Color;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GamePalette
{
    /**  */
    public Color form;
    /**  */
    public Color tileLight;
    /**  */
    public Color tileDark;
    /**  */
    public Color boardLight;
    /**  */
    public Color boardDark;
    /**  */
    public Color mine;
    /**  */
    public Color border;

    /***************************************************************************
     * 
     **************************************************************************/
    public GamePalette()
    {
        this( PaletteTheme.BLUE.palette );
    }

    /***************************************************************************
     * @param form
     * @param tileLight
     * @param tileDark
     * @param boardLight
     * @param boardDark
     * @param mine
     * @param border
     **************************************************************************/
    public GamePalette( int form, int tileLight, int tileDark, int boardLight,
        int boardDark, int mine, int border )
    {
        this.form = new Color( form );

        this.tileLight = new Color( tileLight );
        this.tileDark = new Color( tileDark );

        this.boardLight = new Color( boardLight );
        this.boardDark = new Color( boardDark );

        this.mine = new Color( mine );
        this.border = new Color( border );
    }

    /***************************************************************************
     * @param p
     **************************************************************************/
    public GamePalette( GamePalette p )
    {
        this.form = p.form;

        this.tileLight = p.tileLight;
        this.tileDark = p.tileDark;

        this.boardLight = p.boardLight;
        this.boardDark = p.boardDark;

        this.mine = p.mine;
        this.border = p.border;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum PaletteTheme
    {
        /**  */
        BLUE( 0x0099CC, 0x8FBFFF, 0x7FAFFF, 0xCCCCCC, 0xBBBBBB,
            Color.red.getRGB(), Color.blue.getRGB() ),
        /**  */
        GREEN( 0x0099CC, 0x66CCFF, 0x0066CC, Color.lightGray.getRGB(),
            Color.gray.getRGB(), Color.red.getRGB(), Color.blue.getRGB() );

        /**  */
        private final GamePalette palette;

        /**
         * @param form
         * @param tileLight
         * @param tileDark
         * @param boardLight
         * @param boardDark
         * @param mine
         * @param border
         */
        private PaletteTheme( int form, int tileLight, int tileDark,
            int boardLight, int boardDark, int mine, int border )
        {
            this.palette = new GamePalette( form, tileLight, tileDark,
                boardLight, boardDark, mine, border );
        }
    }
}
