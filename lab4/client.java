
import java.io.*;
import java.util.*;
import java.net.*;

public class client{
      
      public static void main(String args[])throws Exception
      {
            String host="localhost";
            try{                  
                  Socket s=new Socket(host, 8008);
                  DataInputStream dIn = new DataInputStream(s.getInputStream());
                  DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                  Scanner scan = new Scanner(System.in);
                  int n=scan.nextInt();
                  
                  System.out.println("Enter the number of bits for hamming data: ");
                  for(int i=0;i<n;i++)
                  {
                    System.out.println("Enter bit no. "+n-i+" :");
                    a[n-i-1]=scan.nextInt();
                  }
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
