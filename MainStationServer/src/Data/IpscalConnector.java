package Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IpscalConnector
{
	public IpscalConnector() 
	{
		
	}
	
	public void ConnectAndRecvData(byte[] sendData) throws IOException
	{
		System.out.println("Ipscal connector On");
		
	    try (Socket client = new Socket()) 
	    {
	    	InetSocketAddress ip = new InetSocketAddress("127.0.0.1", 9999); // 로컬 호드트 9999번 포트 사용하여 프로그램간 통신
	    	client.connect(ip);
	      
	    	try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) 
	    	{

	    		// ----------------------- Send -------------------------------
		        // ByteBuffer를 통해 데이터 길이를 byte형식으로 변환
		        ByteBuffer sendbuffer = ByteBuffer.allocate(4);
		        sendbuffer.order(ByteOrder.LITTLE_ENDIAN);
		        sendbuffer.putInt(sendData.length);
		        
		        
		        // 데이터 길이 전송
		        sender.write(sendbuffer.array(), 0, 4);
		        // 데이터 전송
		        sender.write(sendData);
	    		// ----------------------- Send -------------------------------

		        
		        
		        // ----------------------- Receive ----------------------------
		        
		        // 연산 결과 수신
		        byte[] buffLen = new byte[4];
		        receiver.read(buffLen, 0, 4);
		        
		        ByteBuffer recvDataLen = ByteBuffer.wrap(buffLen);
		        recvDataLen.order(ByteOrder.LITTLE_ENDIAN);
		        int recvLen = recvDataLen.getInt();
		        byte[] recvData = new byte[recvLen];
		        receiver.read(recvData, 0, recvLen);
		         
		        // byte형식의 데이터를 string형식으로 변환
		        String msg = new String(recvData, "UTF-8");
		        
		        float[] result = StringToFloatResult(msg);
		        System.out.println("result, pos x: " + result[0] + " pos y: " + result[1]);
		        
		        // ----------------------- Receive ----------------------------	        
	    	}
	    } 
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	    
	    
	    
	}
	
	public float[] StringToFloatResult(String str)
	{
		String[] s = str.split(" ");
		
		if(s.length != 2) // 결과는 두개의 float 값을 가진다.
			return null;
		
		float[] posInfo = new float[2]; // x and y
		posInfo[0] = Float.parseFloat(s[0]);
		posInfo[1] = Float.parseFloat(s[1]);
		
		return posInfo;
	}
}






