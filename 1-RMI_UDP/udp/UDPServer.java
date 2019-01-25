/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	// private int[] receivedMessages;
	private boolean[] receivedMsg = null; //ptr
	// private boolean close;
	private boolean finish=false;


	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// Receive the messages and process them by calling processMessage(...).
		while(!finish){

		}


	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// Use the data to construct a new MessageInfo object
		msg = MessageInfo(data);

		// On receipt of first message, initialise the receive buffer
		if(totalMessages==-1){
			totalMessages=msg.totalMessages;
			receivedMsg=new boolean[totalMessages];
		}

		// Log receipt of the message, index&count start from 0
		System.out.printf("%d,",msg.messageNum);
		receivedMsg[msg.messageNum]=true;

		// ??? msg may not arrive in order !!!
		// If this is the last expected message, then identify
		//        any missing messages

	}


	public UDPServer(int rp) {
		// Initialise UDP socket for receiving data
		recvSoc = new DatagramSocket(rp);
		// Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		receSoc.setSoTimeout(30000);

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// Construct Server object and start it by calling run().
			UDPServer server=UDPServer(recvPort);
			server.run();


	}

}
