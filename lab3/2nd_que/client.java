
import java.io.*;
import java.util.*;
import java.net.*;

public class client{
      public static Socket s = null;
      public static void main(String args[])throws Exception
      {
            String host="localhost";
            try{
                    Socket s=new Socket(host, 8008);
           
                 
                  DataInputStream dIn = new DataInputStream(s.getInputStream());
                  DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                  Scanner scan = new Scanner(System.in);
                  System.out.print("Enter String: ");
                  String send = scan.nextLine();
                  dOut.writeUTF(send);
                 
                        String rec = dIn.readUTF();
                         System.out.println("MM/DD/YYYY");
                        System.out.println("From Server: "+rec);
                        if(rec.equals("true"))
                        {
                              System.out.println("Valid date");
                              
                            }
                             
                        else
                        {
                            System.out.println("Invalid date");
                        }

                  


            }
             catch (Exception e) {
        e.printStackTrace();
  }
            

      }
}
