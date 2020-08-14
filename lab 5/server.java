import java.util.*;
import java.net.*;
import java.io.*;

public void sender1(void)
{
            frame s;                                        // Buffer for an outbound frame 
            packet buffer;                            // Buffer for an outbound packet
            while (true) {
                        from_network_layer(&buffer);  // Go get something to send
                        s.info = buffer;                // Copy it into s for transmission
                        to_physical(&s);            // Send it on its way
            }     
}

public void receiver1(void)
{
            frame r;
            event_type event;                             // Filled in by wait, but not used here
            while (true) {
                        wait_for_event(&event);       // Only possibility is frame_arrival
                        from_physical_layer(&r);      // Go get the inbound frame
                        to_network_layer(&r.info);    // Pass the data to the network layer
            }

}




public class server{
      public static ServerSocket socket;
      public static Socket s;
      public static void data_link(String st)throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            System.out.println(st);

      }
      public static void to_physical()throws Exception
      {
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            String rec = dIn.readUTF();
            data_link(rec);

      }
      public static void main(String[] args)throws Exception
      {
            socket = new ServerSocket(5000);
            s= socket.accept();
            while(true)
            {
                  to_physical();
            }



      }
}