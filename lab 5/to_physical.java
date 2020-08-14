 import java.util.*;
import java.net.*;
import java.io.*;

 public static void to_physical()throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            String rec = dIn.readUTF();
            data_link(rec);

      }