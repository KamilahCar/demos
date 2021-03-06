package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.model.UserInfo;
import com.revature.util.ConnectionFactory;
import com.revature.util.Exceptions;

public class UserDaoImpl implements UserDao {
	
	// Eager singleton
//	private static final UserDao instance = new UserDaoImpl();
	
	private static final String ATTEMPT_LOGIN_QUERY = "SELECT * FROM users WHERE username = ? AND password = ?";
	
	
	// Lazy Singleton
	private static UserDao instance;
	
	// restrict instantiation
	private UserDaoImpl() {}

	@Override
	public UserInfo login(String username, String password) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			int index = 0;
			PreparedStatement stmt = conn.prepareCall(ATTEMPT_LOGIN_QUERY);
			stmt.setString(++index, username);
			stmt.setString(++index, password);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				return userFrom(resultSet);
			}
		} catch (SQLException e) {
			Exceptions.logSQLException(e);
		}
		return null;
	}

	
	// Eager 
//	public static UserDao getInstance() {
//		return instance;
//	}
	
	// Lazy
	public static UserDao getInstance() {
		if (instance == null) {
			instance = new UserDaoImpl();
		} 
		return instance;
	}
	
	
	private static UserInfo userFrom(ResultSet rs) throws SQLException {
		return new UserInfo(
				rs.getString("username"),
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("authority")
				);
	}
}
