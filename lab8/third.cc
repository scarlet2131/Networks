/* -*- Mode:C++; c-file-style:"gnu"; indent-tabs-mode:nil; -*- */
/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
#include<bits/stdc++.h>
#include "ns3/core-module.h"
#include "ns3/point-to-point-module.h"
#include "ns3/network-module.h"
#include "ns3/applications-module.h"
#include "ns3/mobility-module.h"
#include "ns3/csma-module.h"
#include "ns3/internet-module.h"
#include "ns3/yans-wifi-helper.h"
#include "ns3/ssid.h"
#include "ns3/flow-monitor-module.h"
#include "ns3/ipv4-global-routing-helper.h"
#include<iomanip>


// Default Network Topology
//
//   Wifi 10.1.3.0
//                 AP
//  *    *    *    *
//  |    |    |    |    10.1.1.0
// n5   n6   n7   n0 -------------- n1   n2   n3   n4
//                   point-to-point  |    |    |    |
//                                   ================
//                                     LAN 10.1.2.0

using namespace ns3;

NS_LOG_COMPONENT_DEFINE ("ThirdScriptExample");

int 
main (int argc, char *argv[])
{
  bool verbose = true;
  uint32_t nCsma = 3;
  uint32_t nWifi = 3;
  bool tracing = false;

  CommandLine cmd;
  cmd.AddValue ("nCsma", "Number of \"extra\" CSMA nodes/devices", nCsma);
  cmd.AddValue ("nWifi", "Number of wifi STA devices", nWifi);
  cmd.AddValue ("verbose", "Tell echo applications to log if true", verbose);
  cmd.AddValue ("tracing", "Enable pcap tracing", tracing);

  cmd.Parse (argc,argv);

  // The underlying restriction of 18 is due to the grid position
  // allocator's configuration; the grid layout will exceed the
  // bounding box if more than 18 nodes are provided.
  if (nWifi > 18)
    {
      std::cout << "nWifi should be 18 or less; otherwise grid layout exceeds the bounding box" << std::endl;
      return 1;
    }

  if (verbose)
    {
      LogComponentEnable ("UdpEchoClientApplication", LOG_LEVEL_INFO);
      LogComponentEnable ("UdpEchoServerApplication", LOG_LEVEL_INFO);
    }

  NodeContainer p2pNodes;
  p2pNodes.Create (2);

  PointToPointHelper pointToPoint;
  pointToPoint.SetDeviceAttribute ("DataRate", StringValue ("5Mbps"));
  pointToPoint.SetChannelAttribute ("Delay", StringValue ("2ms"));

  NetDeviceContainer p2pDevices;
  p2pDevices = pointToPoint.Install (p2pNodes);

  NodeContainer csmaNodes;
  csmaNodes.Add (p2pNodes.Get (1));
  csmaNodes.Create (nCsma);



  CsmaHelper csma;
  csma.SetChannelAttribute ("DataRate", StringValue ("100Mbps"));
  csma.SetChannelAttribute ("Delay", StringValue ("1ms"));

  NetDeviceContainer csmaDevices;
  csmaDevices = csma.Install (csmaNodes);                
                




  

 // CsmaHelper csma;
 // csma.SetChannelAttribute ("DataRate", StringValue ("100Mbps"));
  //csma.SetChannelAttribute ("Delay", TimeValue (NanoSeconds (6560)));

  //NetDeviceContainer csmaDevices;
  //csmaDevices = csma.Install (csmaNodes);

  NodeContainer wifiStaNodes;
  wifiStaNodes.Create (nWifi);
  NodeContainer wifiApNode = p2pNodes.Get (0);

  YansWifiChannelHelper channel = YansWifiChannelHelper::Default ();
  YansWifiPhyHelper phy = YansWifiPhyHelper::Default ();
  phy.SetChannel (channel.Create ());

  WifiHelper wifi;
  wifi.SetRemoteStationManager ("ns3::AarfWifiManager");

  WifiMacHelper mac;
  Ssid ssid = Ssid ("ns-3-ssid");
  mac.SetType ("ns3::StaWifiMac",
               "Ssid", SsidValue (ssid),
               "ActiveProbing", BooleanValue (false));

  NetDeviceContainer staDevices;
  staDevices = wifi.Install (phy, mac, wifiStaNodes);

  mac.SetType ("ns3::ApWifiMac",
               "Ssid", SsidValue (ssid));

  NetDeviceContainer apDevices;
  apDevices = wifi.Install (phy, mac, wifiApNode);

  MobilityHelper mobility;

  mobility.SetPositionAllocator ("ns3::GridPositionAllocator",
                                 "MinX", DoubleValue (0.0),
                                 "MinY", DoubleValue (0.0),
                                 "DeltaX", DoubleValue (5.0),
                                 "DeltaY", DoubleValue (10.0),
                                 "GridWidth", UintegerValue (3),
                                 "LayoutType", StringValue ("RowFirst"));

  mobility.SetMobilityModel ("ns3::RandomWalk2dMobilityModel",
                             "Bounds", RectangleValue (Rectangle (-50, 50, -50, 50)));
  mobility.Install (wifiStaNodes);

  mobility.SetMobilityModel ("ns3::ConstantPositionMobilityModel");
  mobility.Install (wifiApNode);

  InternetStackHelper stack;
  stack.Install (csmaNodes);
  stack.Install (wifiApNode);
  stack.Install (wifiStaNodes);

  Ipv4AddressHelper address;

  address.SetBase ("10.1.1.0", "255.255.255.0");
  Ipv4InterfaceContainer p2pInterfaces;
  p2pInterfaces = address.Assign (p2pDevices);

  address.SetBase ("10.1.2.0", "255.255.255.0");
  Ipv4InterfaceContainer csmaInterfaces;
  csmaInterfaces = address.Assign (csmaDevices);

  address.SetBase ("10.1.3.0", "255.255.255.0");
  address.Assign (staDevices);
  address.Assign (apDevices);

  UdpEchoServerHelper echoServer (9);

  ApplicationContainer serverApps = echoServer.Install (csmaNodes.Get (nCsma));
  serverApps.Start (Seconds (1.0));
  serverApps.Stop (Seconds (15.0));
        
UdpEchoClientHelper echoClient (csmaInterfaces.GetAddress (nCsma), 9);
  echoClient.SetAttribute ("MaxPackets", UintegerValue (10));
  echoClient.SetAttribute ("Interval", TimeValue (Seconds (0.2)));
  echoClient.SetAttribute ("PacketSize", UintegerValue (1024));


   ApplicationContainer clientApps = echoClient.Install (p2pNodes.Get (0));
  clientApps.Start (Seconds (1.0));
  clientApps.Stop (Seconds (15.0));


  ApplicationContainer clientApps1 = echoClient.Install (csmaNodes.Get (0));
  clientApps1.Start (Seconds (1.5));
  clientApps1.Stop (Seconds (15.0));

  ApplicationContainer clientApps2 = echoClient.Install (csmaNodes.Get (1));
  clientApps2.Start (Seconds (2.0));
  clientApps2.Stop (Seconds (15.0));

  ApplicationContainer clientApps3 = echoClient.Install (csmaNodes.Get (2));
  clientApps3.Start (Seconds (3.0));
  clientApps3.Stop (Seconds (15.0));

  
  //ApplicationContainer clientApps = 
    //echoClient.Install (wifiStaNodes.Get (nWifi - 1));
  //clientApps.Start (Seconds (2.0));
  //clientApps.Stop (Seconds (10.0));

ApplicationContainer clientApps4 = 
    echoClient.Install (wifiStaNodes.Get (0));
  clientApps.Start (Seconds (2.0));
  clientApps.Stop (Seconds (10.0));

ApplicationContainer clientApps5 = 
    echoClient.Install (wifiStaNodes.Get (1));
  clientApps.Start (Seconds (2.5));
  clientApps.Stop (Seconds (10.0));

ApplicationContainer clientApps6 = 
    echoClient.Install (wifiStaNodes.Get (2));
  clientApps.Start (Seconds (3.0));
  clientApps.Stop (Seconds (10.0));

  Ipv4GlobalRoutingHelper::PopulateRoutingTables ();

  Simulator::Stop (Seconds (10.0));

  if (tracing == true)
    {
      pointToPoint.EnablePcapAll ("third");
      phy.EnablePcap ("third", apDevices.Get (0));
      csma.EnablePcap ("third", csmaDevices.Get (0), true);
    }

 //
// Calculate Throughput using Flowmonitor
//
  FlowMonitorHelper flowmon;
  Ptr<FlowMonitor> monitor = flowmon.InstallAll();


//double t1=0;
//ns3::int64x64_t t2=0;
//
// Now, do the actual simulation.
//
  NS_LOG_INFO ("Run Simulation.");
  Simulator::Stop (Seconds(30.0));
  Simulator::Run ();

  monitor->CheckForLostPackets ();

  Ptr<Ipv4FlowClassifier> classifier = DynamicCast<Ipv4FlowClassifier> (flowmon.GetClassifier ());
  std::map<FlowId, FlowMonitor::FlowStats> stats = monitor->GetFlowStats ();
  for (std::map<FlowId, FlowMonitor::FlowStats>::const_iterator i = stats.begin (); i != stats.end (); ++i)
    {
         // std::cout << "Flow ";
	  Ipv4FlowClassifier::FiveTuple t = classifier->FindFlow (i->first);
      //if (((t.sourceAddress=="10.1.1.1" || t.sourceAddress=="10.1.2.3" || t.sourceAddress=="10.1.2.2" || t.sourceAddress=="10.1.2.1") && t.destinationAddress == "10.1.2.4"))
      //{
          std::cout<<"\n\n";
          std::cout << "Flow " << i->first  << " (" << t.sourceAddress << " -> " << t.destinationAddress << ")\n";
          std::cout << "  Tx Bytes:   " << i->second.txBytes << "\n";
          std::cout << "  Rx Bytes:   " << i->second.rxBytes << "\n";
           std::cout << "  Last Packet Recieved:   " << std::setprecision(5)<<i->second.timeLastRxPacket.GetSeconds()  << "\n";
           std::cout << "  First Packet Sent:   " <<std::setprecision(5)<<i->second.timeFirstTxPacket.GetSeconds()  << "\n";

      std::cout<<"Duration		: "<<i->second.timeLastRxPacket.GetSeconds()-i->second.timeFirstTxPacket.GetSeconds()<<std::endl;


      	  std::cout << "  Throughput: " << std::setprecision(5)<<i->second.rxBytes * 8.0 / (i->second.timeLastRxPacket.GetSeconds() - i->second.timeFirstTxPacket.GetSeconds())/1024/1024  << " Mbps\n";
                std::cout<<" seond.delaysum: "<<i->second.delaySum<<"\n";
                 std::cout<<"second.rxbytes"<<i->second.rxBytes<<"\n";
          std::cout<<" Mean Delay: "<<i->second.delaySum/(i->second.rxBytes/1024);

      //}
          std::cout<<"\n";
     }



  monitor->SerializeToXmlFile("lab-1.flowmon", true, true);
      

  Simulator::Destroy ();
  return 0;
}
