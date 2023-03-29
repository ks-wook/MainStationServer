package Packet;

public abstract class Packet 
{
	
	protected byte _packetSize;
	protected byte _packetId;
	
	public abstract void printLog();
}