package com.imdeity.deitynether.cmd;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.ChatUtils;
import com.imdeity.deitynether.util.PlayerStats;

public class TimeSubCommand {

	private ChatUtils cu = new ChatUtils();
	
	public TimeSubCommand(Player p){
		if(p.getWorld() == DeityNether.config.getNetherWorld()){ //Get time left
			if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
				p.sendMessage(cu.format("&2You can stay here as long as you want honey <3", true));
			}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
				getTimeLeft(p);
			}else{
				p.sendMessage(cu.format("&4o.O how did you get in here?...", true));
			}
		}else{ //Get time remaining in nether/need to wait
			if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION)){
				p.sendMessage(cu.format("&2You can go to the nether anytime, babe <3", true));
			}else if(p.hasPermission(DeityNether.GENERAL_PERMISSION)){
				if(PlayerStats.hasTimeLeft(p)){
					getTimeLeft(p);
				}else{
					p.sendMessage(cu.format("&aYou need to wait &2" + PlayerStats.getWaitTimeLeft(p), true));
				}
			}else{
				p.sendMessage(cu.format("&4No nether for you! >:}", true));
			}
		}
	}

	private void getTimeLeft(Player p) {
		int tm = PlayerStats.getMinutesLeft(p);
		int ts = PlayerStats.getSecondsLeft(p);
		String m = "", s = "";
		if(tm != 0){
			m = tm + "m ";
		}else if(ts != 0){
			s = ts + "s ";
		}
		p.sendMessage(cu.format("&aYou have &2" + m + s + "&aleft in the nether", true));
	}
	
}
