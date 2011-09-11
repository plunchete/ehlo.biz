package utils;

import java.sql.Connection;
import java.sql.DriverManager;

import models.User;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.model.Database;

import siena.jdbc.ddl.DdlGenerator;

public class UpdateSchema {
	
	public static Database createFrontendDatabase() {
		DdlGenerator generator = new DdlGenerator();
		generator.addTable(User.class);
		return generator.getDatabase();
	}
	
	private static Connection openConnection(String url, String user, String pass) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(url, user, pass);
	}
	
	private static Platform createPlatform() {
		return PlatformFactory.createNewPlatformInstance("mysql");
	}
	
	public static String calculateFrontendChanges(String url, String user, String pass) throws Exception {
		Connection connection = openConnection(url, user, pass);
		String sql = createPlatform().getAlterTablesSql(connection, createFrontendDatabase());
		connection.close();
		if(sql.trim().length() == 0)
			return null;
		
		return sql;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(calculateFrontendChanges("jdbc:mysql://localhost/ehlo", "root", ""));
	}

}
