import java.util.*;
import java.net.*;
import java.io.*;

public class client{
      public static Socket s=null;
	public static void network_link(String st)throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            data_link(st);
      }
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
                  String x="";
                  // String st = "10101010101010101010101010101010101010101010101010101010101010101";
                  String st = scan.next();
                  System.out.println(st);
                   // for(int i=0;i<st.length()-4;i+=4)
                   // {
                   //      for(int j=i;j<i+4;j++)
                   //      {
                   //           x= x+""+st.charAt(i);
                              
                               
                   //      }
                        network_link(st);
                        // x="";
                        
                        // Thread.sleep(1000);
                  //}

                  
            }


      }
}
