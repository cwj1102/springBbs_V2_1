package com.pknu.bbs.bbs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Named;

@Named
public class OracleDBConnector {
	Connection con;
	private OracleDBConnector() {}
	static OracleDBConnector odbc = new OracleDBConnector();
	public static OracleDBConnector getInstance() {
		if(odbc==null) {
			odbc = new OracleDBConnector();
		}
		return odbc;
	}
	
	public Connection getConnection() {
		try {
			Class.forName("core.log.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
			con = DriverManager.getConnection(url, "koo", "1234");
			System.out.println("연결 완료");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
}
