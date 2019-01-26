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

 // Define constant
 private static final int bufSize = 1000;
 private static final int timeout = 20000;
 private static final int maxContinuousExceptionCount = 3;

 private DatagramSocket recvSoc;
 private int totalMessages = -1;
 private int countdownMessages = -1;
 private boolean[] receivedMsg = null; //ptr
 private int ExcpetionCount = 0;
 private boolean terminate = false;


 private void run() {
  int pacSize;
  byte[] pacData;
  DatagramPacket pac;
  byte[] buffer = new byte[bufSize];

  // Receive the messages and process them by calling processMessage(...).
	// shutdown server when no. of continuous (timeout) exception exceed maxContinuousExceptionCount
  while (!terminate && ExcpetionCount < maxContinuousExceptionCount) {
   try {
    pac = new DatagramPacket(buffer, bufSize);
    recvSoc.receive(pac); //block receive
    String serialized = new String(pac.getData(), StandardCharsets.US_ASCII);
    processMessage(serialized);
    ExcpetionCount = 0; //reset timeout counter

   } catch (Exception e) {
    System.out.println("Exception: " + e.getMessage());
    ExcpetionCount++;
   }
  }

  //print out missing packet
  System.out.println("### missed packet ###");
  for (int i = 0; i < totalMessages; i++) {
   if (!receivedMsg[i]) {
    System.out.printf("%d,", i);
   }
  }
  System.out.println("### end of missed packet ###");

  //return code (return no. of message lost)
  System.exit(countdownMessages); //will only display last byte

 }

 public void processMessage(String data) {

  MessageInfo msg = null;

  try {
	 // Use the data to construct a new MessageInfo object
   msg = new MessageInfo(data);

   // On receipt of first message, initialise the receive buffer
   if (totalMessages == -1) {
    totalMessages = msg.totalMessages;
    receivedMsg = new boolean[totalMessages];
    countdownMessages = totalMessages;
   }

   // Log receipt of the message, (index&count start from 0)
   System.out.printf("%d,", msg.messageNum);
   receivedMsg[msg.messageNum] = true;
   countdownMessages--;

   // If this is the last expected message, then exit
   if (countdownMessages == 0) {
    terminate = true;
   }
  } catch (Exception e) {
   System.err.println("ignored: " + e.getMessage());
  }

 }


 public UDPServer(int rp) {
  // Initialise UDP socket for receiving data.
  try {
   recvSoc = new DatagramSocket(rp);
   recvSoc.setSoTimeout(timeout); // prevent been blocked forever
  } catch (SocketException e) {
   System.out.println("Socket: " + e.getMessage());
  }

  // Done Initialisation
  System.out.println("UDPServer ready");
 }


 public static void main(String args[]) {
  int recvPort;

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
