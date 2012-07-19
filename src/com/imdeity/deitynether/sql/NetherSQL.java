package com.imdeity.deitynether.sql;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.imdeity.deitynether.DeityNether;

public class NetherSQL {

	private Connection conn = null;
	private String db, address, usr, pass;
	private int port;
	
	public NetherSQL() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		db = DeityNether.config.getMySqlDatabaseName();
		address = DeityNether.config.getMySqlServerAddress();
		port = DeityNether.config.getMySqlServerPort();
		usr = DeityNether.config.getMySqlDatabaseUsername();
		pass = DeityNether.config.getMySqlDatabasePassword();
		connect();
		createTables();
	}

	public void connect() throws SQLException{
			conn = DriverManager.getConnection("jdbc:mysql://" + address+ ":" + port + "/" + db, usr, pass);
	}
	
	public void createTables() throws SQLException{
		String sql = "CREATE TABLE IF NOT EXISTS `nether_actions` (`id` INT(16) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
				" `player` VARCHAR(16) NOT NULL, `action` VARCHAR(5) NOT NULL, `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)" +
				" ENGINE = MYISAM COMMENT = 'Nether action log for records of player joins/leaves'";
		statement(sql);
		sql = "CREATE TABLE IF NOT EXISTS `nether_stats` (`id` INT(16) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
				" `player` VARCHAR(16) NOT NULL, `time_in_nether` INT (4) DEFAULT '0', `time_waited` INT(10) NOT NULL DEFAULT '0'," +
				" UNIQUE(`player`))" +
				" ENGINE = MYISAM COMMENT = 'Current time in nether record for players'";
		statement(sql);
	}
	
	public void ensureConnection(){
		try {
			if(conn == null || !conn.isValid(5))
				connect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getResultSet(String sql) throws SQLException{
		ensureConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		return stmt.executeQuery();
	}
	
	public Timestamp getCurrentTime(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	public void statement(String sql) throws SQLException{
		ensureConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.executeUpdate();
	}
}
