package jutils.core.net;

import java.io.IOException;
import java.net.SocketException;

import jutils.core.ValidationException;
import jutils.core.io.LogUtils;
import jutils.core.io.parsers.IpAddressParser;

public class NtpMain
{
    /**
     * @param args
     */
    public static void main( String [] args )
    {
        IpAddressParser ipParser = new IpAddressParser();
        String ntpStr = "";
        String lclStr = "0.0.0.0";

        if( args.length == 1 )
        {
            ntpStr = args[0];
        }
        else if( args.length == 2 )
        {
            ntpStr = args[0];
            lclStr = args[1];
        }
        else
        {
            LogUtils.printError(
                "Must supply NTP address and optionally a local address" );
            return;
        }

        try
        {
            IpAddress ntpServer = ipParser.parse( ntpStr );
            IpAddress local = ipParser.parse( lclStr );
            NtpMessage.sendTo( ntpServer, local );
        }
        catch( SocketException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( ValidationException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }
}
