package Packet;

import java.nio.channels.AsynchronousSocketChannel;
import ServerCore.Define;

public class DisconnectPacket extends Packet{

	protected AsynchronousSocketChannel _channel;
	
	public DisconnectPacket(AsynchronousSocketChannel channel) 
	{
		_channel = channel;
		
		this._packetId = (byte) Define.PacketId.Disc.getValue();
		this._packetSize = (byte) Define.PacketSize.DiscSize.getValue();
	}
	
	@Override
	public void printLog() {
		try 
		{
			System.out.println(
					"Log - " + "[" + this._packetId + "]" + " - " + 
					_channel.getRemoteAddress() + " disconnected");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
