import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Math; 

class sender{
public static void main(String args[]) throws Exception{
sender sws = new sender();
sws.run();
}

public void run() throws Exception{

Scanner sc=new Scanner(System.in);

System.out.println("Enter no of frames to be sent: ");

int f=sc.nextInt();

Socket myskt=new Socket("localhost",5000);
int new_n;

PrintStream myps=new PrintStream(myskt.getOutputStream());
int number;
for(int i=0;i<=f;)
{
  if(i==f)
  {
    myps.println("exit");
    break;
  }
  int a;
  String checksum = "";
number = sc.nextInt();
int n=number;

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
    
    if(r_num%2==0)
    {
      int  r=number+1 ;

      myps.println(String.valueOf(r)+ " "+checksum+ " "+ i);
    }
    else
    {
      
      myps.println(String.valueOf(number)+ " "+ checksum+ " "+ i);
    }


BufferedReader bf=new BufferedReader(new InputStreamReader(myskt.getInputStream()));

String ack=bf.readLine();
// System.out.println("ackno"+ack);
    if(ack.equals("Received"))
      {
        System.out.println("Acknowledgement RECEIVED");
        

	
        Thread.sleep(2000);
        i++;
      }
      else if (ack.equals("Send Again"))
      {
        System.out.println("Frame sent in Error ");
        System.out.println("Please Resend ");
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
        myps.println(String.valueOf(number)+ " "+check+ " "+ i);
        String ack1=bf.readLine();
        if(ack1.equals("Received"))
        {
          System.out.println("Acknowledgement RECEIVED");
          

    
          Thread.sleep(2000);
          i++;
        }
        

       
      }

      
    }
  }

  

}
