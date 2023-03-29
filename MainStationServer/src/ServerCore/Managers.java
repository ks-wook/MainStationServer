package ServerCore;

import Server.SessionManager;

public class Managers {

	private Managers() { };
	
	// managers
	private static ThreadManager threadManager = new ThreadManager();
	
	public static ThreadManager getThreadManager() {
		return threadManager;
	}
	
	private static SessionManager sessionManager = new SessionManager();
	
	public static SessionManager getSessionManager() {
		return sessionManager;
	}
}
