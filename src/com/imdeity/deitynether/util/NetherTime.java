package com.imdeity.deitynether.util;

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
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			player.sendInfoMessage("You have unlimited time in the nether");
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(player.hasTimeLeft()){
				int timeM = (3600 - player.getTimeInNether()) / 60;
				int timeS = (3600 - player.getTimeInNether()) % 60;
				String time = formatTime(timeM, timeS);
				player.sendInfoMessage("%aYou have %2" + time + " %aleft");
			}else{
				player.sendInfoMessage("%cYou need to wait " + getWaitTimeLeft(player) + "%ctill you can enter the nether again");
			}
		}
	}
	
	public String getWaitTimeLeft(DeityPlayer player){
		int needToWait = neededWaitTime - player.getTimeWaited();
		int hours = needToWait / 3600;
		int minutes = (needToWait % 3600) / 60;
		int secs = (needToWait % 3600) % 60;
		String h =  "", m = "", s = "";
		if(hours != 0)
			h = hours + "h ";
		if(minutes != 0)
			m = minutes + "m ";
		if(secs != 0)
			s = secs + "s ";
		return "%6" + h + m + s;
	}
	
	private String formatTime(int mins, int secs){
		if(secs == 0){
			return mins + "m";
		}else if(mins == 0){
			return secs + "s";
		}else if(secs == 0 && mins == 0){
			return "no time";
		}else{
			return mins + "m " + secs + "s ";
		}
	}
	
}
