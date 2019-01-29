package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


import common.MessageInfo;

public class UDPClient {
 public static void main(String[] args) {
  InetAddress serverAddr = null;
  int recvPort;
  int countTo;
  String message;

  long startT;
  long endT;

  // Get the parameters
  if (args.length < 3) {
   System.err.println("Arguments required: server name/IP, recv port, message count");
   System.exit(-1);
  }

	// Get server address
  try {
   serverAddr = InetAddress.getByName(args[0]);
  } catch (UnknownHostException e) {
   System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
   System.exit(-1);
  }
  recvPort = Integer.parseInt(args[1]);
  countTo = Integer.parseInt(args[2]);


  // Construct UDP client class and try to send messages
  UDPClientInst client = new UDPClientInst();

  startT=System.currentTimeMillis();
  client.testLoop(serverAddr, recvPort, countTo);
  endT=System.currentTimeMillis();

  System.out.printf("\nfirst message received at %d ns", startT);
  System.out.printf("\nlast message received at %d ns", endT);
  System.out.printf("\ntime diff is %d ns\n", endT-startT);

  System.err.printf("udp,%d,%d,%d,\n",startT,endT,countTo);


 }
}


class UDPClientInst {

 // Define constant
 private static final int sleepMilli = 0;

 private DatagramSocket sendSoc;

 public UDPClientInst() {

  // Initialise the UDP socket for sending data
  try {
   sendSoc = new DatagramSocket();
  } catch (SocketException e) {
   System.out.println("Socket: " + e.getMessage());
  }
 }

 public void testLoop(InetAddress serverAddr, int recvPort, int countTo) {

  //Send the messages to the server
   for (int tries = 0; tries < countTo; tries++) {
    MessageInfo msg = new MessageInfo(countTo, tries);
    send(msg.toString(), serverAddr, recvPort);
   }


  //close socket
  sendSoc.close();
 }

 private void send(String payload, InetAddress destAddr, int destPort) {
  int payloadSize;
  byte[] pktData;
  DatagramPacket pkt;

  // build the datagram packet and send it to the server
  try {
   pktData = payload.getBytes(StandardCharsets.US_ASCII);
   payloadSize = pktData.length;
   pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);

   String str = new String(pkt.getData(), StandardCharsets.US_ASCII);

   sendSoc.send(pkt);
  } catch (IOException e) {
   System.out.println("IO: " + e.getMessage());
  }
 }

}
