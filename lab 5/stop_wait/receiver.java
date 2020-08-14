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

System.out.println("Frame "+temp+" was received");
System.out.println("Waiting...!");
Thread.sleep(500);
myps.println("Received");

}

System.out.println("ALL FRAMES RECEIVED SUCCESSFULLY");

}

}