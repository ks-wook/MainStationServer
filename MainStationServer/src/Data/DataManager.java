package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Server.mainStationServer;
import ServerCore.Define.IDType;
 
public class DataManager {
	
    private static Connection conn; //DB 커넥션 연결 객체
    
    // db 접속 관련 데이터는 외부 json 파일로 추출하여 관리
    private static final String USERNAME = "Your Id";//DBMS접속 시 아이디
    private static final String PASSWORD = "Your password";//DBMS접속 시 비밀번호
    private static final String URL = "Your database"; //DBMS접속할 db명
    
    // 싱글턴
    private static HashMap<IDType, Byte> _IdCounter = new HashMap<>();
    
    static DataManager _instance = new DataManager();
    
    static public DataManager getInstance() { return _instance; } 
    
    private DataManager() 
    {
    	
    	_IdCounter.put(IDType.User, (byte) 0);
    	_IdCounter.put(IDType.Space, (byte) 0);
    	_IdCounter.put(IDType.Router, (byte) 0);
    	_IdCounter.put(IDType.Device, (byte) 0);
    	_IdCounter.put(IDType.Beacon, (byte) 0);

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("dataBase connected");
        } 
        catch (Exception e) 
        {
            System.out.println("dataBase connect failed");
            try
            {
                conn.close();
            } 
            catch (SQLException e1) {    }
        }
        
        
    }
    
    // ------------- id -----------------
    
    // id 생성 
    private static byte[] generateId(IDType type) 
    {
    	byte[] id = {0, 0, 0, 0, 0, 0};
    	id[0] = (byte) (type.getValue() + _IdCounter.get(type));
    	increaseId(type); 
    	
    	Random random = new Random();
    	for(int i = 1; i < id.length; i++)
    		id[i] = (byte) random.nextInt(255);
    	
    	
    	return id;
    }
    
    // id 가중치 증가
    private static void increaseId(IDType type)
    {
    	byte lastId = _IdCounter.get(type);
    	lastId ++;
    	if(type == IDType.User)
    	{
    		if(lastId > (byte) 16)
    			lastId = 0;
    	}
    	else if(type == IDType.Space)
    	{
    		if(lastId > (byte) 32)
    			lastId = 0;
    	}
    	else if(type == IDType.Router)
    	{
    		if(lastId > (byte) 32)
    			lastId = 0;
    	}
    	else if(type != IDType.Device)
    	{
    		if(lastId > (byte) 16)
    			lastId = 0;
    	}
    	else // beacon
    	{
    		if(lastId > (byte) 80)
    			lastId = 0;
    	}
    	
    	_IdCounter.put(type, lastId);
    }
    
    public static IDType getTypeById(byte[] id)
    {
    	int type = id[0];
    	if(type < (byte) 0)
    		type += 256;
    	
    	if(type >= 0x10 && type <= 0x1F)
    		return IDType.User;
    	else if(type >= 0x20 && type <= 0x3F)
    		return IDType.Space;
    	else if(type >= 0x40 && type <= 0x5F)
    		return IDType.Router;
    	else if(type >= 0x60 && type <= 0x9F)
    		return IDType.Device;
    	else if(type >= 0xA0 && type <= 0xDF)
    		return IDType.Beacon;
    	
    	return null;
    }
    
    
    // ------------- id -----------------

    
    
    
    // ------------- sql ----------------
    private Queue<SQL> SQLQueue = new LinkedList<>();
    
    public void Push(SQL query)
    {
    	
    	if(!SQLQueue.isEmpty())  // 다른 쓰레드에서 비동기 작업 수행 중
    	{
    		EnQueue(query);
        	return;
    	}
    	else // 아직 다른 쓰레드에서 접근해서 사용하지 않는 경우
    	{
    		EnQueue(query);
    		while(!SQLQueue.isEmpty()) // 큐가 빌때 까지
    		{
    			Pop();
    		}
    	}

    }
    
    synchronized public void EnQueue(SQL query)
    {
    	SQLQueue.add(query);
    }
    
    synchronized public void Pop()
    {
    	SQLQueue.poll().executeQyery();
    }
    
    // 커맨드 패턴 적용을 위한 인터페이스 선언
    public interface SQL
    {
    	byte[] executeQyery ();
    }
    
    
    public static class InsertUser implements SQL 
    {
    	String name;
    	byte[] OwnerId;
    	byte[] SpaceId;
    	byte[] MacAdr;
    	byte[] Id;
    	byte[] State;
    	
    	public InsertUser (String name, byte[] OwnerId, byte[] SpaceId, byte[] MacAdr, byte[] Id, byte[] State)
    	{
    		this.name = name;
    		this.OwnerId = OwnerId;
    		this.SpaceId = SpaceId;
    		this.MacAdr = MacAdr;
    		this.Id = Id;
    		this.State = State;
    	}
    	
    	// 쿼리 실행 메서드 -> 모든 쿼리문 공통
		@Override
		public byte[] executeQyery() {
			// TODO Auto-generated method stub

	    	// insert 쿼리문
	        String sql = "insert into user values(?,?)";
	        
	        // user type의 id 생성
	        byte[] id = generateId(IDType.User);
	        
	        PreparedStatement pstmt = null;
	        
	        try 
	        {
	        	
	            pstmt = conn.prepareStatement(sql);        
	            pstmt.setBytes(1, id);
	            pstmt.setString(2, name);

	            
	            int result = pstmt.executeUpdate();
	            
	            // --------- Debugging ------------
	            if(result==1) 
	                System.out.println("user data insert success");
	                
	            System.out.print("inserted User Id: ");
	        	for(int i = 0; i< id.length; i++)
	            	System.out.print(id[i]);
	        	System.out.println("");
	            // --------------------------------
	            
	        } 
	        catch (Exception e) 
	        {
	        	e.printStackTrace();
	            System.out.println("user data insert failed");
	        }    
	        finally 
	        {
	            try 
	            {
	                if(pstmt!=null && !pstmt.isClosed()) 
	                    pstmt.close();
	            } 
	            catch (Exception e2) { }
	        }
	        
	        // 생성한 user id 반환
	        return id;
		}
    	
    }
    
    public static class InsertDevice implements SQL 
    {
    	String name;
    	byte[] OwnerId;
    	byte[] SpaceId;
    	byte[] MacAdr;
    	byte[] Id;
    	byte[] State;
    	
    	public InsertDevice (String name, byte[] OwnerId, byte[] SpaceId, byte[] MacAdr, byte[] Id, byte[] State)
    	{
    		this.name = name;
    		this.OwnerId = OwnerId;
    		this.SpaceId = SpaceId;
    		this.MacAdr = MacAdr;
    		this.Id = Id;
    		this.State = State;
    	}
    	
    	// 쿼리 실행 메서드 -> 모든 쿼리문 공통
		@Override
		public byte[] executeQyery() {
			// insert 쿼리문
	        String sql = "insert into device values(?,?,?)";
	        
	        // device type의 id 생성
	        byte[] id = generateId(IDType.Device);
	        // 기본적으로 00 state로 설정, 이후 정보 갱신 시 바뀜
	        byte[] state = {0, 0};
	        
	        PreparedStatement pstmt = null;
	        
	        try 
	        {
	        	
	            pstmt = conn.prepareStatement(sql);        
	            pstmt.setBytes(1, id);
	            pstmt.setBytes(2, state);
	            pstmt.setBytes(3, OwnerId);

	            int result = pstmt.executeUpdate();
	            
	            // --------- Debugging ------------
	            if(result==1) 
	                System.out.println("device data insert success");
	                
	            System.out.print("inserted Device Id: ");
	        	for(int i = 0; i< id.length; i++)
	            	System.out.print(id[i]);
	        	System.out.println("");
	            // --------------------------------
	            
	        } 
	        catch (Exception e) 
	        {
	        	e.printStackTrace();
	            System.out.println("device data insert failed");
	        }    
	        finally 
	        {
	            try 
	            {
	                if(pstmt!=null && !pstmt.isClosed()) 
	                    pstmt.close();
	            } 
	            catch (Exception e2) { }
	        }
	        
	        // 생성한 device id 반환
	        return id;
		}
    	
    }
    
    public static class InsertBeacon implements SQL 
    {
    	String name;
    	byte[] OwnerId;
    	byte[] SpaceId;
    	byte[] MacAdr;
    	byte[] Id;
    	byte[] State;
    	
    	public InsertBeacon (String name, byte[] OwnerId, byte[] SpaceId, byte[] MacAdr, byte[] Id, byte[] State)
    	{
    		this.name = name;
    		this.OwnerId = OwnerId;
    		this.SpaceId = SpaceId;
    		this.MacAdr = MacAdr;
    		this.Id = Id;
    		this.State = State;
    	}
    	
    	// 쿼리 실행 메서드 -> 모든 쿼리문 공통
		@Override
		public byte[] executeQyery() {
			// insert 쿼리문
	        String sql = "insert into beacon values(?,?,?)";
	        
	        // beacon type의 id 생성
	        byte[] id = generateId(IDType.Beacon);
	        // 기본적으로 00 state로 설정, 이후 정보 갱신 시 바뀜
	        byte[] state = {0, 0};
	        
	        
	        // Temp
	        mainStationServer.updateid = id;
	        
	        PreparedStatement pstmt = null;
	        
	        try 
	        {
	        	
	            pstmt = conn.prepareStatement(sql);        
	            pstmt.setBytes(1, id);
	            pstmt.setBytes(2, state);
	            pstmt.setBytes(3, SpaceId);

	            int result = pstmt.executeUpdate();
	            
	            // --------- Debugging ------------
	            if(result==1) 
	                System.out.println("Beacon data insert success");
	                
	            System.out.print("inserted Beacon Id: ");
	        	for(int i = 0; i< id.length; i++)
	            	System.out.print(id[i]);
	        	System.out.println("");
	            // --------------------------------
	            
	        } 
	        catch (Exception e) 
	        {
	        	e.printStackTrace();
	            System.out.println("Beacon data insert failed");
	        }    
	        finally 
	        {
	            try 
	            {
	                if(pstmt!=null && !pstmt.isClosed()) 
	                    pstmt.close();
	            } 
	            catch (Exception e2) { }
	        }
	        
	        // 생성한 Beacon id 반환
	        return id;
		}
    	
    }
    
    public static class InsertRouter implements SQL 
    {
    	String name;
    	byte[] OwnerId;
    	byte[] SpaceId;
    	byte[] MacAdr;
    	byte[] Id;
    	byte[] State;
    	
    	public InsertRouter (String name, byte[] OwnerId, byte[] SpaceId, byte[] MacAdr, byte[] Id, byte[] State)
    	{
    		this.name = name;
    		this.OwnerId = OwnerId;
    		this.SpaceId = SpaceId;
    		this.MacAdr = MacAdr;
    		this.Id = Id;
    		this.State = State;
    	}
    	
    	// 쿼리 실행 메서드 -> 모든 쿼리문 공통
		@Override
		public byte[] executeQyery() {
			// insert 쿼리문
	        String sql = "insert into router values(?,?,?)";
	        
	        // beacon type의 id 생성
	        byte[] id = generateId(IDType.Router);
	        
	        PreparedStatement pstmt = null;
	        
	        try 
	        {
	        	
	            pstmt = conn.prepareStatement(sql);        
	            pstmt.setBytes(1, id);
	            pstmt.setString(2, name);
	            pstmt.setBytes(3, MacAdr);

	            int result = pstmt.executeUpdate();
	            
	            // --------- Debugging ------------
	            if(result==1) 
	                System.out.println("Router data insert success");
	                
	            System.out.print("inserted Router Id: ");
	        	for(int i = 0; i< id.length; i++)
	            	System.out.print(id[i]);
	        	System.out.println("");
	            // --------------------------------
	            
	        } 
	        catch (Exception e) 
	        {
	        	e.printStackTrace();
	            System.out.println("Router data insert failed");
	        }    
	        finally 
	        {
	            try 
	            {
	                if(pstmt!=null && !pstmt.isClosed()) 
	                    pstmt.close();
	            } 
	            catch (Exception e2) { }
	        }
	        
	        // 생성한 Router id 반환
	        return id;
		}
    	
    }
    
    public static class InsertSpace implements SQL 
    {
    	String name;
    	byte[] OwnerId;
    	byte[] SpaceId;
    	byte[] MacAdr;
    	byte[] Id;
    	byte[] State;
    	
    	public InsertSpace (String name, byte[] OwnerId, byte[] SpaceId, byte[] MacAdr, byte[] Id, byte[] State)
    	{
    		this.name = name;
    		this.OwnerId = OwnerId;
    		this.SpaceId = SpaceId;
    		this.MacAdr = MacAdr;
    		this.Id = Id;
    		this.State = State;
    	}
    	
    	// 쿼리 실행 메서드 -> 모든 쿼리문 공통
		@Override
		public byte[] executeQyery() {
			// insert 쿼리문
	        String sql = "insert into space values(?,?)";
	        
	        // beacon type의 id 생성
	        byte[] id = generateId(IDType.Space);
	        
	        PreparedStatement pstmt = null;
	        
	        try 
	        {
	        	
	            pstmt = conn.prepareStatement(sql);        
	            pstmt.setBytes(1, id);
	            pstmt.setString(2, name);

	            int result = pstmt.executeUpdate();
	            
	            // --------- Debugging ------------
	            if(result==1) 
	                System.out.println("Space data insert success");
	                
	            System.out.print("inserted Space Id: ");
	        	for(int i = 0; i< id.length; i++)
	            	System.out.print(id[i]);
	        	System.out.println("");
	            // --------------------------------
	            
	        } 
	        catch (Exception e) 
	        {
	        	e.printStackTrace();
	            System.out.println("Space data insert failed");
	        }    
	        finally 
	        {
	            try 
	            {
	                if(pstmt!=null && !pstmt.isClosed()) 
	                    pstmt.close();
	            } 
	            catch (Exception e2) { }
	        }
	        
	        // 생성한 Space id 반환
	        return id;
		}
    	
    }
    
    
    public static class UpdateState implements SQL 
    {
    	String name;
    	byte[] OwnerId;
    	byte[] SpaceId;
    	byte[] MacAdr;
    	byte[] Id;
    	byte[] State;
    	
    	public UpdateState (String name, byte[] OwnerId, byte[] SpaceId, byte[] MacAdr, byte[] Id, byte[] State)
    	{
    		this.name = name;
    		this.OwnerId = OwnerId;
    		this.SpaceId = SpaceId;
    		this.MacAdr = MacAdr;
    		this.Id = Id;
    		this.State = State;
    	}
    	
    	// 쿼리 실행 메서드 -> 모든 쿼리문 공통
		@Override
		public byte[] executeQyery() {
			
			String dataBase = null;
			
			IDType type = getTypeById(Id);
			if(type == IDType.Device)
				dataBase = "device";
			else if(type == IDType.Beacon)
				dataBase = "beacon";
			else // error
				return null;
			
			String sql = "update " +  dataBase + " set state =? where ID=?";

	        PreparedStatement pstmt = null;
	        try 
	        {
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setBytes(1, State);
	            pstmt.setBytes(2, Id);
	            pstmt.executeUpdate();
	            
	            System.out.print("수정된 id: ");
	            for(int i = 0; i < Id.length; i++)
	            	System.out.print(Id[i]);
	            System.out.print(" state : " );
	            for(int i = 0; i < 2; i++)
	            	System.out.print(State[i]);
	            System.out.println("");

	            
	        } catch (Exception e) 
	        {
	            System.out.println("update 예외 발생");
	        }    
	        finally 
	        {
	            try 
	            {
	                if(pstmt!=null && !pstmt.isClosed()) 
	                    pstmt.close();
	            } 
	            catch (Exception e2) {}
	        }
	        
			return Id;
		}
    	
    } 
    // data search 
    public void findById(String dataBase, byte[] id) 
    {
    	
    	// select 쿼리 생성
        String sql = "select * from " + dataBase + " where id = ?";
        PreparedStatement pstmt = null;
        try 
        {
            pstmt = conn.prepareStatement(sql);
            pstmt.setBytes(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) 
            {
                System.out.print("searched id: ");
            	for(int i = 0; i < 6; i++)
                    System.out.print(rs.getBytes("ID")[i]);
            	
                System.out.println(" name: " + rs.getString("name"));
            }
            
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
            System.out.println("select 메서드 예외발생");
        } 
        finally 
        {
            try 
            {
                if(pstmt!=null && !pstmt.isClosed()) 
                    pstmt.close();
            } catch (Exception e2) {}
        }
    }
    
    
    
    
}
