package Server;

import Packet.DisconnectPacket;
import Packet.SyncPacket;
import ServerCore.Define;
import ServerCore.Session;

public class ClientSession extends Session{
	
	
	@Override
	public void OnConnected() 
	{
		try 
		{
			System.out.println(
					"Log - " + "[" + Define.PacketId.Conc.getValue() + "]" + 
					" - " + _channel.getRemoteAddress() + " connected");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void OnRecvPacket(byte[] data) {
		
		try 
		{
			int packetId = data[1];
			
			if(packetId == Define.PacketId.Sync.getValue())
			{
				SyncPacket syncPacket =  new SyncPacket(data[2], data[3], data[4]);
				syncPacket.printLog();
			}
			else if(packetId == Define.PacketId.Disc.getValue())
			{
				DisconnectPacket discPacket = new DisconnectPacket(_channel);
				discPacket.printLog();
				this.Disconnect();
			}
			
			
			
		}
		catch(Exception e) 
		{
			System.out.println(e.toString());
		}
		
	}


	@Override
	public void OnDisconnected() 
	{
		// TODO Auto-generated method stub
	}

}
