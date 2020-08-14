import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.io.*;
import java.net.*;
public class server{
   public static boolean validate_Date(String strDate)
   {
  
  if (strDate.trim().equals(""))
  {
      return true;
  }
  
  else
  {
      
      SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
      format.setLenient(false);
      try
      {
          Date javaDate = format.parse(strDate); 
          System.out.println(strDate+" is valid date format");
      }
    
      catch (ParseException e)
      {
          System.out.println(strDate+" is Invalid Date format");
          return false;
      }
      return true;
  }
   }
   public static void main(String args[])throws IOException{
  ServerSocket server = new ServerSocket(8008);
 
            
           Socket s = server.accept();
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            String rec = dIn.readUTF();
            

            String send="";
            send=Boolean.toString(validate_Date(rec));
            dOut.writeUTF(send);



   }
}