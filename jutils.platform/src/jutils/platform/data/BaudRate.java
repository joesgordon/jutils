package jutils.platform.data;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum BaudRate implements INamedItem
{
    /** 110 bits per second */
    B110( 110 ),
    /** 300 bits per second */
    B300( 300 ),
    /** 600 bits per second */
    B600( 600 ),
    /** 1200 bits per second */
    B1200( 1200 ),
    /** 2400 bits per second */
    B2400( 2400 ),
    /** 4800 bits per second */
    B4800( 4800 ),
    /** 9600 bits per second */
    B9600( 9600 ),
    /** 14400 bits per second */
    B1440( 14400 ),
    /** 19200 bits per second */
    B19200( 19200 ),
    /** 56000 bits per second */
    B56000( 56000 ),
    /** 57600 bits per second */
    B57600( 57600 ),
    /** 115200 bits per second */
    B115200( 115200 ),
    /** 230400 bits per second */
    B234000( 230400 ),
    /** 576000 bits per second */
    B576000( 576000 ),
    /** 921600 bits per second */
    B921600( 921600 ),
    /** 1000000 bits per second */
    B01M( 1000000 ),
    /** 2000000 bits per second */
    B02M( 2000000 ),
    /** 4000000 bits per second */
    B04M( 4000000 ),
    /** 10000000 bits per second */
    B10M( 10000000 );

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     **************************************************************************/
    private BaudRate( int value )
    {
        this.value = value;
        this.name = "" + value;
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
     * @return
     **************************************************************************/
    public static Integer [] getBauds()
    {
        BaudRate [] vals = values();
        Integer [] bauds = new Integer[vals.length];

        for( int i = 0; i < vals.length; i++ )
        {
            bauds[i] = vals[i].value;
        }

        return bauds;
    }
}
