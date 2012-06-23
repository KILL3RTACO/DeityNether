package com.imdeity.deitynether.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.imdeity.deitynether.DeityNether;

public class NetherSQL {

	private Connection conn = null;
	private DeityNether plugin = null;
	private String db, address, usr, pass;
	private int port;
	
	public NetherSQL(DeityNether instance) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		plugin = instance;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		db = plugin.config.getMySqlDatabaseName();
		address = plugin.config.getMySqlServerAddress();
		port = plugin.config.getMySqlServerPort();
		usr = plugin.config.getMySqlDatabaseUsername();
		pass = plugin.config.getMySqlDatabasePassword();
		connect();
//		createTables();
	}
	
	public void connect() throws SQLException{
			conn = DriverManager.getConnection("jdbc:mysql://" + address+ ":" + port + "/" + db, usr, pass);
	}
	
	public void createTables(){
		String sql = "CREATE TABLE IF NOT EXISTS `nether-players` ( `player` VARCHAR(16) NOT NULL `time` TIMSTAMP NOT NULL DEFAULT CURRENT TIMESTAMP) ";
	}
	
	public void ensureConnection(){
		try {
			if(conn == null || !conn.isValid(5)){
				connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addPlayerToTable(String playerName){
		try {
			String sql = "INSERT INTO `nether-players` (`player`) VALUES(" + playerName + ")";
			statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void statement(String sql) throws SQLException{
		ensureConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.executeUpdate();
	}
}
