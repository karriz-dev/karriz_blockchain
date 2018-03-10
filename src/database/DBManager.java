package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager 
{
	private static DBManager instance = null;
	
	private Connection conn = null;
	
	private DBManager() 
	{
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn = DriverManager.getConnection("jdbc:sqlite:");
		}catch(Exception e) {
			
		}
	}
	
	protected void finalize() throws Throwable{
		close();
		super.finalize();
	}
	
	public boolean close() throws Exception{
		if(this.conn != null) {
			this.conn.close();
			this.conn = null;
		}
		DBManager.instance = null;
		return true;
	}
	public synchronized static DBManager getInstance() {
		if(instance == null)
			instance = new DBManager();
		return instance;
	}
}
