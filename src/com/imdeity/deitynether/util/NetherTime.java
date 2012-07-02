package com.imdeity.deitynether.util;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.obj.DeityPlayer;

public class NetherTime {

	private DeityNether plugin = null;
	
	public NetherTime(DeityNether instance){
		plugin = instance;
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
	
	private String formatTime(int mins, int secs){
		if(secs == 0){
			return mins + " minutes";
		}else if(mins == 0){
			return secs + " seconds";
		}else{
			return mins + "minutes and " + secs + " seconds";
		}
	}
	
}
