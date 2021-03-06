package com.revature.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionFactory {
	
	private static Logger logger = LogManager.getLogger(ConnectionFactory.class);
	private static Properties props = getJdbcProperties();
	
	// Magic Strings
	private static final String URL = props.getProperty("jdbc.url");
	private static final String USERNAME = props.getProperty("jdbc.username");
	private static final String PASSWORD = props.getProperty("jdbc.password");
	
	// Fail Fast
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			logger.error("Failed to load JDBC Driver: {}", e);
			System.exit(1);
		}
	}

	// Restrict Instantiation
	private ConnectionFactory() {}
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			Exceptions.logSQLException(e);
			return null;
		}
	}
	
	
	// All this method does is retrieve our particular properties needed to connect to DB
	private static Properties getJdbcProperties() {
		try {
			Properties props = new Properties();
			
			// Load the values from src/main/resources/application.properties
			// TL;DR - this method looks in src/main/resources for the file that you pass to getResourceAsStream()
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
			return props;
		} catch (IOException | NullPointerException e) {
			logger.error("Unable to locate JDBC Properties at src/main/resources/application.properties");
			logger.error("Stack Trace: ", e);
			throw new RuntimeException("Check Logs; Failed to get connection properties");
		}
	}
}
