package ServerCore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

import Server.ClientSession;

// iocp listsener
public class Listener {
	
	// test packet
		byte[] sync = {(byte) Define.PacketSize.SyncSize.getValue(), 
					   (byte)Define.PacketId.Sync.getValue(), 
					   (byte) 10, (byte) 20, (byte) 30}; // dummy message
					
		
		byte[] disc = {(byte) Define.PacketSize.DiscSize.getValue(), 
					   (byte) Define.PacketId.Disc.getValue() };
		
		
	AsynchronousChannelGroup _channelGroup;
    AsynchronousServerSocketChannel _iocpServerSocket;

    private int PORTNUMBER;
        
	public Listener(int port)
	{
		this.PORTNUMBER = port;
	}
	
	public void init() throws IOException 
	{
        try 
        {
        	_channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(), 
                    Executors.defaultThreadFactory());
        	_iocpServerSocket = AsynchronousServerSocketChannel.open(_channelGroup);
            
            InetSocketAddress inetSocketAddress = new InetSocketAddress(PORTNUMBER);
            _iocpServerSocket.bind(inetSocketAddress);
            
        } 
        catch (IOException e)
        {
        	e.printStackTrace();
        } 
        
        
        RegisgerAccept();
    }
		
	
	public void RegisgerAccept() 
	{
		_iocpServerSocket.accept(null, AcceptCompletionHandler());
	}
	
	
	private CompletionHandler<AsynchronousSocketChannel, Void> AcceptCompletionHandler()
    {
        return new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, Void attachment) {
                // TODO Auto-generated method stub
            	
            	try 
            	{
					ClientSession session = new ClientSession();
					session.Init(channel);
					session.OnConnected();
					
	            	RegisgerAccept();

	            	
					// test
				    for(int i = 0; i < 20; i++)
				    {
						session.Send(sync);
						Thread.sleep(3000);
				    }
				    
            	}
            	catch (Exception e) 
            	{
					e.printStackTrace();
				}
            
            }
            
            @Override
            public void failed(Throwable exc, Void attachment) 
            {
            	System.out.println("accept failed");
            }
        };
    }
	
	
	
}



