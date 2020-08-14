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
      }
      public static void data_link(Socket myskt, String st)throws Exception
      {
            
            to_physical(myskt, st);
      }
      public static void to_physical(Socket myskt, String send)throws Exception
      {
            myps=new PrintStream(myskt.getOutputStream());
            myps.println(send);
            // dOut.writeUTF(send);

      }
public static void run() throws Exception{

      // Scanner sc=new Scanner(System.in);
      String st="abcdefghijklmnopqrstuvwxyz";
      Socket myskt=new Socket("localhost",5000);
      System.out.println("Frame to be sent:"+ st);
      int f=st.length();
      int window = 4;
      long threshold = 1999;

      
      String snt = "";

      PrintStream myps=new PrintStream(myskt.getOutputStream());
      int number;
      char frame[] = new char[window];
      for(int i=0;i<window;i++)
        frame[i] = st.charAt(i);
      Date d1 = new Date();
      long time[] = new long[window];
      for(int i=0;i<window;i++)
      {
        snt = (i+1)+ " " + st.charAt(i) ;
        System.out.println("Sending Frame - "+ (i+1) + " , Frame Data: " + st.charAt(i));
        network_link(myskt,snt);
        Thread.sleep(1500);

      }
      int l = window+1;
      while(true)
      {
          BufferedReader bf=new BufferedReader(new InputStreamReader(myskt.getInputStream()));
          String ack=bf.readLine();
          String[] arrOfStr = ack.split(" ");
          // System.out.println(arrOfStr[2]);
          

            if( arrOfStr[2].equals("0"))
            {
              int cur_frame = Integer.parseInt(arrOfStr[1]);
              int last_frame = l;
              System.out.println("Frame:  "+ cur_frame+ " Timed Out");
              System.out.println("Resending Frames after: "+ cur_frame +" ,to " + l);
              snt="";
              for(int i=cur_frame;i<l;i++)
              {
                // resend all the frames after the current frame
                System.out.println("Sending Frame - "+ (i) + " , Frame Data: " + st.charAt(i) );
                snt = (i+1) + " " +  st.charAt(l) ;
                network_link(myskt,snt);
                Thread.sleep(1500);
                
              }
            }
            else
            {
              // System.out.println("Frame:  "+ arrOfStr[1]+ " Received");
              System.out.println("Sending Frame - "+ (l) + " , Frame Data: " + st.charAt(l) );
              snt = l +" " + st.charAt(l) ;
              network_link(myskt,snt);
              Thread.sleep(1500);
              l++;
              if( l== f)
              {
                System.out.println("ALL FRAMES RECEIVED");
                break;
              }
            }
            
          
          
          


      }


    }

  }

