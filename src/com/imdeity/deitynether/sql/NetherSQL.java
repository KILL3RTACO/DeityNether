package com.imdeity.deitynether.sql;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityOfflinePlayer;
import com.imdeity.deitynether.obj.DeityPlayer;

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
		createTables();
		setDefaultResetStatus();
	}
	
	public void connect() throws SQLException{
			conn = DriverManager.getConnection("jdbc:mysql://" + address+ ":" + port + "/" + db, usr, pass);
	}
	
	public void createTables() throws SQLException{
		String sql = "CREATE TABLE IF NOT EXISTS `nether-actions` (`id` INT(16) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
				" `player` VARCHAR(16) NOT NULL, `action` VARCHAR(5) NOT NULL, `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)" +
				" ENGINE = MYISAM COMMENT = 'Nether action log for records of player joins/leaves'";
		statement(sql);
		sql = "CREATE TABLE IF NOT EXISTS `nether-stats` (`id` INT(16) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
				"`player` VARCHAR(16) NOT NULL, `duration` INT (2) DEFAULT '0', UNIQUE(`player`))" +
				" ENGINE = MYISAM COMMENT = 'Current time in nether record for players'";
		statement(sql);
		sql = "CREATE TABLE IF NOT EXISTS `nether-wait-times` (`id` INT(16) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
				" `player` VARCHAR(16) NOT NULL, `duration` INT(5) DEFAULT '0', UNIQUE(`player`))" +
				"ENGINE = MYISAM COMMENT = 'Log for how long a player has waited to go to the nether'";
		statement(sql);
		sql= "CREATE TABLE IF NOT EXISTS `nether-reset-log` (`id` INT (1) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
				" `last-reset` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `needs-reset` INT(1) NOT NULL DEFAULT '0')" +
				" ENGINE = MYISAM COMMENT = 'Log for when the nether was last reset and if it needs resetting'";
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
	
	private void setDefaultResetStatus(){
		try {
			String sql = "SELECT * FROM `nether-reset-log` WHERE `id`='1'";
			ResultSet rs = getResultSet(sql);
			if(!rs.next()){
				sql = "INSERT INTO `nether-reset-log` (`last-reset`, `needs-reset`) VALUES (NOW(), '0')";
				statement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setJoinTime(Player p){
		try {
			String name = p.getName();
			String sql = "SELECT * FROM `nether-actions` WHERE `player`='" + name + "'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()){ //Row doesn't exist
				sql = "INSERT INTO `nether-actions` (`player`, `action`) VALUES ('" + name + "' , 'join')";
				statement(sql);
				sql = "INSERT INTO `nether-stats` (`player`) VALUES ('" + name + "')";
				statement(sql);
				sql = "INSERT INTO `nether-wait-times` (`player`) VALUES ('" + name + "')";
				statement(sql);
			}else{ //Row exists
				sql = "UPDATE `nether-actions` SET `action`='join', `time`=NOW() WHERE `player`='" + name + "'" ;
				statement(sql);
				sql = "UPDATE `nether-stats` SET `duration`='0'";
				statement(sql);
				sql = "UPDATE `nether-wait-times` SET `duration`='0'";
				statement(sql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setLeaveTime(Player player){
		try {
			String sql = "UPDATE `nether-actions` SET `action`='leave', `time`=NOW() WHERE `player`='" + player.getName() + "'";
			statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setResetTime(){
		try {
			String sql = "UPDATE `nether-reset-log` SET `last-reset`=NOW() WHERE `id`='1'";
			statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getResetStatus(){
		try {
			String sql = "SELECT `needs-reset` FROM `nether-reset-log` WHERE `id`='1'";
			ResultSet rs = getResultSet(sql);
			rs.next();
			if(rs.getInt(1) == 0)
				return false;
			else return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void overrideResetStatus(boolean status){ // 0 = false, 1 = true
		try {
			String sql;
			if(status)
				sql = "UPDATE `nether-reset-log` SET `needs-reset`='1' WHERE `id`='1'";
			else
				sql = "UPDATE `nether-reset-log` SET `needs-reset`='0' WHERE `id`='1'";
			statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addTime(DeityPlayer player){
		try {
			int duration = player.getTimeInNether();
			String sql = "UPDATE `nether-stats` SET `duration`='" + (duration + 1) + "' WHERE `player`='" + player.getName() +"'";
			statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addWaitTime(DeityPlayer player){
		try {
			int duration = player.getTimeWaited();
			String sql = "UPDATE `nether-wait-times` SET `duration`='" + (duration + 1) + "' WHERE `player`='" + player.getName() + "'";
			statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addWaitTime(DeityOfflinePlayer player){
		try {
			int duration = player.getTimeWaited();
			String sql = "UPDATE `nether-wait-times` SET `duration`='" + (duration + 1) + "' WHERE `player`='" + player.getName() + "'";
			statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void checkResetStatus(){
		int days = plugin.config.getRegenerationInterval();
		try {
			String sql = "SELECT `last-reset` FROM `nether-reset-log` WHERE `id`='1'";
			ResultSet rs = getResultSet(sql);
			rs.next();
			Timestamp reset = rs.getTimestamp(1);
			reset.setTime(reset.getTime() + (days * 24 * 60 * 60 * 1000));
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if(now.equals(reset) || now.after(reset)){
				overrideResetStatus(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getResultSet(String sql) throws SQLException{
		ensureConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		return stmt.executeQuery();
	}
	
	private void statement(String sql) throws SQLException{
		ensureConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.executeUpdate();
	}
}
