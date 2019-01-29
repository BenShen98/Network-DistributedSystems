package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.io.EOFException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

import common.MessageInfo;

public class RMIClient {

 private static final int registryPort = 1099;


 public static void main(String[] args) {

  RMIServerI server = null;
  long firstMsgT;
  long lastMsgT;

  // Check arguments for Server host and number of messages
  if (args.length < 2) {
   System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
   System.exit(-1);
  }

  String urlServer = new String("rmi://RMIServer");
  int countTo = Integer.parseInt(args[1]);

  // Initialise Security Manager
  firstMsgT=System.nanoTime();
  try {
   if (System.getSecurityManager() == null) {
    System.setSecurityManager(new SecurityManager());
   }

   // Bind to RMIServer
   Registry registry = LocateRegistry.getRegistry(args[0], registryPort);
   server = (RMIServerI) registry.lookup(urlServer);

   //Attempt to send messages the specified number of times
   //When server receives all data, server will shutdown, which will cause an EOFException
   for (int tries = 0; tries < countTo; tries++) {
    MessageInfo msg = new MessageInfo(countTo, tries);
    server.receiveMessage(msg);
   }

  } catch (Exception e) {
   if (e.getCause() instanceof EOFException) {
    //server received all file, shutdown
    lastMsgT=System.nanoTime();
    System.out.printf("\nfirst message received at %d ns", firstMsgT);
    System.out.printf("\nlast message received at %d ns", lastMsgT);
    System.out.printf("\ntime diff is %d ns\n", lastMsgT-firstMsgT);

    System.err.printf("rmi,%d,%d,%d,\n",firstMsgT,lastMsgT,countTo);


    System.exit(0);

   } else {
    System.out.println("Exception:" + e);
   }
  }
 }

}
