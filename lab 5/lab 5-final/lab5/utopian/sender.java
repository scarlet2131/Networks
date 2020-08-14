import java.util.*;
import java.net.*;
import java.io.*;
import java.time.*;

class sender{
public static void main(String args[]) throws Exception{
      sender sws = new sender();
      sws.run();
}
public static PrintStream myps;

public static void network_link(Socket myskt, String st)throws Exception
{
      
      data_link(myskt,st);
      
      // System.out.println(count);
}
public static void data_link(Socket myskt, String st)throws Exception
{
      
      to_physical(myskt, st);
}
public static void to_physical(Socket myskt, String send)throws Exception
{
      myps=new PrintStream(myskt.getOutputStream());
      System.out.println("Physical sending: " + send);
      myps.println(send);
      // dOut.writeUTF(send);

}

public static void run() throws Exception{

      // Scanner sc=new Scanner(System.in);
      String st="10111110101010101010101010101001";
      Socket myskt=new Socket("localhost",5000);
      //System.out.println("Frame to be sent:"+ st);
      int f=st.length();
      int p = 4;
      String s = "";
      int c = 0;
      int count =0;
      for(int i=0;i<f;i+=p)
      {
            c++;
            s = "";
      	System.out.print("Sending Frame number: " + c);
      	for(int j=i;j<i+p;j++)
      	{
      	
      	    s = s + st.charAt(j);

      	}

            System.out.println("with data value : " + s);
            s = s + " " + c;
            // System.out.println(s);
            network_link(myskt,s);
            Thread.sleep(1500);
            
              
               // x++;
                
      }

	

      
      //int new_n;

      PrintStream myps=new PrintStream(myskt.getOutputStream());
       network_link(myskt,"0");
      System.out.println("ALL FRAMES RECEIVED");
      // generate the new frame
        }

      
    }


  


