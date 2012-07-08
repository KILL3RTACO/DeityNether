package com.imdeity.deitynether.sql;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityOfflinePlayer;
import com.imdeity.deitynether.obj.DeityPlayer;
import com.imdeity.deitynether.util.NetherTime;

public class NetherSQL {

	private Connection conn = null;
	private DeityNether plugin = null;
	private NetherTime nt = null;
	private String db, address, usr, pass;
	private int port;
	
	public NetherSQL(DeityNether instance) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		plugin = instance;
		nt = new NetherTime(plugin);
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		db = plugin.config.getMySqlDatabaseName();
		address = plugin.config.getMySqlServerAddress();
		port = plugin.config.getMySqlServerPort();
		usr = plugin.config.getMySqlDatabaseUsername();
		pass = plugin.config.getMySqlDatabasePassword();
		connect();
		createTables();
		setDefaultResetStatus();
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
	
	private void setDefaultResetStatus() {
		
	}
	
	public void setJoinTime(Player p){
		try {
			String name = p.getName();
			String sql = "SELECT * FROM `nether_actions` WHERE `player`='" + name + "'";
			ResultSet rs = getResultSet(sql);
			if(!rs.next()){ //Row doesn't exist
				sql = "INSERT INTO `nether_actions` (`player`, `action`) VALUES ('" + name + "' , 'join')";
				statement(sql);
				sql = "INSERT INTO `nether_stats` (`player`) VALUES ('" + name + "')";
				statement(sql);
			}else{ //Row exists
				sql = "UPDATE `nether_actions` SET `action`='join', `time`=NOW() WHERE `player`='" + name + "'" ;
				statement(sql);
				sql = "UPDATE `nether_stats` SET  `time_in_nether`='0' WHERE `player`='" + name + "'";
				statement(sql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setLeaveTime(Player player, boolean playerDied){
		try {
			String sql;
			sql = "UPDATE `nether_actions` SET `action`='leave', `time`=NOW() WHERE `player`='" + player.getName() + "'";
			statement(sql);
			if(playerDied){
				sql = "UPDATE `nether_stats` SET `time_in_nether`='3600'";
				statement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addTime(Player p){
		DeityPlayer player = new DeityPlayer(p, plugin);
		try {
			int timeWaited = player.getTimeWaited();
			int timeInNether = player.getTimeInNether();
			if(timeInNether != 3600){
				String sql = "UPDATE `nether_stats` SET `time_in_nether`='" + (timeWaited + 1) + "' WHERE `player`='" + p.getName() + "'";
				statement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addWaitTime(Player p){
		DeityPlayer player = new DeityPlayer(p, plugin);
		try {
			int timeWaited = player.getTimeWaited();
			int timeInNether = player.getTimeInNether();
			if(timeInNether == 3600){
				String sql = "UPDATE `nether_stats` SET `time_waited`='" + (timeWaited + 1) + "' WHERE `player`='" + p.getName() + "'";
				statement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addWaitTime(OfflinePlayer p){
		DeityOfflinePlayer player = new DeityOfflinePlayer(p, plugin);
		try {
			int timeWaited = player.getTimeWaited();
			String sql = "UPDATE `nether_stats` SET `time_waited`='" + (timeWaited + 1) + "' WHERE `player`='" + p.getName() + "'";
			if(timeWaited < nt.neededWaitTime)
				statement(sql);
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
	
	private void statement(String sql) throws SQLException{
		ensureConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.executeUpdate();
	}
}
