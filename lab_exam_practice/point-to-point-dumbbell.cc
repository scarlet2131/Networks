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
 *
 * Author: George F. Riley<riley@ece.gatech.edu>
 */

// Implement an object to create a dumbbell topology.

#include <cmath>
#include <iostream>
#include <sstream>

// ns3 includes
#include "ns3/log.h"
#include "ns3/point-to-point-dumbbell.h"
#include "ns3/constant-position-mobility-model.h"

#include "ns3/node-list.h"
#include "ns3/point-to-point-net-device.h"
#include "ns3/vector.h"
#include "ns3/ipv6-address-generator.h"

namespace ns3 {

NS_LOG_COMPONENT_DEFINE ("PointToPointDumbbellHelper");

PointToPointDumbbellHelper::PointToPointDumbbellHelper (uint32_t nLeftLeaf,
                                                        PointToPointHelper leftHelper,
                                                        uint32_t nRightLeaf,
                                                        PointToPointHelper rightHelper,
                                                        PointToPointHelper bottleneckHelper)
{
  // Create the bottleneck routers
  m_routers.Create (2);                     //NODE6----NODE5  
  // Create the leaf nodes
  m_leftLeaf.Create (2);                    //NODE1----NODE2
  m_rightLeaf.Create (2);                   //NODE3----NODE4

  // Add the link connecting routers
  m_routerDevices = bottleneckHelper.Install (m_routers);
  // Add the left side links
  for (uint32_t i = 0; i < 2; ++i)
    {
      NetDeviceContainer c = leftHelper.Install (m_routers.Get (0),
                                                 m_leftLeaf.Get (i));
      m_leftRouterDevices.Add (c.Get (0));
      m_leftLeafDevices.Add (c.Get (1));
    }
  // Add the right side links
  for (uint32_t i = 0; i < 2; ++i)
    {
      NetDeviceContainer c = rightHelper.Install (m_routers.Get (1),
                                                  m_rightLeaf.Get (i));
      m_rightRouterDevices.Add (c.Get (0));
      m_rightLeafDevices.Add (c.Get (1));
    }
}


  // Assign the router network
  m_routerInterfaces = routerIp.Assign (m_routerDevices);
  // Assign to left side 
  for (uint32_t i = 0; i < LeftCount (); ++i)
    {
      NetDeviceContainer ndc;
      ndc.Add (m_leftLeafDevices.Get (i));
      ndc.Add (m_leftRouterDevices.Get (i));
      Ipv4InterfaceContainer ifc = leftIp.Assign (ndc);
      m_leftLeafInterfaces.Add (ifc.Get (0));
      m_leftRouterInterfaces.Add (ifc.Get (1));
      leftIp.NewNetwork ();
    }
  // Assign to right side
  for (uint32_t i = 0; i < RightCount (); ++i)
    {
      NetDeviceContainer ndc;
      ndc.Add (m_rightLeafDevices.Get (i));
      ndc.Add (m_rightRouterDevices.Get (i));
      Ipv4InterfaceContainer ifc = rightIp.Assign (ndc);
      m_rightLeafInterfaces.Add (ifc.Get (0));
      m_rightRouterInterfaces.Add (ifc.Get (1));
      rightIp.NewNetwork ();
    }
}



} // namespace ns3
