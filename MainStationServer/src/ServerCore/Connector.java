package ServerCore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

import Client.ServerSession;

public class Connector 
{
	
	AsynchronousChannelGroup _channelGroup;
    AsynchronousSocketChannel _iocpSocket;
	
	String ServerIP;
	int port;
	
	// test packet
	byte[] sync = {(byte) Define.PacketSize.SyncSize.getValue(), 
				   (byte)Define.PacketId.Sync.getValue(), 
				   (byte) 10, (byte) 20, (byte) 30}; // dummy message
				
	
	byte[] disc = {(byte) Define.PacketSize.DiscSize.getValue(), 
				   (byte) Define.PacketId.Disc.getValue() };
	
	
	public Connector(String ip, int port)
	{
		this.ServerIP = ip;
		this.port = port;
	}

	// dummy value
	int x = 10;
	int y = 20;
	int z = 30;
	
	public void Init() 
	{
		
		try 
        {
        	_channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(), 
                    Executors.defaultThreadFactory());
        	_iocpSocket = AsynchronousSocketChannel.open(_channelGroup);
            
            
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        } 
		
		
		
		// async connect
		RegisterConnect();
		
		
		    
	}
	
	public void RegisterConnect()
	{
		_iocpSocket.connect(new InetSocketAddress(ServerIP, port), null, ConnectCompletionHandler());
	}
	
	private CompletionHandler<Void, Void> ConnectCompletionHandler()
    {
        return new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                // TODO Auto-generated method stub
            	
            	try 
            	{
        			ServerSession session = new ServerSession();
        			session.Init(_iocpSocket);
        			session.OnConnected();
        			
        			session.Send(sync);
        			session.Send(sync);

        			Thread.sleep(1000);

        			session.Send(disc);
        			


            	}
            	catch (Exception e) 
            	{
					// TODO Auto-generated catch block
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