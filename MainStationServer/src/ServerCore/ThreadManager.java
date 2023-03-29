package ServerCore;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadManager {
	
	private static final int MaxThread = 10;

	static ThreadPoolExecutor _threadPool = 
			(ThreadPoolExecutor) Executors.newFixedThreadPool(MaxThread);
	
	public void startThread(Thread thread) 
	{
		_threadPool.submit(thread);
	}
	
}
