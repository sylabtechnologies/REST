/* 
send UDP
*/

package udptest;

import java.net.*;
import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UDPTest
{

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException
    {
        Scanner in = new Scanner(System.in);

//      String host = "10.0.0.194";
        String host = "localhost";
        InetAddress addr = InetAddress.getByName(host);
        int port = 10000;
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        boolean go = true;
        while (go)
        {          
            String message = in.nextLine();
            byte[] temp = message.getBytes();
            byte[] digest = md.digest(temp);

            message = "PODL1111" + message;
            temp = message.getBytes();
            byte[] data = merge(temp, digest);
            
            DatagramPacket packet = new DatagramPacket( data, data.length, addr, port );
            DatagramSocket ds = new DatagramSocket();
            ds.send( packet );

            if (message.equals("exit"))
                go = false;
            else
            {
                packet = new DatagramPacket( new byte [1024], 1024 );
                ds.receive( packet );
                System.out.println((int) packet.getData()[0]);
                
                ds.close();
            }
        }


    }

    /// append as reversed, print hex
    private static byte[] merge(byte[] arr1, byte[] md5)
    {
        byte[] ans = new byte[arr1.length + md5.length];
        int count = 0;
        
        for (byte b : arr1)
            ans[count++] = b;
        
        for (int i = 0; i < md5.length; i += 4)
        {
            for (int j = 0; j < 4; j++)
                ans[count + i + 3 - j] = md5[i + j];
        }
        
        String hex = bytesToHex(md5);
        for (int i = 0; i < 32; i += 8)
        {
            String temp = hex.substring(i, i + 8);
            System.out.println(temp);
            // System.out.println(Long.parseLong(temp, 16));
        }
        
        return ans;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        
        return new String(hexChars);
    }
    
}
