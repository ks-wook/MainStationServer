package Server;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

import Data.DataManager;
import Data.DataManager.*;
import ServerCore.Listener;

public class mainStationServer {
	
	private static final int PORT_NUMBER = 5001;
	private static final int BROADCAST_PORT = 8200;
 
	
	// Temp
	public static byte[] updateid;
	
	public static void main(String[] args) {		
		
		System.out.println("Server on");
		
		/*
		// user insert
		InsertUser insertuser = new InsertUser("테스트유저4", null, null, null, null, null);
		DataManager.getInstance().Push(insertuser);

		// device insert
		byte[] deviceId = {1, 2, 3, 4, 5, 6}; // dummy data
		InsertDevice insertdevice = new InsertDevice(null, deviceId, null, null, null, null);
		DataManager.getInstance().Push(insertdevice);
		
		// insert beacon
		byte[] spaceId = {1, 2, 3, 4, 5, 6}; // dummy data
		InsertBeacon insertbeacon = new InsertBeacon(null, null, spaceId, null, null, null);
		DataManager.getInstance().Push(insertbeacon);

		// insert router
		byte[] MacAdr = {1, 2, 3, 4, 5, 6}; // dummy data
		InsertRouter insertrouter = new InsertRouter("라우터1", null, spaceId, MacAdr, null, null);
		DataManager.getInstance().Push(insertrouter);
	
		// insert space
		InsertSpace insertspace = new InsertSpace("공간1", null, null, null, null, null);
		DataManager.getInstance().Push(insertspace);
		
		
		// update state(beacon)
		byte[] state = {0, 1};
		UpdateState updatestate = new UpdateState(null, null, null, null, updateid, state);
		DataManager.getInstance().Push(updatestate);
		*/
		


		
		try 
		{
			// TCP connection listener
			Listener listener = new Listener(PORT_NUMBER);
			listener.init();
			
			
			// -----------------------------------------------

			
			// UDP broadcast connection listener
			DatagramSocket recvSocket = new DatagramSocket(BROADCAST_PORT);
			
			// ping message
			byte[] buf = new byte[1];
			DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
		
			
			while(true)
			{
				recvSocket.receive(recvPacket);
				
				InetAddress clientAddress = InetAddress.getByName(recvPacket.getAddress().getHostAddress());
				
				DatagramSocket sendSocket = new DatagramSocket();
				DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, clientAddress, BROADCAST_PORT);
				
				sendSocket.send(sendPacket);
				sendSocket.close();
			}
			
			
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
	}

}
