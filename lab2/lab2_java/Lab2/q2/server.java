import java.util.*;
import java.io.*;
import java.net.*;

public class server{
      public static ServerSocket socket = null;
      public static Socket s = null;

      public static void main(String[] args)throws IOException
      {
            socket = new ServerSocket(5000);
            s = socket.accept();

            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            DataInputStream dIn = new DataInputStream(s.getInputStream());

            File fr = new File("file1.txt");
            File fr2 = new File("file2.txt");
            Scanner scan = new Scanner(fr);
            int c1=0;
            while(scan.hasNextLine())
            {
                  c1++;
                  scan.nextLine();
            }
            //System.out.println("lines in FILE1: "+c1);
            scan = new Scanner(fr2);
            int c2 = 0;
            while(scan.hasNextLine())
            {
                  c2++;
                  scan.nextLine();
            }
            //System.out.println("Lines in FILE: "+c2);
            String dept_fr[] = new String[c1];
            int count[] = new int[c1];
            int k1=0,k2=0;
            scan = new Scanner(fr);
            while(scan.hasNextLine())
            {
                  String sc[] = scan.nextLine().split(" ");
                  dept_fr[k1++] = sc[0];
                  count[k2++] = Integer.parseInt(sc[1]);
            }
            scan = new Scanner(fr2);
            String name[] = new String[c2];
            int sal[] = new int[c2];
            String dept_fr2[] = new String[c2];
            int k3=0,k4=0,k5=0;
            while(scan.hasNextLine())
            {
                  String sc[] = scan.nextLine().split(" ");
                  name[k3++] = sc[0];
                  sal[k4++] = Integer.parseInt(sc[1]);
                  dept_fr2[k5++] = sc[2];

            }
            String rec = dIn.readUTF();
            int max=0;
            if(rec.equals("01"))
            {
                  max = 0;
                  int ind=0;
                  for(int i=0;i<k4;i++)
                  {
                        if(sal[i]>max)
                        {
                              max = sal[i];
                              ind = i;
                        }
                  }
                  String m="";
                  m = m+max;
                  dOut.writeUTF(name[ind]);
                  dOut.writeUTF(dept_fr2[ind]);
                  dOut.writeUTF(m);

            }
            else if(rec.equals("02"))
            {
                  String d = dIn.readUTF();
                  int total = 0;
                  for(int i=0;i<k5;i++)
                  {
                        if(dept_fr2[i].equals(d))
                        {
                              total++;
                        }
                  }
                  int c = 0;
                  for(int i=0;i<k5;i++)
                  {
                        if(dept_fr2[i].equals(d))
                        {
                              c++;
                              dOut.writeUTF(name[i]+"  "+sal[i]+"  "+d);
                              if(c==total)
                              {
                                    dOut.writeUTF("break");
                                    break;

                              }
                        }
                  }
            }
            else if(rec.equals("03"))
            {
                  int co = 0;
                  String temp="";
                  for(int i=0;i<k2;i++)
                  {
                        if(count[i]>co)
                        {
                              co = count[i];
                              temp = name[i];
                        }
                  }
                  int total = 0;
                  for(int i=0;i<k3;i++)
                  {
                        if(name[i].equals(temp))
                        {
                              total++;
                        }
                  }
                  int c=0;
                  for(int i=0;i<k3;i++)
                  {
                        if(name[i].equals(temp))
                        {
                              c++;

                              dOut.writeUTF(name[i]+"  "+sal[i]+"  "+dept_fr2[i]+"  "+co);
                              if(c==total)
                              {
                                    dOut.writeUTF("break");
                                    break;
                              }
                        }
                  }
            }
            else if(rec.equals("04"))
            {
                  int sec_max = 0;
                  int ind=0;
                  for(int i=0;i<k4;i++)
                  {
                        if(sal[i]>max)
                        {
                              max = sal[i];

                        }
                  }
                  for(int i=0;i<k4;i++)
                  {

                             if(sal[i]>sec_max  && sal[i]!=max)
                              {
                                    sec_max = sal[i];
                                    ind = i;
                              }

                  }
                  dOut.writeUTF(name[ind]+"  "+sal[ind]+"  "+dept_fr2[ind]);
            }
      }
}
