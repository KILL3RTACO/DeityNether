package com.imdeity.deitynether.cmd;

import org.bukkit.entity.Player;

import com.imdeity.deitynether.DeityNether;
import com.imdeity.deitynether.util.PlayerPorter;

public class LeaveSubCommand {

	private PlayerPorter porter = new PlayerPorter();
	
	public LeaveSubCommand(Player p){
		if(p.hasPermission(DeityNether.OVERRIDE_PERMISSION))
			porter.sendToOverworld(p, false);
		else if(p.hasPermission(DeityNether.GENERAL_PERMISSION))
			porter.sendToOverworld(p, true);
		else
			p.sendMessage(DeityNether.lang.formatInvalidPermissions());
	}
}
