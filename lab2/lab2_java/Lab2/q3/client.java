import java.io.*;
import java.util.*;
import java.net.*;

public class client{
      public static Socket s = null;
      public static void main(String args[])throws IOException
      {
            int sock = Integer.parseInt(args[1]);
            s = new Socket(args[0],sock);
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter String: ");
            String send = scan.nextLine();
            dOut.writeUTF(send);
            while(true)
            {
                  String rec = dIn.readUTF();
                  System.out.println("From Server: "+rec);
                  if(rec.equals("Sorry"))
                  {
                        System.out.println("Sorry can't Compute..!");
                        break;
                  }
                  if(rec.length()==1)
                        break;

            }
            



      }
}
