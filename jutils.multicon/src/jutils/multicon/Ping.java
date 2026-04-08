package jutils.multicon;

import java.io.IOException;
import java.net.InetAddress;

/*******************************************************************************
 *
 ******************************************************************************/
public class Ping {
  /***************************************************************************
   * Pings a specified host to test reachability
   * @param host to ping, can be an IP address or domain name (e.g "8.8.8.8"
   * or "google.com")
   * @param timeoutMillis maximum time to wait for a response in milliseconds
   * @return true, if host is reachable; false if otherwise.
   **************************************************************************/
  public static boolean executePing(String host, int timeoutMillis) {
    try {
      InetAddress address = InetAddress.getByName(host);
      // Sends ICMP ECHO REQUESTs if privilige can be obtained. Otherwise,
      // establish TCP connection on port 7 (Echo) of destination host.
      return address.isReachable(timeoutMillis);
    } catch (IOException ex) {
      System.err.println("Host " + host +
                         " is unreachable: " + ex.getStackTrace());
      return false;
    }
  }
}
