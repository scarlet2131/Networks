import java.util.*;
import java.net.*;
import java.io.*;

public class client{
      public static Socket s=null;
      public static void data_link(String st)throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            to_physical(st);
      }
      public static void to_physical(String send)throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF(send);

      }
      
      public static void main(String[] args)throws Exception
      {
            s = new Socket("localhost",5000);

            Scanner scan = new Scanner(System.in);
            while(true)
            {
                  String st = scan.next();
                  data_link(st);
            }


      }
}