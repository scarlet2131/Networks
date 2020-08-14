import java.util.*;
import java.net.*;
import java.io.*;

public class server{
      public static ServerSocket socket = null;
      public static Socket s = null;
      public static void main(String args[])throws IOException
      {
            int sock = Integer.parseInt(args[1]);
            socket = new ServerSocket(sock);
            s = socket.accept();
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            String rec = dIn.readUTF();
            int flag=0;
            int sum = 0;
            for(int i=0;i<rec.length();i++)
            {
                  if(rec.charAt(i)>'9' || rec.charAt(i)<'0')
                  {
                        flag=1;
                        break;
                  }
                  else
                  {
                        int x = rec.charAt(i)-'0';
                        sum = sum + x;
                  }
            }
            if(flag==1)
            {
                  dOut.writeUTF("Sorry");
            }
            else
            {
                  String send = "";
                  while(true)
                  {
                        send = "";
                        if(sum>9)
                        {
                              send = send + sum;
                              dOut.writeUTF(send);
                              int k = sum;
                              sum = 0;
                              while(k>0)
                              {
                                   sum = sum + k%10;
                                   k=k/10;
                              }
                        }
                        else
                        {
                              send = send + sum;
                              dOut.writeUTF(send);
                              break;
                        }

                  }
            }
      }
}
