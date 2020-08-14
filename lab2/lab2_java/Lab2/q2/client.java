import java.util.*;
import java.io.*;
import java.net.*;

public class client{
      public static Socket s = null;
      public static void main(String[] args)throws IOException
      {
            s = new Socket("127.0.0.1",5000);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            Scanner scan = new Scanner(System.in);

                  System.out.println("1.Query_1\n2.Query_2\n3.Query_3\n4.Query_4\n");
                  int ch = scan.nextInt();
                  if(ch==1)
                  {
                        dOut.writeUTF("01");
                        String name = dIn.readUTF();
                        String dept = dIn.readUTF();
                        int max = Integer.parseInt(dIn.readUTF());
                        System.out.println("NAME: "+name);
                        System.out.println("DEPT: "+dept);
                        System.out.println("SAL: "+max);

                  }
                  else if(ch==2)
                  {
                        dOut.writeUTF("02");
                        System.out.println("Enter Dept Name: ");
                        String dept = scan.next();
                        dOut.writeUTF(dept);
                        while(true)
                        {
                              String rc = dIn.readUTF();
                              if(rc.equals("break"))
                                    break;
                              System.out.println(rc);
                        }
                  }
                  else if(ch==3)
                  {
                        dOut.writeUTF("03");
                        while(true)
                        {
                              String rc = dIn.readUTF();
                              if(rc.equals("break"))
                                    break;
                              System.out.println(rc);
                        }
                  }
                  else if(ch==4)
                  {
                        dOut.writeUTF("04");
                        String rc = dIn.readUTF();
                        System.out.println(rc);
                  }



      }
}
