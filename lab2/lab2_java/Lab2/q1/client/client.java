import java.io.*;
import java.net.*;
import java.util.*;

public class client{
      public static Socket s = null;
      public static void main(String[] args)throws Exception
      {
            client c = new client();
            c.run();
      }

      public static void receive(Socket s,String name)throws Exception
      {
            Socket ssock=s;

            DataInputStream dis=new DataInputStream(ssock.getInputStream());
            DataOutputStream dout=new DataOutputStream(ssock.getOutputStream());

            String fname=name;
            //System.out.println("Receiving File "+filename);
            File f=new File(fname);
            FileOutputStream fout=new FileOutputStream(f);
            int ch;
            while((ch=Integer.parseInt(dis.readUTF()))!=-1)
            {
                  fout.write(ch);
            }
            System.out.println("Received File...");
            fout.close();
      }
      public void run()throws Exception
      {
            s = new Socket("localhost",3300);
            PrintStream print = new PrintStream(s.getOutputStream());

                  System.out.println("1. Show Files at Server\n2. Download File\n");
                  Scanner scan = new Scanner(System.in);
                  int ch = scan.nextInt();
                  if(ch==1)
                  {
                        print.println("01");
                  }
                  else if(ch==2)
                  {
                        System.out.print("Enter fileName: ");
                        String name = scan.next();
                        print.println("02");
                        print.println(name);
                        receive(s,name);

                  }


            //ps.println("Hello Server!");
      }
}
