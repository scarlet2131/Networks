#include<bits/stdc++.h>
using namepscae std;
typedef enum {frame_arrival} event_type;
#include "protocol.h"
void sender1(void)
{
		frame s; 					            // Buffer for an outbound frame 
		packet buffer; 	                        // Buffer for an outbound packet
		while (true) {
				from_network_layer(&buffer);	// Go get something to send
				s.info = buffer; 		        // Copy it into s for transmission
				to_physical_layer(&s); 		    // Send it on its way
		}	
}
 
void receiver1(void)
{
		frame r;
		event_type event; 				    // Filled in by wait, but not used here
		while (true) {
				wait_for_event(&event); 	// Only possibility is frame_arrival
				from_physical_layer(&r); 	// Go get the inbound frame
				to_network_layer(&r.info); 	// Pass the data to the network layer
		}

}


// //sender side 
// begin
//    while (true)      //check repeatedly
//    do
//       Wait_For_Event();      //wait for availability of packet
//       if ( Event(Frame_Available)) then
//          Get_Data_From_Network_Layer();
//          Make_Frame();
//          Send_Frame_To_Physical_Layer();
//       end if
//    end while
// end

// //Receiver side

// begin
//    while (true)       //check repeatedly
//    do
//       Wait_For_Event();      //wait for arrival of frame
//       if ( Event(Frame_Arrival)) then
//          Receive_Frame_From_Physical_Layer();
//          Extract_Data();
//          Deliver_Data_To_Network_Layer();
//       end if
//    end while
// end
int main()
{

}