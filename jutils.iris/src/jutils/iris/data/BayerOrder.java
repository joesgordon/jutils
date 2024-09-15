package jutils.iris.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum BayerOrder implements INamedValue
{
    /**  */
    GRBG( 0, "GRBG" ),
    /**  */
    GBRG( 1, "GBRG" ),
    /**  */
    RGGB( 2, "RGGB" ),
    /**  */
    BGGR( 3, "BGGR" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private BayerOrder( int value, String name )
    {
        this.value = value;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * @param isEvenRow
     * @param isEvenCol
     * @return
     **************************************************************************/
    public boolean isGreen1( boolean isEvenRow, boolean isEvenCol )
    {
        return ( value < 2 && isEvenRow && isEvenCol ) ||
            ( value > 1 && isEvenRow && !isEvenCol );
    }

    /***************************************************************************
     * @param isEvenRow
     * @param isEvenCol
     * @return
     **************************************************************************/
    public boolean isRed( boolean isEvenRow, boolean isEvenCol )
    {
        switch( value )
        {
            case 0:
                return isEvenRow && !isEvenCol;

            case 1:
                return !isEvenRow && isEvenCol;

            case 2:
                return isEvenRow && isEvenCol;

            case 3:
                return !isEvenRow && !isEvenCol;
        }
        return false;
    }

    /***************************************************************************
     * @param isEvenRow
     * @param isEvenCol
     * @return
     **************************************************************************/
    public boolean isBlue( boolean isEvenRow, boolean isEvenCol )
    {
        switch( value )
        {
            case 0:
                return !isEvenRow && isEvenCol;

            case 1:
                return isEvenRow && !isEvenCol;

            case 2:
                return !isEvenRow && !isEvenCol;

            case 3:
                return isEvenRow && isEvenCol;
        }
        return false;
    }

    /***************************************************************************
     * @param isEvenRow
     * @param isEvenCol
     * @return
     **************************************************************************/
    public boolean isGreen2( boolean isEvenRow, boolean isEvenCol )
    {
        return ( value < 2 && !isEvenRow && !isEvenCol ) ||
            ( value > 1 && !isEvenRow && isEvenCol );
    }

    /***************************************************************************
     * @param bitDepth
     * @param isPacked
     * @return
     **************************************************************************/
    public String getExtension( int bitDepth, boolean isPacked )
    {
        String str = name.substring( 0, 2 ) + bitDepth +
            ( isPacked ? "p" : "" );
        return str.toLowerCase();
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public String getChannelName( int index )
    {
        char c = name.charAt( index );
        String name = null;

        switch( c )
        {
            case 'R':
                name = "Red";
                break;

            case 'G':
                name = "Green" + ( index < 2 ? "1" : "2" );
                break;

            case 'B':
                name = "Blue";
                break;

        }

        return name;
    }
}
