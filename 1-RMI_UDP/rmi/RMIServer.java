package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

 private static final String serverURL = "rmi://RMIServer";
 private static final int registryPort = 1099;

 private int totalMessages = -1;
 private int msgCounter = 0;

 private long startT;
 private long endT;
 private boolean logging;


 public RMIServer(boolean _logging) throws RemoteException {
  super();
  logging=_logging;
 }

 public void receiveMessage(MessageInfo msg) throws RemoteException {

  // On receipt of first message, initialise the receive buffer
  if (totalMessages == -1) {
   totalMessages = msg.totalMessages;
   startT=System.currentTimeMillis();
  }

  // Log receipt of the message, index&count start from 0
  if(logging){
    System.out.printf("%d,", msg.messageNum);
  }
  msgCounter++;


  // If this is the last expected message, then exit
  // (RMI is based on TCP, order guaranteed)
  if (msg.totalMessages - msg.messageNum == 1) {
    endT=System.currentTimeMillis();
   System.out.printf("\nfirst message received at %d ns", startT);
   System.out.printf("\nlast message received at %d ns", endT);
   System.out.printf("\ntime diff is %d ns\n", endT-startT);

   System.err.printf("rmi,%d,%d,%d,%d,%b,\n",startT,endT,totalMessages,msg.totalMessages-msgCounter,logging);

   System.exit(0);
  }

 }


 public static void main(String[] args) {
  RMIServer rmis = null;

  boolean logging = true;
  if(args.length == 1){
    // disable logging if contain 1 args
    logging=false;
  }

  // Initialise Security Manager
  if (System.getSecurityManager() == null) {
   System.setSecurityManager(new SecurityManager());
  }

  try {
   // Instantiate the server class
   rmis = new RMIServer(logging); // remote as it extend UnicastRemoteObject

   // Bind to RMI registry
   rebindServer(serverURL, rmis);
   System.out.println("Server bound, exit main");

  } catch (Exception e) {
   System.err.println("Server creation exception:" + e);
  }
 }



 protected static void rebindServer(String serverURL, RMIServer server) throws Exception {
  Registry registry = LocateRegistry.createRegistry(registryPort);
  registry.bind(serverURL, server);
 }

}
