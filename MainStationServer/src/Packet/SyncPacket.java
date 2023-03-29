package Packet;

import ServerCore.Define;

public class SyncPacket extends Packet{
	
	int _rssiValue1 = 0;
	int _rssiValue2 = 0;
	int _rssiValue3 = 0;
	
	public SyncPacket (int rssiValue1, int rssiValue2, int RssiValue3) {
		
		_rssiValue1 = rssiValue1;
		_rssiValue2 = rssiValue2;
		_rssiValue3 = RssiValue3;
		
		this._packetId = (byte) Define.PacketId.Sync.getValue();
		this._packetSize = (byte) Define.PacketSize.SyncSize.getValue();
		
	}
	
	@Override
	public void printLog() {
		System.out.println(
				"Log - " + "[" + this._packetId + "]" + 
				" - " + _rssiValue1 + _rssiValue2 + _rssiValue3);
	}
}
