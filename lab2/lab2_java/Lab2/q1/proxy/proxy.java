import java.io.*;
import java.net.*;

public class proxy{
      public static ServerSocket socket = null;
      public static Socket s = null , ps =null;
      public static void main(String args[])throws Exception
      {
            proxy server = new proxy();
            server.run();
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
      public static void run()throws Exception
      {
            socket = new ServerSocket(3300);
            s = socket.accept();
            Runtime run = Runtime.getRuntime();
            Process p = null;
            InputStreamReader dIn = new InputStreamReader(s.getInputStream());

            BufferedReader br = new BufferedReader(dIn);

            String rec = "";

            System.out.println(rec);

            ps = new Socket("127.0.0.1",5000);
            PrintStream print = new PrintStream(ps.getOutputStream());

            rec = br.readLine();
                  if(rec.equals("01"))
                  {
                        System.out.println("Client Asks to Show Files: ");
                        print.println("01");
                  }
                  else if(rec.equals("02"))
                  {
                        rec = br.readLine();
                        System.out.println("Client asks to Download: "+rec);

                        String cmd0 = null;
                        String read = null;
                        String files[] = new String[10];
                        int k=0;
                        try
                        {
                              //cmd0 = "ls ~/Downloads/4th_sem/networks_lab/lab1/q6";
                              cmd0="ls";
                              p = run.exec(cmd0);
                              //p.getErrorStream();
                              p.waitFor();
                              BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                             // read = null;
                             System.out.println("Files at Proxy: ");
                              while(true)
                              {
                                    read = reader.readLine();

                                    if(read==null)
                                          break;
                                    files[k++]=read;
                                    System.out.println(read);
                              }

                        }
                        catch(Exception e)
                        {
                              System.out.println("Error running the Command : "+cmd0);
                        }
                        int flag=0;
                        for(int i=0;i<k;i++)
                        {
                              if(rec.equals(files[i]))
                              {
                                    flag=1;
                                    break;
                              }
                        }

                        if(flag==1)
                        {
                              System.out.println("File "+rec+" Found here!");
                              send(s,rec);
                        }
                        else
                        {
                              System.out.println("File Not Found Here.. Asking Server..");
                              print.println("02");
                              print.println(rec);
                              receive(ps,rec);
                              send(s,rec);
                        }


                  }


      }
}
