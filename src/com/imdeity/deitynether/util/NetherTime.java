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
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
			DeityPlayer.sendErrorMessage("You have unlimited time in the nether", p);
		}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
			if(DeityPlayer.hasTimeLeft(p, plugin)){
				int timeM = (3600 - DeityPlayer.getTimeInNether(p, plugin)) / 60;
				int timeS = (3600 - DeityPlayer.getTimeInNether(p, plugin)) % 60;
				String time = formatTime(timeM, timeS);
				DeityPlayer.sendInfoMessage("%aYou have %2" + time + " %aleft", p);
			}else{
				DeityPlayer.sendErrorMessage("You need to wait " + getWaitTimeLeft(p) + "%ctill you can enter the nether again", p);
			}
		}
	}
	
	public String getWaitTimeLeft(Player p){
		int needToWait = neededWaitTime - DeityPlayer.getTimeWaited(p, plugin);
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
