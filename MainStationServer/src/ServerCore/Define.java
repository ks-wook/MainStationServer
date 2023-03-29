package ServerCore;


public class Define {

	
	// ------------- Packet ---------------
	public static enum PacketId {
		
		Conc(1),
		Sync(2),
		Disc(3),;
		
		private final int value;
		PacketId(int value) {
			this.value = value;
		}
		public int getValue() { return value; }
	}
	
	public static enum LogId {
		
		Send(4),
		Recv(5),;
		
		private final int value;
		LogId (int value) {
			this.value = value;
		}
		public int getValue() { return value; }
		
	}

	
	public static enum PacketSize {
		
		// TODO
		SyncSize(5),
		DiscSize(2),;

		
		private final int value;
		PacketSize(int value) {
			this.value = value;
		}
		public int getValue() { return value; }
	}
	
	
	// ------------- Packet ---------------

	
	
	// ------------- ID ---------------
	
	public static enum IDType 
	{
		
		User((byte) 16),
		Space((byte) 32),
		Router((byte) 64),
		Device((byte) 96),
		Beacon((byte) 160),;
		
		private final byte value;
		IDType(byte value) {
			this.value = value;
		}
		public byte getValue() { return value; }
	}
	
	
	
	
	
	
	
	
	
	
	
	

}