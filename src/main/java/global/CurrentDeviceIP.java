package global;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @auther kul.paudel
 * @created at 2023-05-19
 */
public class CurrentDeviceIP {

    public static String getIPAddress() {
        String ipAddress = "";
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof Inet4Address) {
                             ipAddress = address.getHostAddress();
                            System.out.println("Current Device IP Address: " + ipAddress);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.getMessage();
        }
        return ipAddress;
    }
}
