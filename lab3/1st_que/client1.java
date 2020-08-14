
import java.io.*;
import java.util.*;
import java.net.*;

public class client1{
      
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
                        System.out.println("From Server: "+rec);
                        
                        
                              

                  


            }
             catch (Exception e) {
        e.printStackTrace();
  }
            

      }
}
