import java.io.*;
import java.net.*;

class receiver{
public static void main(String args[])throws Exception{

receiver swr = new receiver();
swr.run();
}

public void run() throws Exception{

String temp="any message",str="exit";
ServerSocket myss=new ServerSocket(5000);
Socket ss_accept=myss.accept();

BufferedReader ss_bf=new BufferedReader(new InputStreamReader(ss_accept.getInputStream()));

PrintStream myps=new PrintStream(ss_accept.getOutputStream());

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
System.out.println("Frame "+arrOfStr[2]+" data received as "+arrOfStr[0]+" ");

System.out.println("Waiting...!");
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
        if(checksum.equals(arrOfStr[1]))
        {
          // System.out.println(arrOfStr[1] + " "+ checksum);
          myps.println("Received");
        	
        }


}

System.out.println("ALL FRAMES RECEIVED SUCCESSFULLY");

}

}
