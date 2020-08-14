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

      
      int new_n;

      PrintStream myps=new PrintStream(myskt.getOutputStream());
      int number;
      char frame[] = new char[window];
      for(int i=0;i<window;i++)
        frame[i] = st.charAt(i);

      int l =1;
      int low = 0;
        int up = 4;
      for(int i=window;i<f-4;i+=4)
      {
        // send the current frame
        System.out.println("Sending Frame: " + l);
        
        Date d1 = new Date();
        // start sending the frames
        
        for(int j=0;j<window;j++)
        {
          
          while(true)
          {
              
              System.out.println("Current Frame: ");
              for(int k=0;k<window;k++)
                System.out.print(frame[k]+" ");
              System.out.println();

              System.out.println("\nSending: " + frame[0]);
              long start = d1.getTime();
              // send the data here
              String snt = l + " " +frame[0] + " " + (int)frame[0];
              network_link(myskt,snt);

              // recieve data here
               BufferedReader bf=new BufferedReader(new InputStreamReader(myskt.getInputStream()));
               String ack=bf.readLine();
               long end = Long.parseLong(ack.split(" ")[3]);

               // System.out.println(end-start);
               // System.out.println(start);
               if( end-start > threshold )
                {
                  // send again
                  System.out.println("Acknowledgement Timed out for Frame : " + l);
                  System.out.println("Resend Frame: " + l + " With Value: " + frame[j]);
                  continue;
                }
               
                
                String[] arrOfStr = ack.split(" ");
                System.out.println(arrOfStr[0]+ " " +arrOfStr[1]+ " "+arrOfStr[2]);
                low++;
                up++;
                int cf = 0;
                for(int k=low;k<up;k++)
                {
                  frame[cf] = st.charAt(k);
                  cf++;
                }

              break;

              
          }
          l++;

        }

        // generate the new frame
        
      }

      network_link(myskt,"0");
      System.out.println("ALL FRAMES RECEIVED");









      // for(int i=0;i<=f;)
      // {
      //   if(i==f)
      //   {
      //     myps.println("exit");
      //     break;
      //   }
      //   int a;
      //   String checksum = "";
      // number = sc.nextInt();
      // int n=number;

      // while(n > 0)
      //         {
      //             a = n % 2;
      //             checksum = checksum + "" + a;
      //             n = n / 2;
      //         }
      //         System.out.println("Frame no "+i+" is sent data number "+number+" ");
      //         Random ran = new Random(); 
        
              
      //         int r_num = ran.nextInt(1000); 
      //         // System.out.println("Random numer"+ r_num);
      //       // System.out.println("checksum"+ checksum);
          
      //     if(r_num%2==0)
      //     {
      //       int  r=number+1 ;

      //       myps.println(String.valueOf(r)+ " "+checksum+ " "+ i);
      //     }
      //     else
      //     {
            
      //       myps.println(String.valueOf(number)+ " "+ checksum+ " "+ i);
      //     }


      // BufferedReader bf=new BufferedReader(new InputStreamReader(myskt.getInputStream()));

      // String ack=bf.readLine();
      // // System.out.println("ackno"+ack);
      //     if(ack.equals("Received"))
      //       {
      //         System.out.println("Acknowledgement RECEIVED");
              

        
      //         Thread.sleep(2000);
      //         i++;
      //       }
      //       else if (ack.equals("Send Again"))
      //       {
      //         System.out.println("Frame sent in Error ");
      //         System.out.println("Please Resend ");
      //          //new_n=sc.nextInt();
      //         int num=number;
      //         int ar;
      //         String check= "";
      //         while(num > 0)
      //         {
      //             ar = num % 2;
      //             check = check + "" + ar;
      //             num = num / 2;
      //         }
      //         myps.println(String.valueOf(number)+ " "+check+ " "+ i);
      //         String ack1=bf.readLine();
      //         if(ack1.equals("Received"))
      //         {
      //           System.out.println("Acknowledgement RECEIVED");
                

          
      //           Thread.sleep(2000);
      //           i++;
      //         }
              

             
      //       }

            
      //     }
    }

  }

