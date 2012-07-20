package com.imdeity.deitynether.cmd;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.PlayerStats;

public class TimeSubCommand {
	
	public TimeSubCommand(Player p){
		if(p.getWorld() == DeityNether.config.getNetherWorld()){ //Get time left
			p.sendMessage(DeityNether.lang.formatPlayTimeLeft(p, PlayerStats.getMinutesLeft(p), PlayerStats.getSecondsLeft(p)));
		}else{ //Get time remaining in nether/need to wait
			if(PlayerStats.hasTimeLeft(p) || PlayerStats.hasWaited(p)){
				p.sendMessage(DeityNether.lang.formatPlayTimeLeft(p, PlayerStats.getMinutesLeft(p), PlayerStats.getSecondsLeft(p)));
			}else{
				p.sendMessage(DeityNether.lang.formatWaitTimeLeft(p, PlayerStats.getWaitHoursLeft(p), 
						PlayerStats.getWaitMinutesLeft(p), PlayerStats.getWaitSecondsLeft(p)));
			}
		}
	}
	
}
