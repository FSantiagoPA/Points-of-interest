package com.santiparra.db.JBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.Config;

public class BaseDeDatosSingleton {
	
	private static Connection conn;
	
	private BaseDeDatosSingleton() {
		
	}
	
	public static Connection connect() throws SQLException {
		if(conn == null) {
			conn = DriverManager.getConnection(Config.URL, Config.username, Config.passworld);
		}
		return conn;
	}
	
	public static Connection closeConnection() throws SQLException {
		if(conn != null) {
			conn.close();
			conn = null;
		}
		return conn;
	}
}
