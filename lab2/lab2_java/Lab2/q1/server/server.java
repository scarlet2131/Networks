import java.io.*;
import java.net.*;

public class server{
      public static ServerSocket ss = null;
      public static Socket ps = null;
      public static void main(String[] args)throws Exception
      {
            server s = new server();
            s.run();
      }

      public static void send(Socket s,String name)throws Exception
      {
            Socket ssock=s;

            DataInputStream dis=new DataInputStream(ssock.getInputStream());
            DataOutputStream dout=new DataOutputStream(ssock.getOutputStream());
            String fname=name;
//System.out.println("Reading File "+filename);
            File f=new File(fname);
            FileInputStream fin=new FileInputStream(f);
            int ch;
            do
            {
                  ch=fin.read();
                  dout.writeUTF(Integer.toString(ch));
            }while(ch!=-1);
            fin.close();
            System.out.println("File Sent");
      }

      public static void run()throws Exception
      {
            ss = new ServerSocket(5000);
            ps = ss.accept();
            InputStreamReader ir = new InputStreamReader(ps.getInputStream());
            BufferedReader br = new BufferedReader(ir);

            Runtime run = Runtime.getRuntime();
            Process p = null;
            String action = br.readLine();
            if(action!=null)
            {
                  if(action.equals("01"))
                  {
                        System.out.println("Files Present Here: ");
                        String cmd0 = null;
                        String read = null;

                        try
                        {
                              //cmd0 = "ls ~home/scarlet2131/mon/5th_sem/networks/lab2/lab2_java/Lab2";
                              cmd0="ls";
                              p = run.exec(cmd0);
                              //p.getErrorStream();
                              p.waitFor();
                              BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                             // read = null;

                              while(true)
                              {
                                    read = reader.readLine();
                                    if(read==null)
                                          break;
                                    System.out.println(read);
                              }

                        }
                        catch(Exception e)
                        {
                              System.out.println("Error running the Command : "+cmd0);
                        }

                  }
                  else if(action.equals("02"))
                  {
                        String name = br.readLine();
                        send(ps,name);

                  }
            }



      }
}
