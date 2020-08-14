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

        Socket myskt=new Socket("localhost",5000);
// int new_n;

      PrintStream myps=new PrintStream(myskt.getOutputStream());
      // int number;
      String st ="101111111110110101010101010100101";
      int f=st.length();

       long threshold = 1999;

      for(int i=0;i<=f;)
      {
         Date d1 = new Date();
            if(i==f)
            {
              myps.println("exit");
              break;
            }
            int a;
            String checksum = "";
          // number = sc.nextInt();
          String send= "";
          send= send+ ""+ st.charAt(i);
          int number =Integer.parseInt(send);
          int n=Integer.parseInt(send);

          while(n > 0)
                  {
                      a = n % 2;
                      checksum = checksum + "" + a;
                      n = n / 2;
                  }
                  System.out.println("Frame no "+i+" is sent data number "+number+" ");
                  Random ran = new Random(); 
            
                  
                  int r_num = ran.nextInt(1000); 
                  // System.out.println("Random numer"+ r_num);
                // System.out.println("checksum"+ checksum);
              long start = d1.getTime();
              if(r_num%2==0)
              {
                int  r=number+1 ;

                String s=(String.valueOf(r)+ " "+checksum+ " "+ i);
                network_link(myskt,s);
              }
              else
              {
                
                String s=(String.valueOf(number)+ " "+checksum+ " "+ i);
                network_link(myskt,s);
              }


          BufferedReader bf=new BufferedReader(new InputStreamReader(myskt.getInputStream()));

          String ack=bf.readLine();
          String[] arr = ack.split(" ");
          long end = Long.parseLong(ack.split(" ")[1]);
          // System.out.println("ackno"+ack);
              if(arr[0].equals("Received"))
                {
                  System.out.println("Acknowledgement RECEIVED");
                  

            
            Thread.sleep(2000);
            i++;
          }
          else if (arr[0].equals("SendAgain") && end-start > threshold)
          {
            System.out.println("Packet lost: ");

            System.out.println("Please Resend ");
            System.out.println();
            System.out.println();
             //new_n=sc.nextInt();
            int num=number;
            int ar;
            String check= "";
            while(num > 0)
            {
                ar = num % 2;
                check = check + "" + ar;
                num = num / 2;
            }
            // myps.println(String.valueOf(number)+ " "+check+ " "+ i);
            String s=(String.valueOf(number)+ " "+check+ " "+ i);
          network_link(myskt,s);
            String ack1=bf.readLine();
            String[] arr1 = ack1.split(" ");

            if(arr1[0].equals("Received"))
            {
              System.out.println("Acknowledgement RECEIVED");
              

        
              Thread.sleep(2000);
              i++;
            }
            

           
          }
          else if (arr[0].equals("SendAgain") && end-start < threshold)
          {
            System.out.println("Damaged Frame Received: ");

            System.out.println("Please Resend ");
            System.out.println();
            System.out.println();
             //new_n=sc.nextInt();
            int num=number;
            int ar;
            String check= "";
            while(num > 0)
            {
                ar = num % 2;
                check = check + "" + ar;
                num = num / 2;
            }
            // myps.println(String.valueOf(number)+ " "+check+ " "+ i);
            String s=(String.valueOf(number)+ " "+check+ " "+ i);
          network_link(myskt,s);
            String ack1=bf.readLine();
            String[] arr1 = ack1.split(" ");

            if(arr1[0].equals("Received"))
            {
              System.out.println("Acknowledgement RECEIVED");
              

        
              Thread.sleep(2000);
              i++;
            }
            

           
          }
		else
		{
			System.out.println("SEND ACKNOWLEDGEMENT AGAIN");
			String ack2=bf.readLine();
            		String[] arr2 = ack2.split(" ");
			if(arr2[0].equals("Received"))
			{
				System.out.println("Acknowledgement RECEIVED");
				i++;
			}
			//i++;
		}

          
        }      
    }

  }

