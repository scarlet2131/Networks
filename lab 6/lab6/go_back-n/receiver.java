import java.util.*;
import java.net.*;
import java.io.*;

public class receiver{
      public static void main(String args[]) throws Exception{
		receiver sws = new receiver();
		sws.run();
		}
	
      public static void data_link(String st)throws Exception
      {
            
            System.out.println(st);

      }
      public static void to_physical()throws Exception
      {
            
            // String rec = dIn.readUTF();
      	String temp="any message",str="exit";
			
			// Socket ss_accept=myss.accept();
			// BufferedReader ss_bf=new BufferedReader(new InputStreamReader(ss_accept.getInputStream()));
            // data_link(rec);

      }
     

     public static void run() throws Exception{

			String temp="any message",str="exit";
			ServerSocket myss=new ServerSocket(5000);
			Socket ss_accept=myss.accept();

			Date d1 = new Date();
			while(true)
			{
				BufferedReader ss_bf=new BufferedReader(new InputStreamReader(ss_accept.getInputStream()));

				PrintStream myps=new PrintStream(ss_accept.getOutputStream());


				temp = ss_bf.readLine();
				if(temp == "0")
					break;

				// System.out.println(temp);
				String[] arrOfStr = temp.split(" ");
				// System.out.println("array"+ arrOfStr[0]+" "+arrOfStr[1]);
				System.out.println("Received Frame No->"+ arrOfStr[0] + " , Frame Data-> " + arrOfStr[1]);
				
				Random ran = new Random(); 
	        	int r_num = ran.nextInt(1000); 
	        	if( r_num%4==0 )
	        	{
	        		// pause for 3000 ms
	        		//Thread.sleep(3000);
	        		// System.out.println("Frame Timed Out");
	        		myps.println("Received" + " " + arrOfStr[0] + " " + "0");

	        	}
	        	else
	        	{
	        		myps.println("Received" + " " + arrOfStr[0] +" " + "1");
	        	}
				
			}
			

			

			// System.out.println("ALL FRAMES RECEIVED SUCCESSFULLY");

			}
}
