package Client;


import ServerCore.Connector;

public class DummyClient {

	private static final int clientNum = 20;
	
	public static void main(String[] args) {
		String ip = "127.0.0.1";
        int port = 5001;
        
        
        
        
        for(int i = 0; i < clientNum; i++)
        {
        	Connector connector = new Connector(ip, port);
            connector.Init();
        }
	}

}
