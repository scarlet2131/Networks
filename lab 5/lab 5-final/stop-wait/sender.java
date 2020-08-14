import java.io.*;
import java.net.*;
import java.util.*;

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


myps.println(String.valueOf(number)+ " "+checksum+ " "+ i);

BufferedReader bf=new BufferedReader(new InputStreamReader(myskt.getInputStream()));

String ack=bf.readLine();
    if(ack!=null)
      {
        System.out.println("Acknowledgement RECEIVED");
        i++;

	
        Thread.sleep(2000);
      }

      else
      {
        myps.println(i);
        }

      }

  }

}
