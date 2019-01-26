/*
 * Created on 01-Mar-2016
 */
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

	private static final String serverURL="rmi://localhost/RMIServer";

	private int totalMessages = -1;
	// private boolean[] receivedMsg = null; //ptr


	public RMIServer() throws RemoteException {
		super();
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// On receipt of first message, initialise the receive buffer
		if(totalMessages==-1){
			totalMessages=msg.totalMessages;
			// receivedMsg=new boolean[totalMessages];
		}

		// Log receipt of the message, index&count start from 0
		System.out.printf("%d,",msg.messageNum);
		// receivedMsg[msg.messageNum]=true;

		// If this is the last expected message, then exit
		// RMI is based on TCP, order guaranteed
		if(msg.totalMessages-msg.messageNum==0){
			System.exit(0);
		}

	}


	public static void main(String[] args) {
			RMIServer rmis = null;

			// Initialise Security Manager
			if (System.getSecurityManager() == null) {
				System.setSecurityManager (new SecurityManager ());
			}

			try{
			// Instantiate the server class
			rmis = new RMIServer();// remote as it extend UnicastRemoteObject
			// RMIServer stub = (RMIServer) UnicastRemoteObject.exportObject(server, 0);

			// Bind to RMI registry
			rebindServer(serverURL, rmis);

		}catch(Exception e){
			System.err.println("Server creation exception:"+e);
      // e.printStackTrace();
		}
	}

	protected static void rebindServer(String serverURL, RMIServer server) throws Exception {

		// create registry if not runing
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.bind(serverURL, server);
		RMIServerI serverx = (RMIServerI) registry.lookup("rmi://localhost/RMIServer");
		System.out.println(serverURL);

		MessageInfo msg = new MessageInfo(1,0);
		serverx.receiveMessage(msg);

		System.out.println("Server bound, exit main");
	}
}
