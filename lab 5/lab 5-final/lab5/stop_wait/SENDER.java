import java.util.*;
import java.net.*;
import java.io.*;
import java.time.*;

class SENDER{
public static void main(String args[]) throws Exception{
SENDER sws = new SENDER();

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
      String st="01010101010101010010101001";
      Socket myskt=new Socket("localhost",5000);
      System.out.println("Frame to be sent:"+ st);
      int f=st.length();
      // int window = 4;
      // long threshold = 1999;

      
      // int new_n;

      PrintStream myps=new PrintStream(myskt.getOutputStream());
      // int number;
                for(int i=0;i<=f;)
                  {
                    if(i==f)
                      {
                        myps.println("exit");
                        break;
                      }
                      int a;
              String checksum = "";
              String send = "";
              send = send+ ""+ st.charAt(i);
            
            int n = Integer.parseInt(send);
            while(n > 0)
                    {
                        a = n % 2;
                        checksum = checksum + "" + a;
                        n = n / 2;
                    }
                    System.out.println("Frame no "+i+" is sent data number "+st.charAt(i)+" ");

                    String s= "";
            s = send+ " "+checksum+ " "+ i;
            network_link(myskt,s);

            BufferedReader bf=new BufferedReader(new InputStreamReader(myskt.getInputStream()));

            String ack=bf.readLine();
                if(ack!=null)
                  {
                    System.out.println("Acknowledgement RECEIVED");
                    i++;

              
                    Thread.sleep(2000);
                  }

                  
                  }

      network_link(myskt,"0");
      System.out.println("ALL FRAMES RECEIVED");









      
    }

  }

