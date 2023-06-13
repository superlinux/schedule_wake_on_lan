package net.superlinux.shedulewakeonlan;

import android.content.Context;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Network {
    public static String getLocalIpAddress() {
        //List<String> address_list;
        String IPaddress="NONE";
        try {

            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
// IPv4 : 202.54.168.48
                    if (!inetAddress.isLoopbackAddress()) {
                        IPaddress=inetAddress.getHostAddress().toString();
                        if (IPaddress.matches("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$"))
                            return  IPaddress;
                    }

                }
            }

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    static String getMacAddress (){
        String IPaddress="NONE";
        String mac_address="NONE";
        try {

            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        IPaddress=inetAddress.getHostAddress().toString();
                        if (IPaddress.matches("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$"))
                            mac_address=intf.getHardwareAddress().toString();
                            return  mac_address; //intf.getHardwareAddress().toString();
                    }

                }
            }

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
   static String getBroadcast()  {
        try {
            System.setProperty("java.net.preferIPv4Stack", "true");
            for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
                NetworkInterface ni = niEnum.nextElement();
                if (!ni.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                        if (interfaceAddress.getBroadcast()!=null)
                            return  interfaceAddress.getBroadcast().toString().substring(1);
//192.168.1.5 && network subnet mask 255.255.255.0 => network IP  192.168.1.0 , network Broadcast IP  192.168.1.255 ,

                    }
                }
            }
        } catch (SocketException e){
            e.printStackTrace();
        }
        return "";
    }
//b4:2e:99:6f:ec:bd
   static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

   static boolean wakeOnLan(String macStr){
        int PORT=9;

        try {
            byte[] macBytes = getMacBytes(macStr);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            InetAddress address = InetAddress.getByName(getBroadcast());
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

           return true;
        }
        catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }
}
