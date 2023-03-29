package Server;

import java.util.HashMap;

public class SessionManager {
	
	private HashMap<String, ClientSession> _sessions = new HashMap<>();
	
	int sessionId = 0;
	
	public synchronized String addSession(ClientSession session) 
	{
		_sessions.put(String.valueOf(sessionId++), session);
		return String.valueOf(sessionId);
	}
	
	public synchronized void removeSession(String key) 
	{
		_sessions.remove(key);
	}
}
