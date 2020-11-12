package org.jutils.multicon;

import java.io.IOException;
import java.util.*;

import org.jutils.core.ValidationException;
import org.jutils.core.io.xs.XsUtils;
import org.jutils.core.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonOptions
{
    /**  */
    public TcpInputs tcpServerInputs;
    /**  */
    public TcpInputs tcpClientInputs;
    /**  */
    public UdpInputs udpInputs;
    /**  */
    public MulticastInputs multicastInputs;
    /**  */
    public Map<String, FavStorage<?>> favs;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonOptions()
    {
        this.tcpServerInputs = new TcpInputs();
        this.tcpClientInputs = new TcpInputs();
        this.udpInputs = new UdpInputs();
        this.multicastInputs = new MulticastInputs();
        this.favs = new HashMap<>();
    }

    /***************************************************************************
     * @param options the options to be copied.
     **************************************************************************/
    public MulticonOptions( MulticonOptions options )
    {
        this.tcpServerInputs = options.tcpServerInputs == null ? new TcpInputs()
            : new TcpInputs( options.tcpServerInputs );
        this.tcpClientInputs = options.tcpClientInputs == null ? new TcpInputs()
            : new TcpInputs( options.tcpClientInputs );
        this.udpInputs = options.udpInputs == null ? new UdpInputs()
            : new UdpInputs( options.udpInputs );
        this.multicastInputs = options.multicastInputs == null
            ? new MulticastInputs()
            : new MulticastInputs( options.multicastInputs );
        favs = options.favs == null ? new HashMap<>()
            : new HashMap<>( options.favs );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @return
     **************************************************************************/
    public <T> FavStorage<T> getFavs( String name )
    {
        @SuppressWarnings( "unchecked")
        FavStorage<T> f = ( FavStorage<T> )favs.get( name );

        return f;
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param favs
     * @return
     **************************************************************************/
    public <T> void setFavs( String name, FavStorage<T> favs )
    {
        this.favs.put( name, favs );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static class FavStorage<T>
    {
        /**  */
        private final Map<String, T> favs;

        /**
         * @param file
         */
        public FavStorage()
        {
            this.favs = new HashMap<>();
        }

        /**
         * @return
         */
        public List<String> getNames()
        {
            return new ArrayList<String>( favs.keySet() );
        }

        /**
         * @param name
         * @return
         */
        public T getFav( String name )
        {
            T fav = favs.get( name );

            if( fav != null )
            {
                fav = XsUtils.cloneObject( fav );
            }

            return fav;
        }

        /**
         * @param name
         * @param data
         * @throws IOException
         * @throws ValidationException
         */
        public void setFav( String name, T data )
        {
            favs.put( name, XsUtils.cloneObject( data ) );
        }
    }
}
