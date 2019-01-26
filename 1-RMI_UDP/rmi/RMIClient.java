package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;

public class RMIClient {

 private static final int sleepMilli = 0;
 private static final int registryPort = 1099;

 public static void main(String[] args) {

  RMIServerI server = null;

  // Check arguments for Server host and number of messages
  if (args.length < 2) {
   System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
   System.exit(-1);
  }

  String urlServer = new String("rmi://" + args[0] + "/RMIServer");
  int countTo = Integer.parseInt(args[1]);

  // Initialise Security Manager
  try {
   if (System.getSecurityManager() == null) {
    System.setSecurityManager(new SecurityManager());
   }

   // Bind to RMIServer
   Registry registry = LocateRegistry.getRegistry(args[0], registryPort);
   server = (RMIServerI) registry.lookup(urlServer);

   //Attempt to send messages the specified number of times
   for (int tries = 0; tries < countTo; tries++) {
    MessageInfo msg = new MessageInfo(countTo, tries);
    server.receiveMessage(msg);
    Thread.sleep(sleepMilli);
   }

  } catch (Exception e) {
   System.out.println("Exception:" + e);
  }
 }
}
