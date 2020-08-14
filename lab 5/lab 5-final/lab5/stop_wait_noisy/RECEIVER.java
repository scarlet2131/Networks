import java.util.*;
import java.net.*;
import java.io.*;

public class RECEIVER{
      public static void main(String args[]) throws Exception{
		RECEIVER sws = new RECEIVER();
		sws.from_physical();
		}
		// public static PrintStream myps;
	
      public static void data_link(Socket myss,String st)throws Exception
      {
            
            network_link(myss,st);

      }
      public static void network_link(Socket myss,String st)throws Exception
      {
		    
		    System.out.println(st);

      }      
      public static void from_physical()throws Exception
      {
            
           		String temp="any message",str="exit";
				ServerSocket myss=new ServerSocket(5000);
				Socket ss_accept=myss.accept();

				BufferedReader ss_bf=new BufferedReader(new InputStreamReader(ss_accept.getInputStream()));

				PrintStream myps=new PrintStream(ss_accept.getOutputStream());

				Date d1 = new Date();

				while(temp.compareTo(str)!=0)
				{

				Thread.sleep(1000);
				temp=ss_bf.readLine();

				  if(temp.compareTo(str)==0)
				  {
				  break;
				  }
				String[] arrOfStr = temp.split(" ");
				// System.out.println("array"+ arrOfStr[0]+" "+arrOfStr[1]);
				// System.out.println("Frame "+arrOfStr[2]+" data received as "+arrOfStr[0]+" ");
				System.out.println("Waiting...! "+"for frame number: "+ arrOfStr[2]);
				Thread.sleep(500);
				int num = Integer.parseInt(arrOfStr[0]);
				String checksum ="";
				int a;

				while(num > 0)
				        {
				            a = num % 2;
				            checksum = checksum + "" + a;
				            num = num / 2;
				        }
				        Random ran = new Random(); 
		        	int r_num = ran.nextInt(1000); 
		        	if( r_num%4==0 )
		        	{
				        
				          myps.println("SendAgain"+" "+d1.getTime()+3000);
				          System.out.println("Frame is lost ......PLease send again!!!");
				        	
				       
				    }
				    else
				    {
				    	if(checksum.equals(arrOfStr[1]))
				        {
				          // System.out.println(arrOfStr[1] + " "+ checksum);
					  
					 Random ran1 = new Random(); 
					int a_num = ran1.nextInt(1000); 
					if( a_num%2!=0 )
					{
				          myps.println("Received"+" "+d1.getTime());
						
				          System.out.println("Frame "+arrOfStr[2]+" data received as "+arrOfStr[0]+" ");
					}
					else
					{
						
						System.out.println("ack Received_with_error");	
						myps.println("Received_with_error"+" "+d1.getTime());
						myps.println("Received"+" "+d1.getTime());
						
				          System.out.println("Frame "+arrOfStr[2]+" data received as "+arrOfStr[0]+" ");
				        	
				        }
					}
				        else
				        {
				          // System.out.println(arrOfStr[1] + " "+ checksum);
				        	myps.println("SendAgain"+ " "+d1.getTime());
				        	System.out.println("Frame "+arrOfStr[2]+" data received as "+arrOfStr[0]+" "+ "which is wrong");

				        }
				    }


				}

				System.out.println("ALL FRAMES RECEIVED SUCCESSFULLY");




      }
     

     
}

