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
import java.nio.charset.StandardCharsets;

import common.MessageInfo;

public class UDPServer {

	// define constant
	private static final int bufSize=1000;
	private static final int timeout=20000;
	private static final int maxContinuousExceptionCount=3; //prevent infinite loop

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int countdownMessages = -1;
	private boolean[] receivedMsg = null; //ptr
	private int ExcpetionCount=0;
	private boolean terminate=false;


	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;
		byte[] buffer=new byte[bufSize];

		// Receive the messages and process them by calling processMessage(...).
		while(!terminate && ExcpetionCount<maxContinuousExceptionCount){
			try{
				pac=new DatagramPacket(buffer,bufSize);
				recvSoc.receive(pac); //block receive
				String serialized=new String(pac.getData(),StandardCharsets.US_ASCII);
				processMessage(serialized);

				ExcpetionCount=0; //reset timeout counter
			}catch(SocketException e){System.out.println("Socket: "+e.getMessage());
		}catch(IOException e){
			System.out.println("IO: "+e.getMessage());
			ExcpetionCount++;
			}
		}

		//print out data
		System.out.println("### missed packet ###");
		for( int i=0; i<totalMessages; i++){
			if(!receivedMsg[i]){
				System.out.printf("%d,",i);
			}
		}
		System.out.println("### end of missed packet ###");

		//return code (return msg not sent)
		System.exit(countdownMessages); //only display last byte

	}

	public void processMessage(String data) {


		MessageInfo msg = null;

		// Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data);

			// On receipt of first message, initialise the receive buffer
			if(totalMessages==-1){
				totalMessages=msg.totalMessages;
				receivedMsg=new boolean[totalMessages];
				countdownMessages=totalMessages;
			}

			// Log receipt of the message, index&count start from 0
			System.out.printf("%d,",msg.messageNum);
			receivedMsg[msg.messageNum]=true;
			countdownMessages--;

			// If this is the last expected message, then exit
			if(countdownMessages==0){
				terminate=true;
			}
		}catch(Exception e){
			System.err.println("ignored: "+e.getMessage());
		}

	}


	public UDPServer(int rp) {
		// Initialise UDP socket for receiving data.
		try{
			recvSoc = new DatagramSocket(rp);
			// Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
			recvSoc.setSoTimeout(timeout);
		}catch(SocketException e){System.out.println("Socket: "+e.getMessage());
		}


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
			UDPServer server = new UDPServer(recvPort);
			server.run();


	}

}
