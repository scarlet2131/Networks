import java.util.*;
import java.net.*;
import java.io.*;

public class receiver{
      public static void main(String args[]) throws Exception{
		receiver sws = new receiver();
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
            
            
            String temp = "any message", str = "exit";
			ServerSocket myss = new ServerSocket(5000);
			Socket ss_accept = myss.accept();

			
			while(true)
			{
				BufferedReader ss_bf=new BufferedReader(new InputStreamReader(ss_accept.getInputStream()));

				PrintStream myps=new PrintStream(ss_accept.getOutputStream());

				temp = ss_bf.readLine();
				
				if(temp.equals("0"))
				{
					break;
				}

				 // System.out.println(temp);
				// System.out.println(temp);
				String[] arrOfStr = temp.split(" ");
				
				// System.out.println("array"+ arrOfStr[0]+" "+arrOfStr[1]);
				System.out.println("Received Frame No->"+ arrOfStr[1] + " , Frame Data-> " + arrOfStr[0] + " ");
				data_link(ss_accept,arrOfStr[0]);
				
			}
			


      }
     

     
}

