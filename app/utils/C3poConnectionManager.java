package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import siena.SienaException;
import siena.jdbc.AbstractConnectionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3poConnectionManager extends AbstractConnectionManager {
	
	private DataSource dataSource;

	private ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();

	public void closeConnection() {
		
		Connection connection = currentConnection.get();
		try {
			if(connection != null) {
				currentConnection.set(null);
				if(!connection.isClosed()) {
					connection.close();
				}
			}
		} catch(SQLException e) {
			throw new SienaException(e);
		}
	}

	public Connection getConnection() {
		
		Connection connection = currentConnection.get();
		try {
			if(connection == null || connection.isClosed()) {
				connection = dataSource.getConnection();
				currentConnection.set(connection);
			} else {
			}
		} catch(SQLException e) {
			throw new SienaException(e);
		}
		return connection;
	}



	public void init(Properties p) {
		String url    = p.getProperty("url");
		String user   = p.getProperty("user");
		String pass   = p.getProperty("password");
		
		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setUser(user);
		cpds.setPassword(pass);
		cpds.setJdbcUrl(url);
		cpds.setMaxPoolSize(10);
		cpds.setInitialPoolSize(5);
		cpds.setMinPoolSize(5);
		dataSource = cpds;
	}

}
