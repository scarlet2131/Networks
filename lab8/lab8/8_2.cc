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

#include "ns3/core-module.h"
#include "ns3/point-to-point-module.h"
#include "ns3/network-module.h"
#include "ns3/applications-module.h"
#include "ns3/wifi-module.h"
#include "ns3/netanim-module.h"
#include "ns3/mobility-module.h"
#include "ns3/csma-module.h"
#include "ns3/internet-module.h"
#include "ns3/ipv4-global-routing-helper.h"
#include "ns3/flow-monitor-module.h"

#include "ns3/packet-socket-helper.h"
#include<iostream>
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
  csma.SetChannelAttribute ("Delay", TimeValue (NanoSeconds (6560)));

  NetDeviceContainer csmaDevices;
  csmaDevices = csma.Install (csmaNodes);

  NodeContainer wifiStaNodes;
  NodeContainer wifiApNode = p2pNodes.Get (0);
  wifiStaNodes.Create (nWifi);


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

  ApplicationContainer serverApps = echoServer.Install (p2pNodes.Get (0));
  serverApps.Start (Seconds (1.0));
  serverApps.Stop (Seconds (20.0));

  UdpEchoClientHelper echoClient (p2pInterfaces.GetAddress (0), 9);
  echoClient.SetAttribute ("MaxPackets", UintegerValue (4));
  echoClient.SetAttribute ("Interval", TimeValue (Seconds (1.0)));
  echoClient.SetAttribute ("PacketSize", UintegerValue (1024));

  ApplicationContainer clientApps = echoClient.Install (csmaNodes.Get (nCsma));
  clientApps.Start (Seconds (1.0));
  clientApps.Stop (Seconds (22.0));
    ApplicationContainer clientApps1 = echoClient.Install (csmaNodes.Get (0));
  clientApps1.Start (Seconds (2.0));
  clientApps1.Stop (Seconds (22.0));
     ApplicationContainer clientApps2 = echoClient.Install (csmaNodes.Get (1));
  clientApps2.Start (Seconds (1.0));
  clientApps2.Stop (Seconds (22.0));
     ApplicationContainer clientApps3 = echoClient.Install (csmaNodes.Get (2));
  clientApps3.Start (Seconds (1.5));
  clientApps3.Stop (Seconds (22.0));


  ApplicationContainer clientApps4 =
    echoClient.Install (wifiStaNodes.Get (nWifi - 1));
  clientApps4.Start (Seconds (2.0));
  clientApps4.Stop (Seconds (22.0));
ApplicationContainer clientApps5 =
    echoClient.Install (wifiStaNodes.Get (nWifi - 2));
  clientApps5.Start (Seconds (2.5));
  clientApps5.Stop (Seconds (22.0));
ApplicationContainer clientApps6 =
    echoClient.Install (wifiStaNodes.Get (nWifi - 3));
  clientApps6.Start (Seconds (3.0));
  clientApps6.Stop (Seconds (22.0));

  Ipv4GlobalRoutingHelper::PopulateRoutingTables ();

  Simulator::Stop (Seconds (22.0));

AnimationInterface anim ("8th.xml");


   // Flow monitor
Ptr<FlowMonitor> flowMonitor;
FlowMonitorHelper flowHelper;
flowMonitor = flowHelper.InstallAll();
double avg=0;
//Simulator::Stop (Seconds(25));
Simulator::Run ();
flowMonitor->CheckForLostPackets();
		std::map<FlowId, FlowMonitor::FlowStats> flowStats = flowMonitor->GetFlowStats();
		Ptr<Ipv4FlowClassifier> classing = DynamicCast<Ipv4FlowClassifier> (flowHelper.GetClassifier());
		for (std::map<FlowId, FlowMonitor::FlowStats>::const_iterator stats = flowStats.begin (); stats != flowStats.end (); ++stats)
		{
			Ipv4FlowClassifier::FiveTuple fiveTuple = classing->FindFlow (stats->first);
			std::cout<<"Flow ID			: " << stats->first <<" ; "<< fiveTuple.sourceAddress <<" -----> "<<fiveTuple.destinationAddress<<std::endl;
			std::cout<<"Tx Packets = " << stats->second.txPackets<<std::endl;
			std::cout<<"Rx Packets = " << stats->second.rxPackets<<std::endl;
			std::cout<<"Duration		: "<<stats->second.timeLastRxPacket.GetSeconds()-stats->second.timeFirstTxPacket.GetSeconds()<<std::endl;
			std::cout<<"Last Received Packet	: "<< std::setprecision(5)<<stats->second.timeLastRxPacket.GetSeconds()<<" Seconds"<<std::endl;
             std::cout<<" Mean Delay: "<<stats->second.delaySum/(stats->second.rxBytes/1024);

            avg+=stats->second.rxBytes * 8.0 / (stats->second.timeLastRxPacket.GetSeconds()-stats->second.timeFirstTxPacket.GetSeconds())/1024/1024;

			std::cout<<"Throughput: " << std::setprecision(5)<<stats->second.rxBytes * 8.0 / (stats->second.timeLastRxPacket.GetSeconds()-stats->second.timeFirstTxPacket.GetSeconds())/1024/1024  << " Mbps"<<std::endl;
			std::cout<<"---------------------------------------------------------------------------"<<std::endl;
		}

flowMonitor->SerializeToXmlFile("NameOfFile1.xml", true, true);

  Simulator::Destroy ();
  return 0;
}
