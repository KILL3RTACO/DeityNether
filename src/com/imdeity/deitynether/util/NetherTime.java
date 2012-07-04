package com.imdeity.deitynether.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;

public class NetherTime {

	private DeityNether plugin = null;
	public int neededWaitTime;
	
	public NetherTime(DeityNether instance){
		plugin = instance;
		neededWaitTime = plugin.config.getWaitTime();
	}
	
	public void getTimeLeft(Player p){
		DeityPlayer player = new DeityPlayer(p, plugin);
		if(player.hasPermission(DeityNether.GENERAL_PERMISSION)){
			int timeM = (3600 - player.getTimeInNether()) / 60;
			int timeS = (3600 - player.getTimeInNether()) % 60;
			String time = formatTime(timeM, timeS);
			player.sendInfoMessage("%aYou have %2" + time + " %aleft");
		}else if(player.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			player.sendInfoMessage("You have unlimited time in the nether");
		}
	}
	
	public String getWaitTimeLeft(DeityPlayer player){
		int needToWait = neededWaitTime - player.getTimeWaited();
		int hours = needToWait / 3600;
		int minutes = (needToWait % 3600) / 60;
		int secs = (needToWait % 3600) % 60;
		return "%6" + hours + " hours " + minutes + " minutes and " + secs + " seconds"; 
	}
	
	private String formatTime(int mins, int secs){
		if(secs == 0){
			return mins + " minutes";
		}else if(mins == 0){
			return secs + " seconds";
		}else{
			return mins + "minutes and " + secs + " seconds";
		}
	}
	
	public boolean checkResetStatus(){
		try {
			String sql = "SELECT * FROM `nether-reset-log` WHERE `id`='1'";
			ResultSet rs = plugin.mysql.getResultSet(sql);
			rs.next();
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
}
