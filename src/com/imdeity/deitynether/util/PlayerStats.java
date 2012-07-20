package com.imdeity.deitynether.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;

public class PlayerStats {
	
	private static final int NEEDED_WAIT_TIME = DeityNether.config.getWaitTime();
	
	public static void setJoinTime(Player p){
		try {
			String name = p.getName();
			String sql = "SELECT * FROM `nether_actions` WHERE `player`='" + name + "'";
			ResultSet rs = DeityNether.db.getResultSet(sql);
			if(!rs.next()){ //Row doesn't exist
				sql = "INSERT INTO `nether_actions` (`player`, `action`) VALUES ('" + name + "' , 'join')";
				DeityNether.db.statement(sql);
				sql = "INSERT INTO `nether_stats` (`player`) VALUES ('" + name + "')";
				DeityNether.db.statement(sql);
			}else{ //Row exists
				sql = "UPDATE `nether_actions` SET `action`='join', `time`=NOW() WHERE `player`='" + name + "'" ;
				DeityNether.db.statement(sql);
				sql = "UPDATE `nether_stats` SET  `time_in_nether`='0' WHERE `player`='" + name + "'";
				DeityNether.db.statement(sql);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setLeaveTime(Player player, boolean playerDied){
		try {
			String sql;
			sql = "UPDATE `nether_actions` SET `action`='leave', `time`=NOW() WHERE `player`='" + player.getName() + "'";
			DeityNether.db.statement(sql);
			if(playerDied){
				sql = "UPDATE `nether_stats` SET `time_in_nether`='3600'";
				DeityNether.db.statement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addTime(Player p){
		try {
			int timeWaited = getTimeWaited(p);
			int timeInNether = getTimeInNether(p);
			if(timeInNether != 3600){
				String sql = "UPDATE `nether_stats` SET `time_in_nether`='" + (timeWaited + 1) + "' WHERE `player`='" + p.getName() + "'";
				DeityNether.db.statement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addWaitTime(Player p){
		try {
			int timeWaited = getTimeWaited(p);
			int timeInNether = getTimeInNether(p);
			if(timeInNether == 3600){
				String sql = "UPDATE `nether_stats` SET `time_waited`='" + (timeWaited + 1) + "' WHERE `player`='" + p.getName() + "'";
				DeityNether.db.statement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addWaitTime(OfflinePlayer p){
		try {
			int timeWaited = getTimeWaited(p);
			String sql = "UPDATE `nether_stats` SET `time_waited`='" + (timeWaited + 1) + "' WHERE `player`='" + p.getName() + "'";
			if(timeWaited < NEEDED_WAIT_TIME)
				DeityNether.db.statement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Timestamp getLastLeave(Player p){
		try {
			String name = p.getName();
			String sql = "SELECT `time` FROM `nether_actions` WHERE `player`='" + name + "' AND `action`='leave'";
			ResultSet rs = DeityNether.db.getResultSet(sql);
			rs.next();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getTimeInNether(Player p){
		try {
			String name = p.getName();
			String sql = "SELECT `time_in_nether` FROM `nether_stats` WHERE `player`='" + name + "'";
			ResultSet rs  = DeityNether.db.getResultSet(sql);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static int getTimeWaited(Player p){
		try {
			String name = p.getName();
			String sql = "SELECT `time_waited` FROM `nether_stats` WHERE `player`='" + name + "'";
			ResultSet rs = DeityNether.db.getResultSet(sql);
			if(rs.next()){
				return rs.getInt(1);
			}else{ //Row does not exist, they haven't entered nether before, therefore they don't need to wait
				return NEEDED_WAIT_TIME;
			}
		} catch (SQLException e) {
			return 0;
		}
	}
	public static int getTimeWaited(OfflinePlayer p){
		try {
			String name = p.getName();
			String sql = "SELECT `time_waited` FROM `nether_stats` WHERE `player`='" + name + "'";
			ResultSet rs = DeityNether.db.getResultSet(sql);
			if(rs.next()){
				return rs.getInt(1);
			}else{ //Row does not exist, they haven't entered nether before, therefore they don't need to wait
				return NEEDED_WAIT_TIME;
			}
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static boolean hasEnteredNether(Player p){
		try {
			String name = p.getName();
			String sql = "SELECT `id` FROM `nether_actions` WHERE `player`='" + name + "' AND `action`='join'";
			ResultSet rs = DeityNether.db.getResultSet(sql);
			if(!rs.next()){ //Row may not exist, but they also might've left and THAT row will exist
				sql = "SELECT `id` FROM `nether_actions` WHERE `player`='" + name + "' AND `action`='join'";
				rs = DeityNether.db.getResultSet(sql);
				if(rs.next()) //Row exists, this player has entered nether, but left
					return true;
				else //Row doesn't exist, this player hasn't entered the nether before
					return false;
			}else{ //They are in nether, therefore they have entered before
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static int getWaitHoursLeft(Player p){
		int needToWait = NEEDED_WAIT_TIME - getTimeWaited(p);
		return needToWait / 3600;
	}
	
	public static int getWaitMinutesLeft(Player p){
		int needToWait = NEEDED_WAIT_TIME - getTimeWaited(p);
		return (needToWait % 3600) / 60;
	}
	
	public static int getWaitSecondsLeft(Player p){
		int needToWait = NEEDED_WAIT_TIME - getTimeWaited(p);
		return (needToWait % 3600) % 60;
	}
	
	public static int getMinutesLeft(Player p){
		return (3600 - PlayerStats.getTimeInNether(p)) / 60;
	}
	
	public static int getSecondsLeft(Player p){
		return (3600 - PlayerStats.getTimeInNether(p)) % 60;
	}
	
	public static boolean hasWaited(Player p){
		int waited = getTimeWaited(p);
		if(NEEDED_WAIT_TIME - waited == 0) return true;
		else return false;
	}
	
	public static boolean hasTimeLeft(Player p){
		return 3600 - getTimeInNether(p) != 0;
	}
	
}
