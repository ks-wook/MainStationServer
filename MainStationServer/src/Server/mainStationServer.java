package Server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

import Data.DataManager;
import Data.DataManager.*;
import Data.IpscalConnector;
import ServerCore.Listener;

public class mainStationServer {
	
	private static final int PORT_NUMBER = 5001;
	private static final int BROADCAST_PORT = 8200;
 
	
	// Temp
	public static byte[] updateid;
	
	// ----------------------- Test data --------------------------
	// 모든 데이터를 한번에 담아 보냄
	static byte[] sendData = new byte[] 
	{ 
		-53, -54, -54, -55, -55, -55, -56, -56, -80, -57,
		-53, -54, -54, -55, -55, -55, -56, -56, -80, -57,
		-53, -54, -54, -55, -55, -55, -56, -56, -80, -57, // 0 ~ 29 : Rssi raw data
		5, 0, 10, 8, 0, 10, // 30 ~ 35 : Position data
		-40, -42, -43 // 36 ~ 38 : Preset data
	};
	// ----------------------- Test data --------------------------
	
	
	public static void main(String[] args) throws IOException {		
		
		System.out.println("Server on");
		
		// Ipscal Test
		for(int i = 0; i < 10; i++)
		{
			IpscalConnector ip = new IpscalConnector();
			ip.ConnectAndRecvData(sendData);
		}
		
		// Db Test
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
		


		// server On
		/*
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
		*/
		
	}

}
