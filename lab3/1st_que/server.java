import java.util.*;
import java.net.*;
import java.io.*;

public class server{
      
      public static void main(String args[])throws Exception
      {
            try
            {

             ServerSocket server = new ServerSocket(8008);

                  Socket s = server.accept();
                  DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                  DataInputStream dIn = new DataInputStream(s.getInputStream());
                  String rec = dIn.readUTF();
                  int flag=0;
                  int sum = 0;
                  int c=0;
                  for(int i=0;i<rec.length();i++)
                  {
                        if(rec.charAt(i)=='a' || rec.charAt(i)=='e' || rec.charAt(i)=='i' || rec.charAt(i)=='o' || rec.charAt(i)=='u' ||rec.charAt(i)=='A' || rec.charAt(i)=='E' || rec.charAt(i)=='I' || rec.charAt(i)=='O' || rec.charAt(i)=='U' )
                        {
                              
                              c=c+1;
                             
                              
                        }
                       
                  }
                  
                  
                        String send=" ";
                        send=send+c;
                        dOut.writeUTF(send);
                  }
                   catch (Exception e) {
        e.printStackTrace();
            }
            
                 
            
      }
}

            
            

