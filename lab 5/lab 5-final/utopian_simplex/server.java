import java.util.*;
import java.net.*;
import java.io.*;

public class server{
      public static ServerSocket socket;
      public static Socket s;
	
      public static void data_link(String st)throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            System.out.println(st);

      }
      public static void to_physical()throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            String rec = dIn.readUTF();
            data_link(rec);

      }
      public static void main(String[] args)throws Exception
      {
            socket = new ServerSocket(5000);
            s= socket.accept();
            while(true)
            {
                  // Thread.sleep(1000);
                  to_physical();
            }



      }
}
